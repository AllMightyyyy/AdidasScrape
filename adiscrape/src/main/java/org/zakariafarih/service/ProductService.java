package org.zakariafarih.service;

import org.zakariafarih.client.ApiClient;
import org.zakariafarih.model.DetailedProduct;
import org.zakariafarih.model.Product;
import org.zakariafarih.util.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ApiClient apiClient;
    private final JsonParser jsonParser;

    public ProductService(ApiClient apiClient, JsonParser jsonParser) {
        this.apiClient = apiClient;
        this.jsonParser = jsonParser;
    }

    /**
     * Fetches all products using pagination.
     *
     * @param baseApiUrl The base API URL without pagination parameters.
     * @param pageSize The number of products per page.
     * @return A list of all Product objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Product> fetchAllProducts(String baseApiUrl, int pageSize) throws IOException {
        List<Product> allProducts = new ArrayList<>();
        int currentPage = 0;
        boolean hasMore = true;

        while (hasMore) {
            String paginatedUrl = String.format("%s&page=%d&size=%d", baseApiUrl, currentPage, pageSize);
            logger.info("Fetching page {}: {}", currentPage, paginatedUrl);
            String jsonResponse = apiClient.getProductListing(paginatedUrl);
            JsonNode rootNode = jsonParser.parseJson(jsonResponse);

            // Navigate to the products array
            JsonNode productsNode = rootNode.path("pageProps").path("products");
            if (productsNode.isArray()) {
                for (JsonNode productNode : productsNode) {
                    Product product = parseProduct(productNode);
                    if (product != null) {
                        allProducts.add(product);
                    }
                }
            }

            // Determine if more pages exist
            JsonNode paginationNode = rootNode.path("pageProps").path("pagination");
            if (paginationNode.isMissingNode()) {
                hasMore = false;
            } else {
                hasMore = paginationNode.path("hasMore").asBoolean(false);
                currentPage++;
            }
        }

        logger.info("Total products fetched: {}", allProducts.size());
        return allProducts;
    }

    /**
     * Parses a single product JSON node into a Product object.
     *
     * @param productNode The JSON node representing a product.
     * @return The Product object, or null if parsing fails.
     */
    private Product parseProduct(JsonNode productNode) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().treeToValue(productNode, Product.class);
        } catch (Exception e) {
            logger.error("Error parsing product node: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Fetches detailed product information for all products.
     *
     * @param products The list of products to fetch details for.
     * @return A list of DetailedProduct objects.
     */
    public List<DetailedProduct> fetchDetailedProducts(List<Product> products) {
        List<DetailedProduct> detailedProducts = new ArrayList<>();
        for (Product product : products) {
            String productId = product.getId();
            if (productId == null || productId.isEmpty()) {
                logger.warn("Skipping product with invalid ID.");
                continue;
            }
            try {
                logger.info("Fetching detailed info for product ID: {}", productId);
                String detailJson = apiClient.getProductDetails(productId);
                JsonNode detailNode = jsonParser.parseJson(detailJson);
                DetailedProduct detailedProduct = parseDetailedProduct(detailNode);
                if (detailedProduct != null) {
                    detailedProducts.add(detailedProduct);
                }
                // Optional: Add a short delay to be respectful to the server
                Thread.sleep(100); // 100 milliseconds
            } catch (IOException e) {
                logger.error("IOException while fetching details for product ID {}: {}", productId, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted: {}", e.getMessage());
            }
        }
        logger.info("Total detailed products fetched: {}", detailedProducts.size());
        return detailedProducts;
    }

    /**
     * Parses a detailed product JSON node into a DetailedProduct object.
     *
     * @param detailNode The JSON node representing detailed product information.
     * @return The DetailedProduct object, or null if parsing fails.
     */
    private DetailedProduct parseDetailedProduct(JsonNode detailNode) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().treeToValue(detailNode, DetailedProduct.class);
        } catch (Exception e) {
            logger.error("Error parsing detailed product node: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Saves the list of DetailedProduct objects to a JSON file.
     *
     * @param detailedProducts The list of DetailedProduct objects.
     * @param filePath The file path to save the JSON data.
     */
    public void saveDetailedProducts(List<DetailedProduct> detailedProducts, String filePath) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(detailedProducts);
            Files.write(Paths.get(filePath), json.getBytes(), StandardOpenOption.CREATE);
            logger.info("Detailed products saved to {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving detailed products to file: {}", e.getMessage());
        }
    }
}
