package game;

import bagel.DrawOptions;
import bagel.util.Point;

/**
 * Horizontal movement of enemies:
 * Move along both the x-axis and y-axis simultaneously.
 * Reverse direction when hitting the left
 * or right edge of the screen.
 */
public class EnemyStrafing extends Enemy {

    private static final double ROTATION = Math.PI;

    public EnemyStrafing(
            String imagePath,
            Point pos,
            double speed,
            int arrivalTime) {
        super(imagePath, pos, speed, arrivalTime, new StrafingMovement());
    }
// Remove the "update()" function and the "directionX" field

    @Override
    public void render() {
        image.draw(
                position.x,
                position.y,
                new DrawOptions().setRotation(ROTATION));
    }
}
