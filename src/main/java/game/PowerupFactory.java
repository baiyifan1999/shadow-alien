package game;

import bagel.util.Point;

/**
 * Factory Method: Responsible for creating
 * corresponding subclasses of Powerup based on the type.
 */
public class PowerupFactory {

    private final String shieldImg;
    private final String lifeImg;
    private final String cooldownImg;
    private final String engineImg;

    private final double shieldSpeed;
    private final double lifeSpeed;
    private final double cooldownSpeed;
    private final double engineSpeed;

    private final int shieldDuration;
    private final int cooldownDuration;
    private final int engineDuration;

    public PowerupFactory(String shieldImg,
                          double shieldSpeed,
                          int shieldDuration,
                          String lifeImg,
                          double lifeSpeed,
                          String cooldownImg,
                          double cooldownSpeed,
                          int cooldownDuration,
                          String engineImg,
                          double engineSpeed,
                          int engineDuration
    ) {
        this.shieldImg       = shieldImg;
        this.lifeImg         = lifeImg;
        this.cooldownImg     = cooldownImg;
        this.engineImg       = engineImg;
        this.shieldSpeed     = shieldSpeed;
        this.lifeSpeed       = lifeSpeed;
        this.cooldownSpeed   = cooldownSpeed;
        this.engineSpeed     = engineSpeed;
        this.shieldDuration  = shieldDuration;
        this.cooldownDuration = cooldownDuration;
        this.engineDuration  = engineDuration;
    }

    /**
     * Factory method: Determine which type of item to
     * create based on the spawn information.
     */
    public Powerup create(PowerupSpawn spawn) {
        double startY = -50;
        Point pos = new Point(spawn.getPosX(), startY);

        switch (spawn.getType()) {
            case "shield":
                return new ShieldPowerup(
                        shieldImg,
                        pos,
                        shieldSpeed,
                        shieldDuration
                );
            case "life":
                return new LifePowerup(lifeImg, pos, lifeSpeed);
            case "cooldown":
                return new CooldownPowerup(
                        cooldownImg,
                        pos,
                        cooldownSpeed,
                        cooldownDuration
                );
            default: // "engine"
                return new EnginePowerup(
                        engineImg,
                        pos,
                        engineSpeed,
                        engineDuration
                );
        }
    }
}
