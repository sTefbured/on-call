package com.stefbured.oncallserver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefbured.oncallserver.exception.ExternalSystemRequestException;
import com.stefbured.oncallserver.model.ImageLinksContainer;
import com.stefbured.oncallserver.service.ImageUploadingService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
public class ImgbbImageUploadingServiceImpl implements ImageUploadingService {
    private static final Logger LOGGER = LogManager.getLogger(ImgbbImageUploadingServiceImpl.class);

    @Value("https://api.imgbb.com/1/upload?key=${imgbb.api.key}")
    private String imageUploadUrl;

    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final Random random;

    public ImgbbImageUploadingServiceImpl() {
        webClient = WebClient.create();
        mapper = new ObjectMapper();
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public ImageLinksContainer loadImage(MultipartFile image) {
        var namePart = String.valueOf(random.nextLong(0, Long.MAX_VALUE));
        var overriddenImage = new MultipartFile() {
            @Override
            public @NonNull String getName() {
                return namePart + image.getName();
            }

            @Override
            public String getOriginalFilename() {
                return namePart + image.getOriginalFilename()   ;
            }

            @Override
            public String getContentType() {
                return image.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return image.isEmpty();
            }

            @Override
            public long getSize() {
                return image.getSize();
            }

            @Override
            public byte @NonNull [] getBytes() throws IOException {
                return image.getBytes();
            }

            @Override
            public @NonNull InputStream getInputStream() throws IOException {
                return image.getInputStream();
            }

            @Override
            public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
                image.transferTo(dest);
            }
        };
        var builder = new MultipartBodyBuilder();
        builder.part("image", overriddenImage.getResource());
        var result = webClient.post()
                .uri(imageUploadUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            LOGGER.info("Imgbb upload result: {}", result);
            return getImageLinks(result);
        } catch (JsonProcessingException e) {
            LOGGER.error("Couldn't read JSON returned from Imgbb: result={}", result);
            throw new ExternalSystemRequestException("Couldn't read JSON returned from Imgbb", e);
        }
    }

    public void deleteImage(String deleteUri) {
        webClient.delete()
                .uri(deleteUri)
                .exchangeToMono(response -> {
                    if (!response.statusCode().is2xxSuccessful()) {
                        LOGGER.error("Error while image deletion: response={}", response);
                        throw new ExternalSystemRequestException("Error while image deletion");
                    }
                    return response.bodyToMono(String.class);
                })
                .block();
    }

    private ImageLinksContainer getImageLinks(String jsonResponseBody) throws JsonProcessingException {
        var dataNode = mapper.readTree(jsonResponseBody).findValue("data");
        var imageUrl = dataNode.findValue("image").findValue("url").asText();
        var thumbnailImageUrl = dataNode.findValue("thumb").findValue("url").asText();
        var mediumImageUrl = dataNode.findValue("display_url").asText();
        var deleteImageUrl = dataNode.findValue("delete_url").asText();
        var container = new ImageLinksContainer();
        container.setImageUrl(imageUrl);
        container.setMediumImageUrl(mediumImageUrl);
        container.setImageThumbnailUrl(thumbnailImageUrl);
        container.setDeleteImageUrl(deleteImageUrl);
        return container;
    }
}
