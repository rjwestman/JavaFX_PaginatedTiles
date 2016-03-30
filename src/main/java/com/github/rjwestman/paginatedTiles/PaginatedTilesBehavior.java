package com.github.rjwestman.paginatedTiles;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class PaginatedTilesBehavior<T> extends BehaviorBase<PaginatedTiles<T>> {

    /* **********************************************************************
     *                                                                      *
     * Constructors                                                         *
     *                                                                      *
     ***********************************************************************/

    public PaginatedTilesBehavior(PaginatedTiles<T> control) {
        super(control, PAGINATEDGRID_BINDINGS);
    }

    /* **********************************************************************
     *                                                                      *
     * Setup KeyBindings                                                    *
     *                                                                      *
     ***********************************************************************/

    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";

    protected static final List<KeyBinding> PAGINATEDGRID_BINDINGS = new ArrayList<>();
    static {
        PAGINATEDGRID_BINDINGS.add(new KeyBinding(KeyCode.LEFT, LEFT));
        PAGINATEDGRID_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, RIGHT));
    }

    protected String matchActionForEvent(KeyEvent e) {
        String action = super.matchActionForEvent(e);
        if (action != null) {
            if (e.getCode() == KeyCode.LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = RIGHT;
                }
            } else if (e.getCode() == KeyCode.RIGHT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = LEFT;
                }
            }
        }
        return action;
    }

    @Override protected void callAction(String name) {
        if (LEFT.equals(name)) {
            PaginatedTilesSkin paginatedTilesSkin = (PaginatedTilesSkin)getControl().getSkin();
            paginatedTilesSkin.selectPrevious();
        } else if (RIGHT.equals(name)) {
            PaginatedTilesSkin paginatedTilesSkin = (PaginatedTilesSkin)getControl().getSkin();
            paginatedTilesSkin.selectNext();
        } else {
            super.callAction(name);
        }
    }

    /* **********************************************************************
     *                                                                      *
     * Mouse Event Handling                                                 *
     *                                                                      *
     ***********************************************************************/

    @Override public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    /* **********************************************************************
     *                                                                      *
     * Private Util Methods                                                 *
     *                                                                      *
     ***********************************************************************/

}
