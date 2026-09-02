package game;

/**
 * Record the birth information of individual props,
 * reading from gameData.properties
 * Similar to EnemySpawn, but the speed of the items
 * does not need to be separately recorded.
 */
public class PowerupSpawn {
    private final int arrivalTime;
    private final double posX;
    private final String type; // "shield", "life", "cooldown", "engine"

    public PowerupSpawn(int arrivalTime, double posX, String type) {
        this.arrivalTime = arrivalTime;
        this.posX = posX;
        this.type = type;
    }

    public int getArrivalTime() { return arrivalTime; }
    public double getPosX()     { return posX; }
    public String getType()     { return type; }
}
