package game;

import bagel.util.Point;

/**
 * Moves diagonally, bouncing off left and right screen edges.
 */
public class StrafingMovement implements MovementStrategy {

    private int directionX = 1; // 1 = right, -1 = left

    @Override
    public Point move(
            Point currentPos,
            double speed,
            double timescale,
            double entityWidth
    ) {
        double newX = currentPos.x + speed * directionX * timescale;
        double newY = currentPos.y + speed * timescale;

        double halfWidth = entityWidth / 2.0;
        double screenWidth = ShadowAliens.getScreenWidth();

        if (newX - halfWidth <= 0 || newX + halfWidth >= screenWidth) {
            directionX *= -1;
            newX = currentPos.x + speed * directionX * timescale;
        }

        return new Point(newX, newY);
    }
}
