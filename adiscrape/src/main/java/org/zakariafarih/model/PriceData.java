package org.zakariafarih.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceData {
    private double price;
    private double salePrice;
    private String discountText;
    private boolean isSoldOut;

    @Override
    public String toString() {
        return "PriceData{" +
                "price=" + price +
                ", salePrice=" + salePrice +
                ", discountText='" + discountText + '\'' +
                ", isSoldOut=" + isSoldOut +
                '}';
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}
