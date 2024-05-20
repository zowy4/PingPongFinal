module singleton.pingpongfinal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens singleton.pingpongfinal to javafx.fxml;
    exports singleton.pingpongfinal;
}