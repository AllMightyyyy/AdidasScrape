package org.zakariafarih;

import org.zakariafarih.client.ApiClient;
import org.zakariafarih.client.SeleniumCookieFetcher;
import org.zakariafarih.model.DetailedProduct;
import org.zakariafarih.model.Product;
import org.zakariafarih.service.ProductService;
import org.zakariafarih.util.ConfigLoader;
import org.zakariafarih.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Cookie;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Load configuration
        String baseApiUrl = ConfigLoader.getProperty("api.url"); // Ensure this URL is correct in config.properties
        String timeoutStr = ConfigLoader.getProperty("api.timeout");
        int timeout = timeoutStr != null ? Integer.parseInt(timeoutStr) : 30;

        // Initialize Selenium and fetch cookies
        SeleniumCookieFetcher cookieFetcher = new SeleniumCookieFetcher();
        List<Cookie> cookies;
        try {
            cookies = cookieFetcher.fetchCookies(baseApiUrl);
        } catch (Exception e) {
            logger.error("Failed to fetch cookies using Selenium: {}", e.getMessage());
            return;
        }

        // Initialize ApiClient with fetched cookies
        ApiClient apiClient = new ApiClient(cookies);

        // Initialize other components
        JsonParser jsonParser = new JsonParser();
        ProductService productService = new ProductService(apiClient, jsonParser);

        try {
            // Fetch products using pagination
            int pageSize = 20; // Example page size, set according to API documentation
            List<Product> products = productService.fetchAllProducts(baseApiUrl, pageSize);

            // Fetch detailed products
            List<DetailedProduct> detailedProducts = productService.fetchDetailedProducts(products);

            // Display detailed products (optional)
            for (DetailedProduct dp : detailedProducts) {
                if (dp != null) { // Check for null in case of parsing errors
                    System.out.println(dp);
                }
            }

            // Save detailed products to a consolidated JSON file
            productService.saveDetailedProducts(detailedProducts, "detailedProducts.json");
        } catch (IOException e) {
            logger.error("An error occurred while fetching products: {}", e.getMessage());
        }
    }
}
