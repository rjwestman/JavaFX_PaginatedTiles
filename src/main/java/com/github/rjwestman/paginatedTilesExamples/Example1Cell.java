package com.github.rjwestman.paginatedTilesExamples;

import com.github.rjwestman.paginatedTiles.PaginatedTilesCell;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Example1Cell extends PaginatedTilesCell<Example1Data> {

    Label label1;
    Label label2;

    public Example1Cell() {
        label1 = new Label();
        label2 = new Label();

        VBox vBox = new VBox(label1, label2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-border-color: black; -fx-background-color: lightcyan");

        getChildren().add(vBox);
        setStyle("-fx-border-color: black; -fx-background-color: lightcyan");

        setMinWidth(120);
        setMaxWidth(120);
        setMinHeight(60);
        setMaxHeight(60);
    }

    @Override
    public void updateCell(Example1Data item) {
        label1.setText(item.getVal1());
        label2.setText(item.getVal2());
    }
}
