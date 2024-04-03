module com.sisdist.sisdist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.sisdist.sisdist to javafx.fxml;
    exports com.sisdist.sisdist;

    opens com.sisdist.client to javafx.fxml;
    exports com.sisdist.client;
    opens com.sisdist.server to javafx.fxml;
    exports com.sisdist.server;

}