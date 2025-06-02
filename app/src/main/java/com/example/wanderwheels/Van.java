package com.example.wanderwheels;

public class Van {
    private String name;
    private int imageResId;
    private String tagText;
    private int tagColor;
    private float rating;
    private int reviewCount;
    private int sleepCount;
    private String sleepType;
    private int currentPrice;

    private String[] features;
    private String category;

    public Van(String name, int imageResId, String tagText, int tagColor, float rating,
               int reviewCount, int sleepCount, String sleepType, int currentPrice,
               int originalPrice, String[] features, String category) {
        this.name = name;
        this.imageResId = imageResId;
        this.tagText = tagText;
        this.tagColor = tagColor;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.sleepCount = sleepCount;
        this.sleepType = sleepType;
        this.currentPrice = currentPrice;

        this.features = features;
        this.category = category;
    }

    // Getters
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public String getTagText() { return tagText; }
    public int getTagColor() { return tagColor; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public int getSleepCount() { return sleepCount; }
    public String getSleepType() { return sleepType; }
    public int getCurrentPrice() { return currentPrice; }

    public String[] getFeatures() { return features; }
    public String getCategory() { return category; }
    public String getVanClass() {return category;}
}
