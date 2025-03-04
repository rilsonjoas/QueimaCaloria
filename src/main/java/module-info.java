module com.example.queimacaloria {
    requires javafx.fxml;
    requires javafx.controls;
    requires static lombok;
    requires kernel;
    requires io;
    requires layout;
    requires java.sql;

    opens com.example.queimacaloria to javafx.graphics, javafx.fxml;
    opens com.example.queimacaloria.controllers to javafx.fxml;

    exports com.example.queimacaloria;
    exports com.example.queimacaloria.controllers;
    exports com.example.queimacaloria.negocio;
    exports com.example.queimacaloria.interfaces;
    exports com.example.queimacaloria.excecoes;
    exports com.example.queimacaloria.dados;
}