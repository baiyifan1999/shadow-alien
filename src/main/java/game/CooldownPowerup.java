package game;

import bagel.util.Point;

/**
 * Cooling item: Shortens the player's
 * shooting cooldown time to 1/3 of its original duration
 * Restores the original cooldown time after a certain number of frames
 * No visual effect
 */
public class CooldownPowerup extends Powerup {

    public CooldownPowerup(
            String imagePath,
            Point pos,
            double movementSpeed,
            int duration
    ) {
        super(imagePath, pos, movementSpeed, duration);
    }

    @Override
    protected void applyEffect(PlayerShip player) {
        player.applyCooldown(duration);
    }
}
