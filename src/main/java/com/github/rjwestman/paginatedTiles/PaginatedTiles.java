package com.github.rjwestman.paginatedTiles;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * <p>
 * A PaginatedTiles control combines the Pagination Control with the TilePane control.
 * The control needs a list of data items, a cell factory for those data items and
 * the width and height of the tiles specified (needed to calculate the tiles that
 * fit on one page as well as the page count)
 * </p>
 *
 * <h3>Styling</h3>
 * <i>Style class: paginated-tiles</i>
 * <p>
 * PaginatedTiles has all the pseudo-class states of javafx.scene.control.Control
 * </p>
 *
 * <h4>Substructure</h4>
 * <ul>
 *     <li>
 *         page - StackPane
 *         <ul>
 *             <li>tile-pane - TilePane</li>
 *         </ul>
 *     </li>
 *     <li>
 *         pagination-control - StackPane
 *         <ul>
 *             <li>
 *                 leftArrowButton - Button
 *                 <ul>
 *                     <li>leftArrow - StackPane</li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 rightArrowButton - Button
 *                 <ul>
 *                     <li>rightArrow - StackPane</li>
 *                 </ul>
 *             </li>
 *             <li>bullet-button - ToggleButton</li>
 *             <li>number-button - ToggleButton</li>
 *             <li>page-information - Label</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h3>Cell factory</h3>
 * <p>
 * The {@link #cellFactoryProperty() cellFactoryProperty} is a callback function
 * that is used to create the tileCells within the Pagination pageFactory. The
 * function is required for the functionality of the pagination control. The callback
 * function should load and return the custom PaginatedTilesCell.
 * </p>
 *
 * <h3>Creating a PaginatedTiles control:</h3>
 * A simple example of how to create a PaginatedTiles can be found in the paginatedTilesExamples package.
 * The concept is this:
 * <ul>
 *     <li>
 *         Create your own tileCell class that extends PaginatedTilesCell.
 *     </li>
 *     <li>
 *         Create a list of data, the data will be used to create your cell
 *     </li>
 *     <li>
 *         Create a PaginatedTiles control, specifying the list of data, your tileCellWidth and your tileCellHeight
 *     </li>
 *     <li>
 *         Set the cell factory
 *     </li>
 * </ul>
 *
 * @param <T> The class type that specifies that data that is visualised by your tileCell.
 */
public class PaginatedTiles<T> extends Control {

    /* **********************************************************************
     *                                                                      *
     * Constructors                                                         *
     *                                                                      *
     ***********************************************************************/

    /**
     * Constructs a new PaginatedTiles control with the specified list of data
     * and the dimensions of your tile cells.
     * The list of data is copied and the copy is used.
     *
     * @param itemList the list of data that is represented by your tile cell.
     * @param tileWidth the width of your tile cells - used to calculate the tiles that fit one one page and the page count.
     * @param tileHeight the height of your tile cells - used to calculate the tiles that fit one one page and the page count.
     */
    public PaginatedTiles (ObservableList<T> itemList, double tileWidth, double tileHeight) {
        this.itemList = new SimpleListProperty<T>(itemList);
        init(tileWidth, tileHeight);
    }

    /**
     * Constructs a new PaginatedTiles control with the specified list of data
     * and the dimensions of your tile cells.
     * The list of data is bidirectionally bound to the list that is used in this control to create the tile cells.
     *
     * @param itemList the list of data that is represented by your tile cell.
     * @param tileWidth the width of your tile cells - used to calculate the tiles that fit one one page and the page count.
     * @param tileHeight the height of your tile cells - used to calculate the tiles that fit one one page and the page count.
     */
    public PaginatedTiles (ListProperty<T> itemList, double tileWidth, double tileHeight) {
        this.itemList.bindBidirectional(itemList);
        init(tileWidth, tileHeight);
    }

    // Helper classes for the constructors

    /**
     * Initialization used by all constructors.
     *
     * @param tileWidth the width of your tile cells - used to calculate the tiles that fit one one page and the page count.
     * @param tileHeight the height of your tile cells - used to calculate the tiles that fit one one page and the page count.
     */
    private void init( double tileWidth, double tileHeight ) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        this.tileWidth = new SimpleDoubleProperty(tileWidth);
        this.tileHeight = new SimpleDoubleProperty(tileHeight);
        cellFactory = new SimpleObjectProperty<Callback<PaginatedTiles<T>, PaginatedTilesCell<T>>>(this, "cellFactory");
    }

    /* **********************************************************************
     *                                                                      *
     * Properties                                                           *
     *                                                                      *
     ***********************************************************************/

    private ListProperty<T> itemList;
    private DoubleProperty tileWidth;
    private DoubleProperty tileHeight;
    private ObjectProperty<Callback<PaginatedTiles<T>, PaginatedTilesCell<T>>> cellFactory;

    /* **********************************************************************
     *                                                                      *
     * Getter and Setter                                                    *
     *                                                                      *
     ***********************************************************************/

    // itemList
    public ObservableList<T> getItemList() {
        return itemList.get();
    }
    public ListProperty<T> itemListProperty() {
        return itemList;
    }
    public void setItemList(ObservableList<T> itemList) {
        this.itemList.set(itemList);
    }

    // tileWidth
    public double getTileWidth() {
        return tileWidth.get();
    }
    public DoubleProperty tileWidthProperty() {
        return tileWidth;
    }
    public void setTileWidth(double tileWidth) {
        this.tileWidth.set(tileWidth);
    }

    // tileHeight
    public double getTileHeight() {
        return tileHeight.get();
    }
    public DoubleProperty tileHeightProperty() {
        return tileHeight;
    }
    public void setTileHeight(double tileHeight) {
        this.tileHeight.set(tileHeight);
    }

    // cellFactory
    public Callback<PaginatedTiles<T>, PaginatedTilesCell<T>> getCellFactory() {
        return  cellFactory.get();
    }
    public ObjectProperty<Callback<PaginatedTiles<T>, PaginatedTilesCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }
    public void setCellFactory(Callback<PaginatedTiles<T>, PaginatedTilesCell<T>> cellFactory) {
        this.cellFactory.set(cellFactory);
    }

    /* **********************************************************************
     *                                                                      *
     * Methods                                                              *
     *                                                                      *
     ***********************************************************************/

    @Override
    protected Skin<?> createDefaultSkin() { return new PaginatedTilesSkin(this); }

    /* **********************************************************************
     *                                                                      *
     * Stylesheet handling                                                  *
     *                                                                      *
     ***********************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "paginated-tiles";

}
