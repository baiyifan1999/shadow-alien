package game;

import bagel.util.Point;

/**
 * Explosion effect:
 * Disappears after displaying several frames at a fixed position
 * It is divided into two types: large (when the enemy is destroyed)
 * and small (when the player is hit)
 */
public class Explosion extends Entity {

    private final int maxFrames;
    private double currentFrame = 0;
    private final boolean isLarge;

    public Explosion(
            String imagePath,
            Point pos,
            int maxFrames,
            boolean isLarge
    ) {
        super(imagePath, pos);
        this.maxFrames = maxFrames;
        this.isLarge = isLarge;
    }

    @Override
    public void update(double timescale) {
        currentFrame += timescale;
    }

    public boolean isFinished() {
        return currentFrame >= maxFrames;
    }
}
