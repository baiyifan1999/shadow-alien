package game;

import bagel.util.Point;

/**
 * Strategy pattern: defines how an enemy moves each frame.
 */
public interface MovementStrategy {

    /**
     * Calculate the next position based on current state.
     *
     * @param currentPos  current position of the entity
     * @param speed       movement speed
     * @param timescale   current timescale
     * @param entityWidth width of the entity image (used for boundary checks)
     * @return            new position after moving
     */
    Point move(
            Point currentPos,
            double speed,
            double timescale,
            double entityWidth
    );
}