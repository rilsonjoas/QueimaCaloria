module com.example.queimacaloria {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens com.example.queimacaloria to javafx.fxml;
    exports com.example.queimacaloria;
}