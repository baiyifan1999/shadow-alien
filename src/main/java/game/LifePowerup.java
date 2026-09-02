package game;

import bagel.util.Point;

/**
 * Healing item: Immediately grants the player +1 life points
 * Cannot exceed the player's initial maximum life points
 * Has no duration, and the effect takes effect immediately
 */
public class LifePowerup extends Powerup {

    public LifePowerup(String imagePath, Point pos, double movementSpeed) {
        // The duration is 0, as this item has no duration.
        super(imagePath, pos, movementSpeed, 0);
    }

    @Override
    protected void applyEffect(PlayerShip player) {
        player.addLife();
    }
}
