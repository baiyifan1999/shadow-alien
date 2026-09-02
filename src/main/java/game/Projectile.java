package game;

import bagel.util.Point;

// Moves upward each frame; destroyed on enemy collision or leaving screen
public class Projectile extends Entity {

    private double speed;

    // constructor
    public Projectile (String imagePath, Point pos, double speed) {
        super(imagePath, pos);
        this.speed = speed;
    }

    // getter
    // getY
    public double getY() {
        return position.y;
    }

    @Override
    public void update(double timescale) {
        double newY = position.y - speed * timescale;
        this.position = new Point(position.x, newY);
    }

    // Returns true when projectile has fully moved above the screen
    public boolean isOffScreen() {
        return position.y + image.getHeight() / 2 < 0;
    }
}
