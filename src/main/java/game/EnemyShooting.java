package game;

import bagel.DrawOptions;
import bagel.util.Point;

/**
 * Enemy capable of shooting: Move downward
 * and fire bullets at a fixed frequency
 */
public class EnemyShooting extends Enemy {

    private static final double ROTATION = Math.PI;

    // How many frames should the shooting occur every time
    private final int firingRate;
    // The number of frames that have passed since its appearance
    private double framesSinceArrival;

    private final String projectileImage;
    private final double projectileSpeed;

    public EnemyShooting(
            String imagePath,
            Point pos,
            double speed,
            int arrivalTime,
            int firingRate,
            String projectileImage,
            double projectileSpeed) {
        super(imagePath, pos, speed, arrivalTime, new StraightDownMovement());
        this.firingRate = firingRate;
        this.framesSinceArrival = 0;
        this.projectileImage = projectileImage;
        this.projectileSpeed = projectileSpeed;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        framesSinceArrival += timescale;
    }

    @Override
    public void render() {

        image.draw(
                position.x,
                position.y,
                new DrawOptions().setRotation(ROTATION)
        );
    }

    /**
     * Determine whether to shoot.
     * If so, create and return a bullet; otherwise, return null.
     * Call in the updateEntities() function of ShadowAliens
     */
    public EnemyProjectile shoot() {
        if (framesSinceArrival >= firingRate) {
            framesSinceArrival = 0;
            return new EnemyProjectile(
                    projectileImage,
                    position,
                    projectileSpeed
            );
        }
        return null;
    }
}