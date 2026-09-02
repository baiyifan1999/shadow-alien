package game;

import bagel.util.Point;

/**
 * Factory Method pattern: Responsible for creating corresponding
 * Enemy subclasses based on the type
 * The WaveManager no longer needs to know
 * how each type of enemy is constructed.
 */
public class EnemyFactory {

    private final String regularImg;
    private final String strafingImg;
    private final String shootingImg;

    private final String enemyProjectileImg;
    private final double enemyProjectileSpeed;
    private final int    firingRate;

    public EnemyFactory(
            String regularImg,
            String strafingImg,
            String shootingImg,
            String enemyProjectileImg,
            double enemyProjectileSpeed,
            int firingRate
    ) {
        this.regularImg           = regularImg;
        this.strafingImg          = strafingImg;
        this.shootingImg          = shootingImg;
        this.enemyProjectileImg   = enemyProjectileImg;
        this.enemyProjectileSpeed = enemyProjectileSpeed;
        this.firingRate           = firingRate;
    }

    /**
     * Factory Method: Determine which type of enemy
     * to create based on the spawn information.
     */
    public Enemy create(EnemySpawn spawn) {
        double startY = -50;
        Point pos = new Point(spawn.getPosX(), startY);

        switch (spawn.getType()) {
            case "strafing":
                return new EnemyStrafing(
                        strafingImg,
                        pos,
                        spawn.getMovementSpeed(),
                        spawn.getArrivalTime());
            case "shooting":
                return new EnemyShooting(
                        shootingImg,
                        pos,
                        spawn.getMovementSpeed(),
                        spawn.getArrivalTime(),
                        firingRate,
                        enemyProjectileImg,
                        enemyProjectileSpeed
                );
            default:
                return new EnemyRegular(
                        regularImg,
                        pos,
                        spawn.getMovementSpeed(),
                        spawn.getArrivalTime()
                );
        }
    }
}