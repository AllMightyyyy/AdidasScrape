package org.zakariafarih.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Product {
    private String id;

    @JsonProperty("modelNumber")
    private String modelNumber;

    private String title;

    @JsonProperty("subTitle")
    private String subTitle;

    private String url;

    private String image;

    @JsonProperty("priceData")
    private PriceData priceData;

    @JsonProperty("colourVariations")
    private List<String> colourVariations;

    private double rating;

    @JsonProperty("ratingCount")
    private int ratingCount;

    @JsonProperty("hoverImage")
    private String hoverImage;

    private Badge badge; // Optional

    // Getters and Setters

    // toString method for easy printing
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", priceData=" + priceData +
                ", colourVariations=" + colourVariations +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", hoverImage='" + hoverImage + '\'' +
                ", badge=" + badge +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }

    public List<String> getColourVariations() {
        return colourVariations;
    }

    public void setColourVariations(List<String> colourVariations) {
        this.colourVariations = colourVariations;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(String hoverImage) {
        this.hoverImage = hoverImage;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }
}
