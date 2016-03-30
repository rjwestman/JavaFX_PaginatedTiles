package com.github.rjwestman.paginatedTiles;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Test1Cell extends PaginatedTilesCell<Test1Data> {

    private Label label1;
    private Label label2;

    public Test1Cell() {
        label1 = new Label();
        label2 = new Label();

        VBox vBox = new VBox(label1, label2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-border-color: black; -fx-background-color: lightcyan");

        getChildren().add(vBox);
        setStyle("-fx-border-color: black; -fx-background-color: lightcyan");

        setMinWidth(186);
        setMaxWidth(186);
        setMinHeight(100);
        setMaxHeight(100);
    }

    @Override
    public void updateCell(Test1Data item) {
        label1.setText(item.getVal1());
        label2.setText(item.getVal2());
    }
}
