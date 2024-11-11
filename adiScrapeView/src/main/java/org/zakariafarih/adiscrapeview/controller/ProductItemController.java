package org.zakariafarih.adiscrapeview.controller;

import javafx.scene.layout.GridPane;
import org.zakariafarih.adiscrapeview.Main;
import org.zakariafarih.adiscrapeview.dao.ProductDAO;
import org.zakariafarih.adiscrapeview.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProductItemController {

    @FXML
    private ImageView productImage;

    @FXML
    private Label productTitle;

    @FXML
    private Label productPrice;

    private Product product;
    private ProductDAO productDAO = new ProductDAO();

    public void setProduct(Product product) {
        this.product = product;
        productTitle.setText(product.getTitle());
        productPrice.setText("$" + product.getPriceData().getPrice());

        // Load image
        Image img = new Image(product.getImage(), true);
        productImage.setImage(img);
    }

    @FXML
    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit_product.fxml"));
            GridPane page = loader.load();  // Changed from VBox to GridPane

            EditProductController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(page));
            stage.showAndWait();

            // After editing, refresh the main view
            Stage primaryStage = (Stage) productImage.getScene().getWindow();
            MainController mainController = (MainController) primaryStage.getScene().getRoot().getUserData();
            mainController.refresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load edit window.");
        }
    }

    @FXML
    private void handleDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setContentText("Are you sure you want to delete this product?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    productDAO.deleteProduct(product.getId());
                    // Refresh the main view
                    Stage primaryStage = (Stage) productImage.getScene().getWindow();
                    MainController mainController = (MainController) primaryStage.getScene().getRoot().getUserData();
                    mainController.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Could not delete product.");
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
