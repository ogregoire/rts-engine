package com.ts.rts.scene.selection;

import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.ts.rts.RTSGame;
import com.ts.rts.datastructure.geom.Vector2;
import com.ts.rts.scene.unit.PositionPhysicalEntity;
import com.ts.rts.scene.unit.Unit;
import com.ts.rts.scene.unit.group.UnitGroup;
import com.ts.rts.scene.unit.group.UnitGroupManager;
import com.ts.rts.util.VectorPool;

/**
 * Manages the selection of entities.
 * 
 * @author Toni Sagrista
 * 
 */
public class Selection {

    /**
     * The game
     */
    private RTSGame game;

    /**
     * Is there an active selection in process?
     */
    public boolean active = false;

    /**
     * Selection start point
     */
    public Vector2 start;

    /**
     * Selection end point
     */
    public Vector2 end;

    /**
     * Selected units
     */
    public UnitGroup selected;

    private ShapeRenderer shapeRenderer;

    private Rectangle sel;

    public Selection(RTSGame game) {
	this.game = game;
	start = VectorPool.getObject();
	end = VectorPool.getObject();
	// The selection is in screen coordinates
	shapeRenderer = RTSGame.game.screenShapeRenderer;
	this.selected = null;
	sel = new Rectangle();
    }

    public void render() {
	if (active) {
	    Rectangle r = getRectangle();
	    shapeRenderer.begin(ShapeType.Line);
	    shapeRenderer.setColor(new Color(1f, 1f, 0f, 1f));
	    shapeRenderer.rect(r.x, r.y, r.width, r.height);
	    shapeRenderer.rect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
	    shapeRenderer.end();
	}
    }

    /**
     * Triggers the selection of the units inside the selection bounds.
     */
    public void select() {
	if (active) {
	    start.x += RTSGame.getCamera().pos.x - RTSGame.getCamera().canvasWidth / 2;
	    start.y += RTSGame.getCamera().pos.y - RTSGame.getCamera().canvasHeight / 2;
	    end.x += RTSGame.getCamera().pos.x - RTSGame.getCamera().canvasWidth / 2;
	    end.y += RTSGame.getCamera().pos.y - RTSGame.getCamera().canvasHeight / 2;

	    selectUnitsIn(getRectangle());
	}
    }

    /**
     * Selects the units inside the given rectangle
     * 
     * @param r
     *            The rectangle in canvas coordinates
     */
    public void selectUnitsIn(Rectangle r) {
	// Translate rectangle to map coordinates, use camera
	r.setX(r.getX());
	r.setY(r.getY());

	selected = UnitGroupManager.getInstance().getSelectionUnitGroup();
	Set<PositionPhysicalEntity> inside = game.getInsideUnits(r);
	if (!inside.isEmpty()) {
	    clearSelection();
	    for (PositionPhysicalEntity u : inside) {
		if (u instanceof Unit) {
		    Unit unit = (Unit) u;
		    selected.add(unit);
		    unit.select();
		}
	    }
	}
	selected.sortByPosition();
    }

    /**
     * Clears the current selection, unselecting all units
     */
    public void clearSelection() {
	if (selected != null) {
	    for (Unit u : selected) {
		u.unselect();
	    }
	    selected.clear();
	}
    }

    /**
     * Selects the unit colliding with the given coordinates
     * 
     * @param x
     *            The x in canvas coordinates
     * @param y
     *            The y in canvas coordinates
     */
    public void selectOrMove(float x, float y) {
	PositionPhysicalEntity ppeColliding = game.getCollidingUnitImage((int) x, (int) y);
	if (ppeColliding != null && ppeColliding instanceof Unit) {
	    Unit colliding = (Unit) ppeColliding;
	    // Remove current selection
	    clearSelection();

	    colliding.toggleSelection();

	    selected = UnitGroupManager.getInstance().getSelectionUnitGroup();

	    selected.add(colliding);
	    selected.sortByPosition();
	} else {
	    if (selected != null && !selected.isEmpty()) {
		game.moveUnits(selected, (int) x, (int) y);
	    }
	}
    }

    /**
     * Gets the rectangle bounds of the current selection
     * 
     * @return
     */
    private Rectangle getRectangle() {
	float x1, x2, y1, y2;

	if (start.x < end.x) {
	    x1 = start.x;
	    x2 = end.x;
	} else {
	    x1 = end.x;
	    x2 = start.x;
	}
	if (start.y < end.y) {
	    y1 = start.y;
	    y2 = end.y;
	} else {
	    y1 = end.y;
	    y2 = start.y;
	}

	sel.set(x1, y1, x2 - x1, y2 - y1);
	return sel;
    }
}
