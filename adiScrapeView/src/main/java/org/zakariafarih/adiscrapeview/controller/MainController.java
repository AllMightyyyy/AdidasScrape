package org.zakariafarih.adiscrapeview.controller;

import org.zakariafarih.adiscrapeview.Main;
import org.zakariafarih.adiscrapeview.dao.ProductDAO;
import org.zakariafarih.adiscrapeview.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    private GridPane productGrid;

    private ProductDAO productDAO = new ProductDAO();
    private int currentPage = 0;
    private int itemsPerPage = 6;

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        productGrid.getChildren().clear();
        try {
            List<Product> products = productDAO.getAllProducts(currentPage * itemsPerPage, itemsPerPage);
            int column = 0;
            int row = 0;

            for (Product product : products) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("product_item.fxml"));
                Node node = loader.load();

                ProductItemController controller = loader.getController();
                controller.setProduct(product);

                productGrid.add(node, column, row);

                column++;
                if (column == 3) { // 3 columns per row
                    column = 0;
                    row++;
                }
            }

            if (products.isEmpty() && currentPage > 0) {
                currentPage--;
                loadProducts();
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load products.");
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadProducts();
        }
    }

    @FXML
    private void handleNextPage() {
        try {
            int totalCount = productDAO.getTotalCount();
            if ((currentPage + 1) * itemsPerPage < totalCount) {
                currentPage++;
                loadProducts();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not retrieve total product count.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to refresh products after CRUD operations
    public void refresh() {
        loadProducts();
    }
}
