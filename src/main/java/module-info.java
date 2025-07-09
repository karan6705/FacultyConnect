module s25.cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens s25.cs151.application to javafx.fxml;
    exports s25.cs151.application;
    exports s25.cs151.application.controllers;
    opens s25.cs151.application.controllers to javafx.fxml;
    opens s25.cs151.application.models to javafx.base;
    exports s25.cs151.application.view;
    opens s25.cs151.application.view to javafx.fxml;
}