package com.fpt.ida.idasignhub.data;

public class PageSizeVM {
    public String name;
    public float minWidth;
    public float maxWidth;
    public float minHeight;
    public float maxHeight;

    public PageSizeVM(String name, float minWidth, float maxWidth, float minHeight, float maxHeight) {
        this.name = name;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
}
