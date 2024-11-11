package org.zakariafarih.adiscrapeview.dao;

import org.zakariafarih.adiscrapeview.model.Product;
import org.zakariafarih.adiscrapeview.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Create
    public void addProduct(Product product, String originUrl) throws SQLException {
        String sql = "INSERT INTO products (id, title, url, image, rating, badge_style, badge_text, model_number, sub_title, price, sale_price, discount_text, sold_out, colour_variations, rating_count, hover_image, origin_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getId());
            stmt.setString(2, product.getTitle());
            stmt.setString(3, product.getUrl());
            stmt.setString(4, product.getImage());
            stmt.setDouble(5, product.getRating());

            if (product.getBadge() != null) {
                stmt.setString(6, product.getBadge().getStyle());
                stmt.setString(7, product.getBadge().getText());
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.setString(8, product.getModelNumber());
            stmt.setString(9, product.getSubTitle());

            if (product.getPriceData() != null) {
                stmt.setDouble(10, product.getPriceData().getPrice());
                stmt.setDouble(11, product.getPriceData().getSalePrice());
                stmt.setString(12, product.getPriceData().getDiscountText());
                stmt.setBoolean(13, product.getPriceData().isSoldOut());
            } else {
                stmt.setNull(10, Types.DOUBLE);
                stmt.setNull(11, Types.DOUBLE);
                stmt.setNull(12, Types.VARCHAR);
                stmt.setNull(13, Types.BOOLEAN);
            }

            stmt.setString(14, product.getColourVariations());
            stmt.setInt(15, product.getRatingCount());
            stmt.setString(16, product.getHoverImage());
            stmt.setString(17, originUrl);

            stmt.executeUpdate();
        }
    }

    // Read All with Pagination
    public List<Product> getAllProducts(int offset, int limit) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getString("id"));
                product.setTitle(rs.getString("title"));
                product.setUrl(rs.getString("url"));
                product.setImage(rs.getString("image"));
                product.setRating(rs.getDouble("rating"));

                String badgeStyle = rs.getString("badge_style");
                String badgeText = rs.getString("badge_text");
                if (badgeStyle != null && badgeText != null) {
                    Product.Badge badge = new Product.Badge();
                    badge.setStyle(badgeStyle);
                    badge.setText(badgeText);
                    product.setBadge(badge);
                }

                product.setModelNumber(rs.getString("model_number"));
                product.setSubTitle(rs.getString("sub_title"));

                double price = rs.getDouble("price");
                double salePrice = rs.getDouble("sale_price");
                String discountText = rs.getString("discount_text");
                boolean soldOut = rs.getBoolean("sold_out");
                Product.PriceData priceData = new Product.PriceData();
                priceData.setPrice(price);
                priceData.setSalePrice(salePrice);
                priceData.setDiscountText(discountText);
                priceData.setSoldOut(soldOut);
                product.setPriceData(priceData);

                product.setColourVariations(rs.getString("colour_variations"));
                product.setRatingCount(rs.getInt("rating_count"));
                product.setHoverImage(rs.getString("hover_image"));

                products.add(product);
            }
        }

        return products;
    }

    // Update
    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET title=?, url=?, image=?, rating=?, badge_style=?, badge_text=?, model_number=?, sub_title=?, price=?, sale_price=?, discount_text=?, sold_out=?, colour_variations=?, rating_count=?, hover_image=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getUrl());
            stmt.setString(3, product.getImage());
            stmt.setDouble(4, product.getRating());

            if (product.getBadge() != null) {
                stmt.setString(5, product.getBadge().getStyle());
                stmt.setString(6, product.getBadge().getText());
            } else {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.setString(7, product.getModelNumber());
            stmt.setString(8, product.getSubTitle());

            if (product.getPriceData() != null) {
                stmt.setDouble(9, product.getPriceData().getPrice());
                stmt.setDouble(10, product.getPriceData().getSalePrice());
                stmt.setString(11, product.getPriceData().getDiscountText());
                stmt.setBoolean(12, product.getPriceData().isSoldOut());
            } else {
                stmt.setNull(9, Types.DOUBLE);
                stmt.setNull(10, Types.DOUBLE);
                stmt.setNull(11, Types.VARCHAR);
                stmt.setNull(12, Types.BOOLEAN);
            }

            stmt.setString(13, product.getColourVariations());
            stmt.setInt(14, product.getRatingCount());
            stmt.setString(15, product.getHoverImage());
            stmt.setString(16, product.getId());

            stmt.executeUpdate();
        }
    }

    // Delete
    public void deleteProduct(String productId) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            stmt.executeUpdate();
        }
    }

    // Get Total Count
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
