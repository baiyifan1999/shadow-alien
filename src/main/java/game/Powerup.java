package game;

import bagel.util.Point;

/**
 * The abstract base class for all props
 * Subclasses: ShieldPowerup, LifePowerup, CooldownPowerup, EnginePowerup
 */
public abstract class Powerup extends Entity {

    protected final int duration;
    private final double movementSpeed;

    public Powerup(
            String imagePath,
            Point pos,
            double movementSpeed,
            int duration
    ) {
        super(imagePath, pos);
        this.movementSpeed = movementSpeed;
        this.duration = duration;
    }

    /**
     * Move downward in each frame
     */
    @Override
    public void update(double timescale) {
        double newY = position.y + movementSpeed * timescale;
        this.position = new Point(position.x, newY);
    }

    /**
     * When the prop flies out from the bottom of the screen,
     * it returns true, and then it will be destroyed.
     */
    public boolean isOffScreen(double screenHeight) {
        return position.y - image.getHeight() / 2.0 > screenHeight;
    }

    /**
     * Template method: Defines the fixed process for activating the props
     * Subclasses no longer override this method,
     * only override the applyEffect() method
     */
    public final void activate(PlayerShip player) {
        // Apply the effect
        // (each item has a different effect, to be implemented by subclasses)
        applyEffect(player);
    }

    /**
     * Abstract method:
     * In the subclass, fill in What specific effect is applied?
     */
    protected abstract void applyEffect(PlayerShip player);
}
