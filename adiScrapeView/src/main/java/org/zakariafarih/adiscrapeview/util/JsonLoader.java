package org.zakariafarih.adiscrapeview.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zakariafarih.adiscrapeview.Main;
import org.zakariafarih.adiscrapeview.dao.ProductDAO;
import org.zakariafarih.adiscrapeview.model.Product;
import org.zakariafarih.adiscrapeview.model.ProductWrapper;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JsonLoader {

    private ProductDAO productDAO = new ProductDAO();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void loadProductsFromJson() {
        try (InputStream input = Objects.requireNonNull(Main.class.getResource("detailedProducts.json")).openStream()) {
            if (input == null) {
                System.err.println("File not found: /products.json");
                return;
            }

            List<ProductWrapper> productWrappers = objectMapper.readValue(input, new TypeReference<List<ProductWrapper>>() {});

            for (ProductWrapper wrapper : productWrappers) {
                Product product = wrapper.getProduct();
                String originUrl = wrapper.getOriginUrl();
                productDAO.addProduct(product, originUrl);
            }

            System.out.println("Products loaded from JSON and inserted into the database.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load products from JSON.");
        }
    }

    public static void main(String[] args) {
        new JsonLoader().loadProductsFromJson();
    }
}
