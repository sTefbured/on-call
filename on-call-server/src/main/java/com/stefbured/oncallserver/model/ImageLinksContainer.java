package com.stefbured.oncallserver.model;

import lombok.Data;

@Data
public class ImageLinksContainer {
    private String imageUrl;

    private String imageThumbnailUrl;

    private String mediumImageUrl;

    private String deleteImageUrl;
}
