package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.ImageLinksContainer;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadingService {
    ImageLinksContainer loadImage(MultipartFile image);
}
