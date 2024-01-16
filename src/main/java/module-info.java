module karimi.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    opens karimi.firstscreen to javafx.fxml;
    exports karimi.firstscreen;
    exports Controller;
    opens Controller to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
}