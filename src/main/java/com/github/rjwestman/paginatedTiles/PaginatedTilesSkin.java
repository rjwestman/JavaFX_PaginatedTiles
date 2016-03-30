package com.github.rjwestman.paginatedTiles;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Pagination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class PaginatedTilesSkin<T> extends BehaviorSkinBase<PaginatedTiles<T>, PaginatedTilesBehavior<T>> {

    /* **********************************************************************
     *                                                                      *
     * Constructors                                                         *
     *                                                                      *
     ***********************************************************************/

    public PaginatedTilesSkin(PaginatedTiles control) {
        super(control, new PaginatedTilesBehavior(control));
        init();
    }

    private void init() {
        if ( getSkinnable().getCellFactory() != null ) {
            createLayout();
        }
    }

    /* **********************************************************************
     *                                                                      *
     * Control State Changes                                                *
     *                                                                      *
     ***********************************************************************/

    /**
     * Changes the pagination page to the next one if possible.
     */
    public void selectNext() {
        if ( pagination.getCurrentPageIndex() < pagination.getPageCount() - 1) {
            pagination.setCurrentPageIndex(pagination.getCurrentPageIndex() + 1);
        }
    }

    /**
     * Changes the pagination page to the previous one if possible.
     */
    public void selectPrevious() {
        if ( pagination.getCurrentPageIndex() > 0) {
            pagination.setCurrentPageIndex(pagination.getCurrentPageIndex() -1);
        }
    }

    /* **********************************************************************
     *                                                                      *
     * Skin Layout                                                          *
     *                                                                      *
     ***********************************************************************/

    private Pagination pagination;
    private int tilesPerPage;
    private TilePane currentPage;

    /**
     * Creates the base layout of this control, which is basically only a pagination.
     * Because making the public pagination methods accessible does not make sense
     * for this control it was not extended, but used
     */
    private void createLayout() {
        pagination = new Pagination();
        pagination.setPageFactory(this::pageFactory);

        getSkinnable().itemListProperty().addListener( (ListChangeListener<T>) listener -> onListChange());

        getChildren().add(pagination);
    }

    /**
     * The page factory for the pagination control used in PaginatedTiles.
     * Pages consist of a page-container (StackPane) and the page itself (Tile-Pane), to allow
     * for more flexible alignment options. The css property for the page-container is "page"
     * and for the page it is "tile-pane".
     * Page creation / rebuild is initiated in 3 ways: Initial call, resizing, item list changes
     *
     * @param pageIndex the index of the page that is to be returned
     * @return returns the built page
     */
    private StackPane pageFactory(int pageIndex ) {

        // init page
        TilePane page = new TilePane();
        page.getStyleClass().add("tile-pane");
        currentPage = page;

        // init pageContainer
        StackPane pageContainer = new StackPane(page);

        // sizeListeners
        registerSizeListeners(pageContainer,page);

        // Fill page
        if ( tilesPerPage != 0 ) {
            fillPage(page,pageIndex);
        }

        Platform.runLater( () -> {
        });

        return  pageContainer;
    }

    /**
     * Registers listeners to the size properties of the pageContainer, that call the onResize method.
     * @param pageContainer the pageContainer of the page
     */
    private void registerSizeListeners( StackPane pageContainer, TilePane page ) {
        pageContainer.heightProperty().addListener( observable -> {
            if (pageContainer.getWidth() != 0 && pageContainer.getHeight() != 0) {
                onResize(pageContainer, page);
            }
        });
        pageContainer.widthProperty().addListener( observable -> {
            if (pageContainer.getWidth() != 0 && pageContainer.getHeight() != 0) {
                onResize(pageContainer, page);
            }
        });
    }

    /**
     * Fills a page with tile cells using the given cell factory.
     *
     * @param page the page that is to be filled
     * @param pageIndex the index of the page - used to add the correct set of tile cells
     */
    private void fillPage( TilePane page, int pageIndex ) {
        int startIndex = pageIndex * tilesPerPage;
        int endIndex = Math.min( getSkinnable().getItemList().size(), startIndex+tilesPerPage);
        for ( int i = startIndex; i < endIndex; i++ ) {
            PaginatedTilesCell<T> paginatedTilesCell = getSkinnable().getCellFactory().call(getSkinnable());
            paginatedTilesCell.updateCell(getSkinnable().getItemList().get(i));
            page.getChildren().add(paginatedTilesCell);
        }
    }

    /**
     * Gets called on the resize event of the pageContainer.
     * It recalculates the tile cells that fit on a page and the resulting page count change.
     * If the resizing caused changes this method calls the correct follow-up methods to rebuild the page.
     *
     * @param pageContainer the pageContainer of a page
     */
    private void onResize( StackPane pageContainer, TilePane page ) {
        int tileCountHorizontal = 1;
        int tileCountVertical = 1;
        int tilesPerPage;
        int pageCount;

        double tileWidth = getSkinnable().getTileWidth();
        double tileHeight = getSkinnable().getTileHeight();
        double tileHGap = page.getHgap();
        double tileVGap = page.getVgap();

        // Calc tilesPerPage
        tileCountHorizontal += ( pageContainer.getWidth() - tileWidth - 2 ) / ( tileWidth + tileHGap );
        tileCountVertical += (pageContainer.getHeight() - tileHeight - 2 ) / ( tileHeight + tileVGap );
        tilesPerPage = tileCountHorizontal * tileCountVertical;
        tilesPerPage = ( tilesPerPage == 0 ) ? 1 : tilesPerPage;

        // Calc pageCount
        pageCount = getSkinnable().getItemList().size() / tilesPerPage + 1;

        // Set page width to allow alignment using the pageContainer
        page.setMinWidth( (tileWidth * tileCountHorizontal) + (tileHGap * (tileCountHorizontal-1)) );
        page.setMaxWidth( (tileWidth * tileCountHorizontal) + (tileHGap * (tileCountHorizontal-1)) );
        page.setMinHeight( (tileHeight * tileCountVertical) + (tileVGap * (tileCountVertical-1)) );
        page.setMaxHeight( (tileHeight * tileCountVertical) + (tileVGap * (tileCountVertical-1)) );

        if ( pagination.getPageCount() != pageCount ) {
            this.tilesPerPage = tilesPerPage;
            Platform.runLater( () -> {
                // This will recall the pageFactory - no further rebuilding of current page needed
                pagination.setPageCount(pageCount);
            });
        } else if ( this.tilesPerPage != tilesPerPage ){
            // If item count changed, rebuild the page
            this.tilesPerPage = tilesPerPage;
            currentPage.getChildren().clear();
            fillPage(currentPage, pagination.getCurrentPageIndex());
        }
    }

    /**
     * Gets called when the list of data that is represented by the tile cells changes.
     * If the pageCount changes because of this, it sets the page count (which will make
     * the page factory be called again by Pagination), else it rebuilds the current page.
     */
    private void onListChange() {
        int pageCount;

        pageCount = getSkinnable().getItemList().size() / tilesPerPage + 1;

        if ( pagination.getPageCount() != pageCount ) {
            Platform.runLater( () -> {
                // This will recall the pageFactory - no further rebuilding of current page needed
                pagination.setPageCount(pageCount);
            });
        } else if ( currentPage != null ){
            currentPage.getChildren().clear();
            fillPage(currentPage, pagination.getCurrentPageIndex());
        }
    }

    /* **********************************************************************
     *                                                                      *
     * Style Sheet Handling                                                 *
     *                                                                      *
     ***********************************************************************/

}
