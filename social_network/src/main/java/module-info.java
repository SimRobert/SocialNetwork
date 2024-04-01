module com.example.lab8_9_map {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab8_9_map to javafx.fxml;
    exports com.example.lab8_9_map;
    exports com.example.lab8_9_map.GUI;
    opens com.example.lab8_9_map.GUI to javafx.fxml;

    opens com.example.lab8_9_map.domain to javafx.base;
    exports com.example.lab8_9_map.domain;
}