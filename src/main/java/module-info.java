module com.example.queimacaloria {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires layout;
    requires kernel;

    opens com.example.queimacaloria to javafx.graphics, javafx.fxml;
    opens com.example.queimacaloria.controllers to javafx.fxml;

    exports com.example.queimacaloria;
    exports com.example.queimacaloria.controllers;
    exports com.example.queimacaloria.negocio;
    exports com.example.queimacaloria.interfaces;
    exports com.example.queimacaloria.excecoes;
    exports com.example.queimacaloria.dados;
}