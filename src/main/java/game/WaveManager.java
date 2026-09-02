package game;

import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Manage the spawn logic for all waves of enemies and items
 * Responsibility: Reading configuration, spawning at specific times,
 * detecting wave completion, and advancing to the next wave
 */
public class WaveManager {

    private final Properties props;

    private final EnemyFactory  enemyFactory;
    private final PowerupFactory powerupFactory;

    // Current wave number (starting from 1)
    private int currentWave;
    private int totalWaves;

    // The spawn schedule for the current wave
    private ArrayList<EnemySpawn> enemySchedule = new ArrayList<>();
    private ArrayList<PowerupSpawn> powerupSchedule = new ArrayList<>();

    // The number of frames that have been processed in the current batch
    private double waveFrameCount = 0;

    public WaveManager() {
        Properties props = GameConfig.getInstance().getProperties();
        this.props = props;

        this.enemyFactory = new EnemyFactory(
                props.getProperty("enemy.regular.image"),
                props.getProperty("enemy.strafing.image"),
                props.getProperty("enemy.shooting.image"),
                props.getProperty("enemyProjectile.image"),
                Double.parseDouble(
                        props.getProperty("enemyProjectile.movementSpeed")
                ),
                Integer.parseInt(
                        props.getProperty("enemy.shooting.firingRate")
                )
        );

        this.powerupFactory = new PowerupFactory(
                props.getProperty("powerup.shield.image"),
                Double.parseDouble(
                        props.getProperty("powerup.shield.movementSpeed")
                ),
                Integer.parseInt(props.getProperty("powerup.shield.duration")),
                props.getProperty("powerup.life.image"),
                Double.parseDouble(
                        props.getProperty("powerup.life.movementSpeed")
                ),
                props.getProperty("powerup.cooldown.image"),
                Double.parseDouble(
                        props.getProperty("powerup.cooldown.movementSpeed")
                ),
                Integer.parseInt(
                        props.getProperty("powerup.cooldown.duration")
                ),
                props.getProperty("powerup.engine.image"),
                Double.parseDouble(
                        props.getProperty("powerup.engine.movementSpeed")
                ),
                Integer.parseInt(props.getProperty("powerup.engine.duration"))
        );

        // Calculate the total number of waves
        this.totalWaves = countTotalWaves();

        // Starting from the first wave
        this.currentWave = 1;
        loadWave(currentWave);
    }

    // ----------------------------------------------------------------
    // Frame update:
    // Check if any enemies or items have reached the time limit.
    // ----------------------------------------------------------------

    /**
     * For each frame,
     * return the enemies and items that need to be added for this frame.
     */
    public void update(double timescale,
                       ArrayList<Enemy> enemies,
                       ArrayList<Powerup> powerups) {

        waveFrameCount += timescale;

        // check enemy spawn
        for (int i = enemySchedule.size() - 1; i >= 0; i--) {
            EnemySpawn spawn = enemySchedule.get(i);
            if (waveFrameCount >= spawn.getArrivalTime()) {
                enemies.add(enemyFactory.create(spawn));
                enemySchedule.remove(i);
            }
        }

        // check powerup spawn
        for (int i = powerupSchedule.size() - 1; i >= 0; i--) {
            PowerupSpawn spawn = powerupSchedule.get(i);
            if (waveFrameCount >= spawn.getArrivalTime()) {
                powerups.add(powerupFactory.create(spawn));
                powerupSchedule.remove(i);
            }
        }
    }

    // ----------------------------------------------------------------
    // The test has been completed.
    // ----------------------------------------------------------------

    /**
     * All the spawns for this current wave have been sent out,
     * and there are no enemies,
     * enemy bullets, or props on the field anymore.
     */
    public boolean isWaveComplete(ArrayList<Enemy> enemies,
                                  ArrayList<EnemyProjectile> enemyProjectiles,
                                  ArrayList<Powerup> powerups) {
        return enemySchedule.isEmpty()
                && powerupSchedule.isEmpty()
                && enemies.isEmpty()
                && enemyProjectiles.isEmpty()
                && powerups.isEmpty();
    }

    /**
     * Move on to the next wave
     */
    public void nextWave() {
        currentWave++;
        waveFrameCount = 0;
        loadWave(currentWave);
    }

    /**
     * All rounds have been fired.
     */
    public boolean isAllWavesComplete() {
        return currentWave > totalWaves;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    // ----------------------------------------------------------------
    // reset
    // ----------------------------------------------------------------

    public void reset() {
        currentWave = 1;
        waveFrameCount = 0;
        enemySchedule.clear();
        powerupSchedule.clear();
        loadWave(currentWave);
    }

    // ----------------------------------------------------------------
    // Private method: Reading configuration
    // ----------------------------------------------------------------

    private int countTotalWaves() {
        int count = 0;
        while (props.getProperty(
                "wave." +
                (count + 1) +
                ".enemy.0.arrivalTime"
        ) != null) {
            count++;
        }
        return count;
    }

    private void loadWave(int waveNumber) {
        enemySchedule.clear();
        powerupSchedule.clear();

        // read enemy
        int i = 0;
        while (props.getProperty(
                "wave." + waveNumber + ".enemy." + i + ".arrivalTime"
        ) != null) {
            String prefix = "wave." + waveNumber + ".enemy." + i + ".";
            int time    = Integer.parseInt(
                    props.getProperty(prefix + "arrivalTime")
            );
            double posX = Double.parseDouble(
                    props.getProperty(prefix + "posX")
            );
            double speed = Double.parseDouble(
                    props.getProperty(prefix + "movementSpeed")
            );
            String type = props.getProperty(prefix + "type");
            enemySchedule.add(new EnemySpawn(time, posX, speed, type));
            i++;
        }

        // read powerup
        int j = 0;
        while (props.getProperty(
                "wave." + waveNumber + ".powerup." + j + ".arrivalTime"
        ) != null) {
            String prefix = "wave." + waveNumber + ".powerup." + j + ".";
            int time    = Integer.parseInt(
                    props.getProperty(prefix + "arrivalTime")
            );
            double posX = Double.parseDouble(
                    props.getProperty(prefix + "posX")
            );
            String type = props.getProperty(prefix + "type");
            powerupSchedule.add(new PowerupSpawn(time, posX, type));
            j++;
        }
    }
}
