module org.zakariafarih.adiscrapeview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.annotation;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens org.zakariafarih.adiscrapeview to javafx.fxml;
    opens org.zakariafarih.adiscrapeview.controller to javafx.fxml;
    opens org.zakariafarih.adiscrapeview.model to com.fasterxml.jackson.databind;
    exports org.zakariafarih.adiscrapeview;
}