package com.example.ca1;

public class GalleryItem {
    private final String name;
    private final int imgName;

    public GalleryItem(String name, int imgName) {
        this.name = name;
        this.imgName = imgName;
    }

    public String getName() {
        return name;
    }

    public int getImgName() {
        return imgName;
    }
}
