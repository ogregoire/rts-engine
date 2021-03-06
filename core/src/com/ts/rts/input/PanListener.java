package com.ts.rts.input;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.ts.rts.RTSGame;
import com.ts.rts.datastructure.IMapCell;
import com.ts.rts.datastructure.geom.Vector2;
import com.ts.rts.scene.cam.Camera;
import com.ts.rts.scene.selection.Selection;
import com.ts.rts.scene.unit.IBoundsObject;
import com.ts.rts.util.VectorPool;

/**
 * Pan listener which manages camera pan mouse moves.
 * 
 * @author Toni Sagrista
 * 
 */
public class PanListener extends InputAdapter {
    /**
     * Pan zone padding
     */
    private static final int PADDING = 20;

    private Camera camera;
    private Selection selection;
    private Rectangle activeZone;
    private Vector2 canvasCenter;

    // Definition of movement keys
    private static final int KEY_UP = Keys.W;
    private static final int KEY_DOWN = Keys.S;
    private static final int KEY_LEFT = Keys.A;
    private static final int KEY_RIGHT = Keys.D;

    // Mouse pixmaps
    Pixmap m_normal, m_up, m_down, m_left, m_right, m_forbidden;

    private static final float KEYBOARD_MOVEMENT_MULTIPLIER = 500f;

    private static Set<Integer> movementKeys;
    static {
        movementKeys = new HashSet<Integer>();
        movementKeys.add(KEY_UP);
        movementKeys.add(KEY_DOWN);
        movementKeys.add(KEY_LEFT);
        movementKeys.add(KEY_RIGHT);
    }

    public PanListener(Camera camera, Selection selection) {
        super();
        m_normal = new Pixmap(Gdx.files.internal("data/img/cursor.png"));
        m_up = new Pixmap(Gdx.files.internal("data/img/up-cursor.png"));
        m_down = new Pixmap(Gdx.files.internal("data/img/down-cursor.png"));
        m_left = new Pixmap(Gdx.files.internal("data/img/left-cursor.png"));
        m_right = new Pixmap(Gdx.files.internal("data/img/right-cursor.png"));
        m_forbidden = new Pixmap(Gdx.files.internal("data/img/forbidden-cursor.png"));

        // Normal image
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_normal, 0, 0));

        this.camera = camera;
        this.selection = selection;
        // Zone of the screen where the mouse can work without panning
        this.activeZone = new Rectangle(PADDING, PADDING, camera.canvasWidth - PADDING * 2, camera.canvasHeight - PADDING * 2);
        this.canvasCenter = VectorPool.getObject(camera.canvasWidth / 2f, camera.canvasHeight / 2f);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenY = Gdx.graphics.getHeight() - screenY;

        if (!selection.active && !activeZone.contains(screenX, screenY)) {
            Vector2 movement = VectorPool.getObject(screenX, screenY);
            movement.subtract(canvasCenter);
            camera.setAccel(movement);
            float angle = movement.angle();
            VectorPool.returnObject(movement);

            if (angle >= 315 || angle < 45) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_down, 0, 0));
            } else if (angle >= 45 && angle < 135) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_right, 0, 0));
            } else if (angle >= 135 && angle < 225) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_up, 0, 0));
            } else if (angle >= 225 && angle < 315) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_left, 0, 0));
            }
        } else {
            camera.stop();
            IMapCell<IBoundsObject> cell = RTSGame.game.getMap().getCell(screenX + camera.getCameraDisplacementX(), screenY + camera.getCameraDisplacementY());
            if (cell.isBlocked()) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_forbidden, 0, 0));
            } else {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(m_normal, 0, 0));
            }

        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (movementKeys.contains(Integer.valueOf(keycode))) {
            switch (keycode) {
            case KEY_UP:
                camera.up(KEYBOARD_MOVEMENT_MULTIPLIER);
                break;
            case KEY_DOWN:
                camera.down(KEYBOARD_MOVEMENT_MULTIPLIER);
                break;
            case KEY_LEFT:
                camera.left(KEYBOARD_MOVEMENT_MULTIPLIER);
                break;
            case KEY_RIGHT:
                camera.right(KEYBOARD_MOVEMENT_MULTIPLIER);
                break;
            }
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (movementKeys.contains(Integer.valueOf(keycode))) {
            switch (keycode) {
            case KEY_UP:
                camera.stopVertical();
                break;
            case KEY_DOWN:
                camera.stopVertical();
                break;
            case KEY_LEFT:
                camera.stopHorizontal();
                break;
            case KEY_RIGHT:
                camera.stopHorizontal();
                break;
            }
            return true;
        }
        return super.keyUp(keycode);
    }

}
