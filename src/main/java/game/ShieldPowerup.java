package game;

import bagel.util.Point;

/**
 * Shield item: Once activated,
 * the player becomes invincible for the duration of the frame.
 * During the invincible period, no health or score is deducted,
 * but other collision effects (such as explosions) occur normally.
 */
public class ShieldPowerup extends Powerup {

    public ShieldPowerup(
            String imagePath,
            Point pos,
            double movementSpeed,
            int duration
    ) {
        super(imagePath, pos, movementSpeed, duration);
    }

    /**
     * Pass the shield effect handling to PlayerShip
     */
    @Override
    protected void applyEffect(PlayerShip player) {
        player.applyShield(duration);
    }
}
