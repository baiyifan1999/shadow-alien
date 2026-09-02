package game;

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PlayerShip extends Entity {

    private final double baseSpeed;
    private final int initialLives;
    private int currentLives;

    private final Point initialPos;
    private Input currentInput;

    // invisible
    private int invincibleTimer = 0;
    private boolean isShielded = false;

    // shield image
    private final Image shieldImage;

    // Engine powerup
    private double speedMultiplier = 1.0;
    private int speedTimer = 0;

    // Cooldown powerup
    private final int baseShootCooldown; // 原始冷却帧数
    private double cooldownMultiplier = 1.0;
    private int cooldownTimer = 0;

    public PlayerShip(String imagePath, Point pos, double speed, int initialLives,
                      int baseShootCooldown, String shieldImagePath) {
        super(imagePath, pos);
        this.baseSpeed = speed;
        this.currentLives = initialLives;
        this.initialPos = pos;
        this.initialLives = initialLives;
        this.baseShootCooldown = baseShootCooldown;
        this.shieldImage = new Image(shieldImagePath);
    }

    // ----------------------------------------------------------------
    // update by frame
    // ----------------------------------------------------------------

    public void setInput(Input input) {
        this.currentInput = input;
    }

    @Override
    public void update(double timescale) {
        // timer of invisible hit
        if (invincibleTimer > 0) {
            invincibleTimer -= timescale;
            if (invincibleTimer <= 0) {
                isShielded = false;
            }
        }

        // timer of speedMultiplier
        if (speedTimer > 0) {
            speedTimer -= timescale;
            if (speedTimer <= 0) {
                speedMultiplier = 1.0; // baseSpeed
            }
        }

        // timer of cooldown
        if (cooldownTimer > 0) {
            cooldownTimer -= timescale;
            if (cooldownTimer <= 0) {
                cooldownMultiplier = 1.0; // baseCooldown
            }
        }

        // move
        if (currentInput == null) return;

        double halfWidth = image.getWidth() / 2.0;
        double currentSpeed = baseSpeed * speedMultiplier;
        double moveDistance = currentSpeed * timescale;
        double newX = position.x;
        double screenWidth = ShadowAliens.getScreenWidth();

        if (currentInput.isDown(Keys.A)) {
            if (newX - halfWidth - moveDistance >= 0) {
                newX -= moveDistance;
            } else {
                newX = halfWidth;
            }
        }

        if (currentInput.isDown(Keys.D)) {
            if (newX + halfWidth + moveDistance <= screenWidth) {
                newX += moveDistance;
            } else {
                newX = screenWidth - halfWidth;
            }
        }

        this.position = new Point(newX, position.y);
    }

    // ----------------------------------------------------------------
    // render: add shield picture when invisible
    // ----------------------------------------------------------------

    @Override
    public void render() {
        image.draw(position.x, position.y);
        if (isInvincible()) {
            shieldImage.draw(position.x, position.y);
        }
    }

    // ----------------------------------------------------------------
    // lives
    // ----------------------------------------------------------------

    public int getLives() {
        return currentLives;
    }

    public void decreaseLife() {
        currentLives--;
    }

    public void addLife() {
        if (currentLives < initialLives) {
            currentLives++;
        }
    }

    // ----------------------------------------------------------------
    // invisible
    // ----------------------------------------------------------------

    public boolean isInvincible() {
        return invincibleTimer > 0 || isShielded;
    }

    // short invisible
    public void applyHitInvincibility(int duration) {
        this.invincibleTimer = duration;
    }

    // shield
    public void applyShield(int duration) {
        this.isShielded = true;
        this.invincibleTimer = duration;
    }

    // ----------------------------------------------------------------
    // Powerup
    // ----------------------------------------------------------------

    public void applyEngine(int duration) {
        this.speedMultiplier = 2.0;
        this.speedTimer = duration;
    }

    public void applyCooldown(int duration) {
        this.cooldownMultiplier = 1.0 / 3.0;
        this.cooldownTimer = duration;
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public int getShootCooldown() {
        int adjusted = (int) Math.floor(
                baseShootCooldown * cooldownMultiplier
        );
        return Math.max(1, adjusted); // at least 1 frame
    }

    public double getSpeed() {
        return baseSpeed * speedMultiplier;
    }

    // ----------------------------------------------------------------
    // reset
    // ----------------------------------------------------------------

    public void resetToInitialState() {
        this.position = this.initialPos;
        this.currentLives = this.initialLives;
        this.invincibleTimer = 0;
        this.isShielded = false;
        this.speedMultiplier = 1.0;
        this.speedTimer = 0;
        this.cooldownMultiplier = 1.0;
        this.cooldownTimer = 0;
    }
}