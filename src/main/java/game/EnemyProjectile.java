package game;

import bagel.util.Point;

/**
 * The bullets fired by the enemy:
 * Move downward (opposite to the direction of the player's bullets)
 * Inherited from Projectile,
 * overrides the methods update() and isOffScreen()
 */
public class EnemyProjectile extends Projectile {

    private final double speed;

    public EnemyProjectile(String imagePath, Point pos, double speed) {
        super(imagePath, pos, speed);
        this.speed = speed;
    }

    /**
     * Move downward: y increases
     */
    @Override
    public void update(double timescale) {
        double newY = position.y + speed * timescale;
        this.position = new Point(position.x, newY);
    }

    /**
     * When the bullet flies out below the screen, return true
     */
    @Override
    public boolean isOffScreen() {

        return position.y - image.getHeight() / 2.0 >
                ShadowAliens.getScreenHeight();
    }
}
