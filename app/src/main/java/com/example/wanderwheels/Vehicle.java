package com.example.wanderwheels;

public class Vehicle {
    private String name;
    private int imageResId;
    private String tagText;
    private int tagColor;
    private float rating;
    private int reviewCount;
    private int sleepCount;
    private int feature1Icon;
    private String feature1Text;
    private int feature2Icon;
    private String feature2Text;
    private int price;
    private String category;

    public Vehicle() {
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public int getTagColor() {
        return tagColor;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = tagColor;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getSleepCount() {
        return sleepCount;
    }

    public void setSleepCount(int sleepCount) {
        this.sleepCount = sleepCount;
    }

    public int getFeature1Icon() {
        return feature1Icon;
    }

    public void setFeature1Icon(int feature1Icon) {
        this.feature1Icon = feature1Icon;
    }

    public String getFeature1Text() {
        return feature1Text;
    }

    public void setFeature1Text(String feature1Text) {
        this.feature1Text = feature1Text;
    }

    public int getFeature2Icon() {
        return feature2Icon;
    }

    public void setFeature2Icon(int feature2Icon) {
        this.feature2Icon = feature2Icon;
    }

    public String getFeature2Text() {
        return feature2Text;
    }

    public void setFeature2Text(String feature2Text) {
        this.feature2Text = feature2Text;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}