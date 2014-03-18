package com.ts.rts.scene.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ts.rts.RTSGame;
import com.ts.rts.datastructure.geom.Vector2;

/**
 * Represents an entity which is physical and has a position.
 * 
 * @author Toni Sagrista
 * 
 */
public abstract class PositionPhysicalEntity extends PositionEntity implements IBoundsObject,
	Comparable<PositionPhysicalEntity> {

    /**
     * The default sprite
     */
    public TextureRegion sprite;
    protected ShapeRenderer shapeRenderer;

    /** Mass [kg] **/
    public float mass;

    /**
     * SIZE
     */
    public float width;
    public float height;

    /** Health points **/
    protected float maxHp;
    public float hp;

    /** Bounds for selection - these match the bounds of the sprite **/
    public Rectangle imageBounds;

    /**
     * Entity bounds for collisions
     */
    public Rectangle hardRadius;

    /**
     * The soft radius for flocking/steering
     */
    public Circle softRadius;

    /** Heading **/
    public Vector2 heading;

    /** The sprite scale, 1 by default **/
    protected float scale = 1f;

    /** Width and height of shadow **/
    protected float shadowWidth = 0f, shadowHeight = 0f;

    /**
     * Sprite offsets from the center, positive down and left
     */
    protected float spriteOffsetX = 0f, spriteOffsetY = 0f;

    public PositionPhysicalEntity() {
	super();
    }

    public PositionPhysicalEntity(float x, float y) {
	super(x, y);
    }

    public PositionPhysicalEntity(Vector2 pos) {
	super(pos);
    }

    public abstract void initGraphics();

    /**
     * Initializes the hard radius and the image bounds. The position of the entity must be set, as well as
     * its image initialized.
     * @param height The height of the hard radius, usually the height of the image
     */
    protected void initHardRadius(float height) {
	float w2 = this.width / 2f;
	float h2 = this.height / 2f;
	hardRadius = new Rectangle(pos.x - w2, pos.y - h2 + spriteOffsetY, this.width, height);
	imageBounds = new Rectangle(pos.x - w2 + spriteOffsetX, pos.y - h2 + spriteOffsetY, this.width, this.height);
    }

    @Override
    public Rectangle bounds() {
	return hardRadius;
    }

    @Override
    public Rectangle getBounds() {
	return hardRadius;
    }

    @Override
    public Circle softRadius() {
	return softRadius;
    }

    @Override
    public Vector2 pos() {
	return pos;
    }

    public boolean isDead() {
	return hp <= 0f;
    }

    /**
     * Returns true if the current entity collides with the given point, false otherwise
     * 
     * @param x
     * @param y
     * @return
     */
    public boolean isColliding(float posx, float posy) {
	return hardRadius.contains(posx, posy);
    }

    /**
     * Checks if the given point is in the soft radius of this unit
     * 
     * @param posx
     * @param posy
     * @return
     */
    public boolean isInSoftRadius(float posx, float posy) {
	return softRadius.contains(posx, posy);
    }

    /**
     * Checks collision with other entity
     * 
     * @param other
     * @return
     */
    public boolean isColliding(PositionPhysicalEntity other) {
	return hardRadius.overlaps(other.hardRadius);
    }

    public boolean isImageColliding(float posx, float posy) {
	return imageBounds.contains(posx, posy);
    }

    /**
     * Returns the image to draw at each frame
     * 
     * @return
     */
    protected TextureRegion getImageToDraw() {
	return sprite;
    }

    public void update(float deltaSecs) {

    }

    /**
     * Renders the sprite
     */
    public void render() {
	RTSGame.getSpriteBatch().draw(sprite, pos.x - sprite.getRegionWidth() / 2 + spriteOffsetX,
		pos.y - sprite.getRegionHeight() / 2 + spriteOffsetY,
		sprite.getRegionWidth() / 2 + spriteOffsetX, sprite.getRegionHeight() / 2,
		sprite.getRegionWidth(), sprite.getRegionHeight(), scale, scale, 0f);
    }

    public abstract void renderSelection();

    @Override
    public int compareTo(PositionPhysicalEntity o) {
	return Float.compare(pos.y, o.pos.y) / -1;
    }

    public void renderDebug() {
	Gdx.gl.glEnable(GL20.GL_BLEND);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	shapeRenderer.begin(ShapeType.Filled);

	// Hard radius
	shapeRenderer.setColor(new Color(0f, 0f, .4f, .4f));
	shapeRenderer.rect(hardRadius.x, hardRadius.y, hardRadius.width, hardRadius.height);

	// Soft radius
	shapeRenderer.setColor(new Color(0f, .4f, 0f, .2f));
	shapeRenderer.circle(softRadius.x, softRadius.y, softRadius.radius);

	// Position
	shapeRenderer.setColor(new Color(0f, 1f, 0f, 1f));
	shapeRenderer.circle(pos.x, pos.y, 1);
	shapeRenderer.end();

	shapeRenderer.begin(ShapeType.Line);
	// Image bounds
	shapeRenderer.setColor(new Color(0f, 0f, 0f, 1f));
	shapeRenderer.rect(imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height);

	shapeRenderer.end();
	Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Default implementation is just a box as big as the bounding box with an offset
     */
    public void renderShadow() {
	if (shadowWidth != 0f && shadowHeight != 0f) {
	    // Render shadow
	    Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.setColor(new Color(0f, 0f, 0f, .5f));
	    shapeRenderer.ellipse(pos.x - shadowWidth / 2 + 2, pos.y - shadowHeight / 2 - 2, shadowWidth, shadowHeight);
	    shapeRenderer.end();
	    Gdx.gl.glDisable(GL20.GL_BLEND);
	}
    }

}
