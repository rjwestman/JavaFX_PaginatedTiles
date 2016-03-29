package com.github.rjwestman.paginatedTilesExamples;

import com.github.rjwestman.paginatedTiles.PaginatedTiles;
import com.github.rjwestman.paginatedTiles.PaginatedTilesCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Example1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label label = new Label("Example:");
        root.getChildren().add(label);

        // PAGINATED_TILES CODE START

        // create example data
        ObservableList<Example1Data> itemList = FXCollections.observableArrayList();
        for ( int i = 1; i <= 100; i++ ) {
            itemList.add(new Example1Data("Cell", ""+i));
        }

        // Create PaginatedTiles object and cell factory
        PaginatedTiles<Example1Data> paginatedTiles = new PaginatedTiles<Example1Data>(itemList, 120, 60);
        paginatedTiles.setCellFactory(new Callback<PaginatedTiles<Example1Data>, PaginatedTilesCell<Example1Data>>() {
            @Override
            public PaginatedTilesCell<Example1Data> call(PaginatedTiles<Example1Data> param) {
                return new Example1Cell();
            }
        });

        // Add the PaginatedTiles object to the example layout. (Grow needed to make it fill the layout)
        root.getChildren().add(paginatedTiles);
        root.setVgrow(paginatedTiles, Priority.ALWAYS);

        // PAGINATED_TILES CODE END

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("testStyle.css").toExternalForm());

        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(375);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
