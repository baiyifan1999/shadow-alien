package game;

import bagel.util.Point;

/**
 * Moves straight down each frame.
 */
public class StraightDownMovement implements MovementStrategy {

    @Override
    public Point move(
            Point currentPos,
            double speed,
            double timescale,
            double entityWidth
    ) {
        return new Point(currentPos.x, currentPos.y + speed * timescale);
    }
}
