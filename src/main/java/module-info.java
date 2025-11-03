module main.sprint3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens main.sprint3 to javafx.fxml;
    exports main.sprint3;
}