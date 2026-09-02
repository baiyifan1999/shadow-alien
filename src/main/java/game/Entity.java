package game;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/** Base class for all game objects with a position and image. */
public abstract class Entity {
    protected final Image image;
    protected Point position;

    // constructor
    public Entity(String imagePath, Point pos) {
        this.image = new Image(imagePath);
        this.position = pos;
    }

    // getters
    public Point getPosition() {
        return new Point(position.x, position.y);
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    // render
    public void render() {
        image.draw(position.x, position.y);
    }

    // update
    // Default implementation does nothing; subclasses override as needed
    public void update(double timescale){

    };
}
