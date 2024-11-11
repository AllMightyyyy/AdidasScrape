package org.zakariafarih.adiscrapeview.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String id;
    private String title;
    private String url;
    private String image;
    private double rating;
    private Badge badge;
    private String modelNumber;
    private String subTitle;
    private PriceData priceData;
    private String colourVariations;
    private int ratingCount;
    private String hoverImage;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }

    public String getColourVariations() {
        return colourVariations;
    }

    public void setColourVariations(String colourVariations) {
        this.colourVariations = colourVariations;
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

    // Nested Badge Class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Badge {
        private String style;
        private String text;

        // Getters and Setters
        public String getStyle() { return style; }
        public void setStyle(String style) { this.style = style; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    // Nested PriceData Class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceData {
        private double price;
        private double salePrice;
        private String discountText;
        private boolean soldOut;

        // Getters and Setters
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public double getSalePrice() { return salePrice; }
        public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

        public String getDiscountText() { return discountText; }
        public void setDiscountText(String discountText) { this.discountText = discountText; }

        public boolean isSoldOut() { return soldOut; }
        public void setSoldOut(boolean soldOut) { this.soldOut = soldOut; }
    }

}
