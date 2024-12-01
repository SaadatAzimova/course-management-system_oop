module org.example.finaloop1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;


    opens org.example.finaloop1 to javafx.fxml;
    exports org.example.finaloop1;
}