module org.example.finaloop1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.finaloop1 to javafx.fxml;
    exports org.example.finaloop1;
}