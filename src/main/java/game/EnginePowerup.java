package game;

import bagel.util.Point;

/**
 * Engine effect: Increases
 * the player's movement speed to twice its original speed
 * Restores to the original speed after a duration of frames
 * No visual effect
 */
public class EnginePowerup extends Powerup {

    public EnginePowerup(
            String imagePath,
            Point pos,
            double movementSpeed,
            int duration
    ) {
        super(imagePath, pos, movementSpeed, duration);
    }

    @Override
    protected void applyEffect(PlayerShip player) {
        player.applyEngine(duration);
    }
}
