package com.sisdist.client;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class ClientController {
    @FXML
    private Label pageText;

    @FXML
    protected void onClickEvent() {
        pageText.setText("O botao foi apertado");
    }

}
