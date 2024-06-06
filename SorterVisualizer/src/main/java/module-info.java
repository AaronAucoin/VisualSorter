module com.example.sortervisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.sortervisualizer to javafx.fxml;
    exports com.example.sortervisualizer;
}