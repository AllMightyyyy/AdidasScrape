package org.zakariafarih.adiscrapeview.controller;

import org.zakariafarih.adiscrapeview.dao.ProductDAO;
import org.zakariafarih.adiscrapeview.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EditProductController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField priceField;

    // Add other fields as necessary

    private Product product;
    private ProductDAO productDAO = new ProductDAO();

    public void setProduct(Product product) {
        this.product = product;
        titleField.setText(product.getTitle());
        priceField.setText(String.valueOf(product.getPriceData().getPrice()));
        // Initialize other fields
    }

    @FXML
    private void handleSave() {
        product.setTitle(titleField.getText());
        try {
            double price = Double.parseDouble(priceField.getText());
            product.getPriceData().setPrice(price);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Price must be a number.");
            return;
        }

        // Update other fields

        try {
            productDAO.updateProduct(product);
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not update product.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
