package com.github.rjwestman.paginatedTiles;

import javafx.scene.layout.StackPane;

/**
 * Classes derived by this abstract class are used to represent data as tile cells in the PaginatedTiles control.
 *
 * @param <T> The class type that specifies that data that is visualised by this cell.
 */
public abstract class PaginatedTilesCell<T> extends StackPane {

    /**
     * Create the objects needed for the tile cell.
     */
    public PaginatedTilesCell() {
    }

    /**
     * Connects the data item with the data representation (tile cell).
     *
     * @param item the data item that is represented by this cell.
     */
    public abstract void updateCell(T item);

}
