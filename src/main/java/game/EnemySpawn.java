package game;

/**
 * Record the birth information of a single enemy,
 * and read it from gameData.properties
 */
public class EnemySpawn {
    private final int arrivalTime;
    private final double posX;
    private final double movementSpeed;
    private final String type;  // "regular", "strafing", "shooting"

    public EnemySpawn(
            int arrivalTime,
            double posX,
            double movementSpeed,
            String type
    ) {
        this.arrivalTime = arrivalTime;
        this.posX = posX;
        this.movementSpeed = movementSpeed;
        this.type = type;
    }

    public int getArrivalTime()      { return arrivalTime; }
    public double getPosX()          { return posX; }
    public double getMovementSpeed() { return movementSpeed; }
    public String getType()          { return type; }
}
