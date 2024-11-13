package com.fpt.ida.idasignhub.data.mock;

public class OCRGoogleMockData {
  String text;
  BoundingBox boundingBox;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}
