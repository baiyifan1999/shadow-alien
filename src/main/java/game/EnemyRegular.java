package game;

import bagel.DrawOptions;
import bagel.util.Point;

/**
 * Ordinary enemy: Moves only downward along the y-axis
 * Behavior is the same as that of the enemy in Project 1
 */
public class EnemyRegular extends Enemy {

    private static final double ROTATION = Math.PI;
    // Rotate 180 degrees to make the picture face downwards.

    public EnemyRegular(
            String imagePath,
            Point pos,
            double speed,
            int arrivalTime
    ) {
        super(
                imagePath,
                pos,
                speed,
                arrivalTime,
                new StraightDownMovement()
        );
    }

    @Override
    public void render() {
        image.draw(
                position.x,
                position.y,
                new DrawOptions().setRotation(ROTATION)
        );
    }
}
