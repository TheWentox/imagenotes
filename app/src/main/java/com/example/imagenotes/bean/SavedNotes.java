package com.example.imagenotes.bean;

import android.graphics.Bitmap;

public class SavedNotes {
    private String text;
    private Bitmap image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
