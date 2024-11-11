package org.zakariafarih.adiscrapeview.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductWrapper {
    @JsonProperty("product")
    private Product product;

    @JsonProperty("originUrl")
    private String originUrl;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getOriginUrl() { return originUrl; }
    public void setOriginUrl(String originUrl) { this.originUrl = originUrl; }
}
