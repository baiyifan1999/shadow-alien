package game;

import bagel.*;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

public class ShadowAliens extends AbstractGame implements ScoreObserver {

    private static double screenWidth;
    private static double screenHeight;

    public static double getScreenWidth()  { return screenWidth; }
    public static double getScreenHeight() { return screenHeight; }

    private GameState gameState = GameState.START;
    private boolean isWin = false;

    private PlayerShip player;

    // Entity list
    private ArrayList<Enemy>            enemies          = new ArrayList<>();
    private ArrayList<Projectile>       projectiles      = new ArrayList<>();
    private ArrayList<EnemyProjectile>  enemyProjectiles = new ArrayList<>();
    private ArrayList<Explosion>        explosions       = new ArrayList<>();
    private ArrayList<Powerup>          powerups         = new ArrayList<>();

    // shooting cooldown
    private double frameSinceLastShot = 0;

    // Explosion images and duration(large/small)
    private final String smallExplosionImg;
    private final String largeExplosionImg;
    private final int    smallExplosionDuration;
    private final int    largeExplosionDuration;

    // UI display
    private bagel.util.Colour backgroundColour;
    private bagel.util.Colour textColour;
    private Image             heartImage;
    private double            heartStartX;
    private double            heartY;
    private double            heartGap;

    private bagel.Font scoreFont;
    private String     scoreText;
    private double     scorePosX;
    private double     scorePosY;
    private final ScoreManager scoreManager = new ScoreManager();
    private int        enemyScoreValue;
    private int        powerupScoreValue;
    private int        gotHitScoreDeduction;

    private String waveText;
    private double wavePosX;
    private double wavePosY;

    private String timescaleText;
    private double timescalePosX;
    private double timescalePosY;

    // Developer Mode
    private double  timescale      = 1.0;
    private int     timescaleLevel = 1;

    // Reward points for wave completion
    private int waveScoreValue;

    // frame count
    private double frameCount = 0;

    // Manager and screen
    private WaveManager waveManager;
    private StartScreen startScreen;
    private PauseScreen pauseScreen;
    private EndScreen   endScreen;

    // ----------------------------------------------------------------
    // constructor
    // ----------------------------------------------------------------

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        GameConfig.init(gameProps);
        GameConfig config = GameConfig.getInstance();
        screenWidth  = Integer.parseInt(
                gameProps.getProperty("window.width")
        );
        screenHeight = Integer.parseInt(
                gameProps.getProperty("window.height")
        );

        // backgroundColour
        String[] bgParts = config.getProperty("background.colour").split(",");
        this.backgroundColour = new bagel.util.Colour(
                Double.parseDouble(bgParts[0].trim()),
                Double.parseDouble(bgParts[1].trim()),
                Double.parseDouble(bgParts[2].trim())
        );
        Window.setClearColour(
                backgroundColour.r,
                backgroundColour.g,
                backgroundColour.b
        );

        // textColour
        String[] colourParts = config.getProperty("text.colour").split(",");
        this.textColour = new bagel.util.Colour(
                Double.parseDouble(colourParts[0].trim()) / 255.0,
                Double.parseDouble(colourParts[1].trim()) / 255.0,
                Double.parseDouble(colourParts[2].trim()) / 255.0
        );

        // player
        String playerImg   = config.getProperty("player.image");
        double playerPosY  = Double.parseDouble(
                config.getProperty("player.posY")
        );
        double playerSpeed = Double.parseDouble(
                config.getProperty("player.speed")
        );
        int    playerLives = Integer.parseInt(
                config.getProperty("player.initialLives")
        );
        int    shootCooldown = Integer.parseInt(
                config.getProperty("player.shootCooldown")
        );
        String shieldImg   = config.getProperty(
                "invincibility.image"
        );
        double startX      = screenWidth / 2.0;

        this.player = new PlayerShip(
                playerImg,
                new Point(startX, playerPosY),
                playerSpeed,
                playerLives,
                shootCooldown,
                shieldImg
        );

        // heartImage
        this.heartImage = new Image(config.getProperty("playerLives.image"));
        String[] heartPos = config.getProperty(
                "playerLives.startPosition"
        ).split(",");
        this.heartStartX = Double.parseDouble(heartPos[0].trim());
        this.heartY      = Double.parseDouble(heartPos[1].trim());
        this.heartGap    = Double.parseDouble(
                gameProps.getProperty("playerLives.gap")
        );

        // Explosion
        this.smallExplosionImg      = config.getProperty(
                "explosion.small.image"
        );
        this.largeExplosionImg      = config.getProperty(
                "explosion.large.image"
        );
        this.smallExplosionDuration = Integer.parseInt(
                config.getProperty("explosion.small.duration")
        );
        this.largeExplosionDuration = Integer.parseInt(
                config.getProperty("explosion.large.duration")
        );

        // score
        this.scoreText           = config.getProperty("score.text");
        String[] scorePos        = config.getProperty("score.pos").split(",");
        this.scorePosX           = Double.parseDouble(scorePos[0].trim());
        this.scorePosY           = Double.parseDouble(scorePos[1].trim());
        this.scoreFont           = new bagel.Font(
                config.getProperty("text.font"),
                Integer.parseInt(config.getProperty("text.size"))
        );
        this.enemyScoreValue     = Integer.parseInt(config.getProperty(
                "score.destroyedEnemy.regular"
                )
        );
        this.powerupScoreValue   = 0;  // 配置文件里没有这个，设为0
        this.waveScoreValue      = Integer.parseInt(
                config.getProperty("score.waveCompleted")
        );
        this.gotHitScoreDeduction = Integer.parseInt(
                config.getProperty("score.gotHit")
        );

        // wave display
        this.waveText = config.getProperty("wave.text");
        String[] wavePos = config.getProperty("wave.pos").split(",");
        this.wavePosX = Double.parseDouble(wavePos[0].trim());
        this.wavePosY = Double.parseDouble(wavePos[1].trim());

        // timescaleText
        this.timescaleText = config.getProperty("timescale.text");
        String[] tsPos = config.getProperty("timescale.pos").split(",");
        this.timescalePosX = Double.parseDouble(tsPos[0].trim());
        this.timescalePosY = Double.parseDouble(tsPos[1].trim());

        // waveManager and Screen
        this.waveManager = new WaveManager();
        this.startScreen = new StartScreen();
        this.pauseScreen = new PauseScreen();
        this.endScreen   = new EndScreen();

        scoreManager.addObserver(this);
    }

    // ----------------------------------------------------------------
    // main loop
    // ----------------------------------------------------------------
    @Override
    public void onScoreChanged(int newScore) {
        // Additional effects can be triggered here,
        // such as combo effects.
        // At present, all you need to know is
        // that the score has changed.
        // Just call renderUI() and it will directly read
        // scoreManager.getScore()
    }

    @Override
    protected void update(Input input) {
        Window.setClearColour(
                backgroundColour.r,
                backgroundColour.g,
                backgroundColour.b
        );

        switch (gameState) {
            case START:
                handleStartState(input);
                break;
            case BATTLE:
                handleBattleState(input);
                break;
            case PAUSE:
                handlePauseState(input);
                break;
            case END:
                handleEndState(input);
                break;
        }
    }

    // ----------------------------------------------------------------
    // Handling of each state
    // ----------------------------------------------------------------

    private void handleStartState(Input input) {
        startScreen.render(screenWidth, textColour);

        if (input.wasPressed(Keys.SPACE)) {
            gameState = GameState.BATTLE;
        }
    }

    private void handleBattleState(Input input) {
        // R- reset
        if (input.wasPressed(Keys.R)) {
            resetGame();
            return;
        }

        // ESC - pause
        if (input.wasPressed(Keys.ESCAPE)) {
            gameState = GameState.PAUSE;
            return;
        }

        handleTimescaleInput(input);
        frameCount += timescale;

        // update player
        player.setInput(input);
        player.update(timescale);

        // player shooting
        handleShooting(input);

        // Wave update: Spawn enemies and items
        waveManager.update(timescale, enemies, powerups);

        // update Entities
        updateEntities();

        // check Collisions
        checkCollisions();

        // Identify and remove the extraneous entities
        cleanUp();

        // render
        renderBattle();

        // check if win the game
        if (player.getLives() <= 0) {
            isWin = false;
            gameState = GameState.END;
        } else if (waveManager.isWaveComplete(
                enemies,
                enemyProjectiles,
                powerups)) {
            if (waveManager.isAllWavesComplete()) {
                isWin = true;
                gameState = GameState.END;
            } else {
                scoreManager.add(waveScoreValue);
                waveManager.nextWave();
            }
        }
    }

    private void handlePauseState(Input input) {
        // First, depict the battlefield
        // (the screen remains still but
        // the scene is visible during the pause)
        renderBattle();

        // Add a pause interface on top of it
        pauseScreen.render(screenWidth, timescaleLevel, textColour);

        if (input.wasPressed(Keys.ESCAPE)) {
            gameState = GameState.BATTLE;
        }

        if (input.wasPressed(Keys.R)) {
            resetGame();
            return;
        }

        handleTimescaleInput(input);
    }

    private void handleEndState(Input input) {
        // under end screen, players can still move left and right,
        // but they cannot shoot.
        player.setInput(input);
        player.update(timescale);
        player.render();

        endScreen.render(screenWidth, isWin, textColour);

        // Start over by pressing the spacebar.
        if (input.wasPressed(Keys.SPACE)) {
            resetGame();
        }
    }

    // ----------------------------------------------------------------
    // update Entities
    // ----------------------------------------------------------------

    private void updateEntities() {
        for (Projectile p : projectiles) {
            p.update(timescale);
        }

        for (Enemy e : enemies) {
            e.update(timescale);

            // EnemyShooting: Attempt to shoot in each frame
            if (e instanceof EnemyShooting) {
                EnemyProjectile ep = ((EnemyShooting) e).shoot();
                if (ep != null) {
                    enemyProjectiles.add(ep);
                }
            }
        }

        for (EnemyProjectile ep : enemyProjectiles) {
            ep.update(timescale);
        }

        for (Explosion exp : explosions) {
            exp.update(timescale);
        }

        for (Powerup pu : powerups) {
            pu.update(timescale);
        }
    }

    // ----------------------------------------------------------------
    // collision check
    // ----------------------------------------------------------------

    private void checkCollisions() {
        GameConfig config = GameConfig.getInstance();

        // Player bullets vs Enemies
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            boolean hit = false;

            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy e = enemies.get(j);
                if (p.getBoundingBox().intersects(e.getBoundingBox())) {
                    // big explosion
                    explosions.add(new Explosion(largeExplosionImg,
                            e.getPosition(),
                            largeExplosionDuration,
                            true));
                    // Add points based on the type of enemy
                    if (e instanceof EnemyShooting) {
                        scoreManager.add(
                                Integer.parseInt(config.getProperty(
                                        "score.destroyedEnemy.shooting")
                                )
                        );
                    } else if (e instanceof EnemyStrafing) {
                        scoreManager.add(
                                Integer.parseInt(config.getProperty(
                                        "score.destroyedEnemy.strafing")
                                )
                        );
                    } else {
                        scoreManager.add(
                                Integer.parseInt(config.getProperty(
                                        "score.destroyedEnemy.regular")
                                )
                        );
                    }
                    enemies.remove(j);
                    hit = true;
                    break;
                }
            }

            // player projectiles vs enemy projectile
            if (!hit) {
                for (int k = enemyProjectiles.size() - 1; k >= 0; k--) {
                    if (p.getBoundingBox().intersects(
                            enemyProjectiles.get(k).getBoundingBox())
                    ) {
                        enemyProjectiles.remove(k);
                        hit = true;
                        break;
                    }
                }
            }

            if (hit) {
                projectiles.remove(i);
            }
        }

        // enemy / enemy projectile vs player
        if (!player.isInvincible()) {
            // enemy hits player
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy e = enemies.get(i);
                if (e.getBoundingBox().intersects(player.getBoundingBox())) {
                    // small explosion
                    explosions.add(new Explosion(smallExplosionImg,
                            e.getPosition(),
                            smallExplosionDuration,
                            false));
                    player.decreaseLife();
                    scoreManager.deduct(gotHitScoreDeduction);
                    player.applyHitInvincibility(
                            Integer.parseInt(config.getProperty(
                                    "player.hitInvincibilityTime")
                            )
                    );
                    enemies.remove(i);
                }
            }

            // enemy projectile hits player
            for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
                if (enemyProjectiles.get(i).getBoundingBox().intersects(
                        player.getBoundingBox())
                ) {
                    player.decreaseLife();
                    scoreManager.deduct(gotHitScoreDeduction);
                    player.applyHitInvincibility(
                            Integer.parseInt(
                                    config.getProperty("player.hitInvincibilityTime")
                            )
                    );
                    enemyProjectiles.remove(i);
                }
            }
        }

        // Props vs Players
        // (Score based on configuration file; no bonus points for props)
        for (int i = powerups.size() - 1; i >= 0; i--) {
            Powerup pu = powerups.get(i);
            if (pu.getBoundingBox().intersects(player.getBoundingBox())) {
                pu.activate(player);
                powerups.remove(i);
            }
        }
    }

    // ----------------------------------------------------------------
    // Identify and remove boundary entities
    // ----------------------------------------------------------------

    private void cleanUp() {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).isOffScreen()) {
                projectiles.remove(i);
            }
        }
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).isOffScreen(screenHeight)) {
                enemies.remove(i);
            }
        }
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            if (enemyProjectiles.get(i).isOffScreen()) {
                enemyProjectiles.remove(i);
            }
        }
        for (int i = explosions.size() - 1; i >= 0; i--) {
            if (explosions.get(i).isFinished()) {
                explosions.remove(i);
            }
        }
        for (int i = powerups.size() - 1; i >= 0; i--) {
            if (powerups.get(i).isOffScreen(screenHeight)) {
                powerups.remove(i);
            }
        }
    }

    // ----------------------------------------------------------------
    // render
    // ----------------------------------------------------------------

    private void renderBattle() {
        player.render();

        for (Projectile p : projectiles)         p.render();
        for (Enemy e : enemies)                  e.render();
        for (EnemyProjectile ep : enemyProjectiles) ep.render();
        for (Explosion exp : explosions)         exp.render();
        for (Powerup pu : powerups)              pu.render();

        renderLives();
        renderUI();
    }

    private void renderLives() {
        int lives = player.getLives();
        for (int i = 0; i < lives; i++) {
            heartImage.draw(heartStartX + i * heartGap, heartY);
        }
    }

    private void renderUI() {
        DrawOptions opt = new DrawOptions().setBlendColour(textColour);
        scoreFont.drawString(
                scoreText + " " + scoreManager.getScore(),
                scorePosX,
                scorePosY,
                opt
        );
        scoreFont.drawString(waveText + " " + waveManager.getCurrentWave(),
                wavePosX, wavePosY, opt);
        scoreFont.drawString(timescaleText + timescaleLevel,
                timescalePosX, timescalePosY, opt);
    }

    // ----------------------------------------------------------------
    // shooting
    // ----------------------------------------------------------------

    private void handleShooting(Input input) {
        frameSinceLastShot += timescale;

        if (input.wasPressed(Keys.SPACE) &&
                frameSinceLastShot >= player.getShootCooldown()
        ) {
            String projImg   = GameConfig.getInstance().getProperty(
                    "projectile.image"
            );
            double projSpeed = Double.parseDouble(
                    GameConfig.getInstance().getProperty(
                            "projectile.movementSpeed"
                    )
            );
            projectiles.add(new Projectile(
                    projImg,
                    player.getPosition(),
                    projSpeed)
            );
            frameSinceLastShot = 0;
        }
    }

    // ----------------------------------------------------------------
    // timescale
    // ----------------------------------------------------------------

    private void handleTimescaleInput(Input input) {
        if (input.wasPressed(Keys.G)) {
            timescaleLevel++;
        }
        if (input.wasPressed(Keys.F)) {
            timescaleLevel--;
        }
        timescale = timescaleLevel >= 1
                ? (double) timescaleLevel
                : 1.0 / Math.abs(timescaleLevel);
    }

    // ----------------------------------------------------------------
    // reset
    // ----------------------------------------------------------------

    private void resetGame() {
        player.resetToInitialState();
        enemies.clear();
        projectiles.clear();
        enemyProjectiles.clear();
        explosions.clear();
        powerups.clear();
        scoreManager.reset();
        frameCount     = 0;
        timescale      = 1.0;
        timescaleLevel = 1;
        frameSinceLastShot = 0;
        gameState = GameState.BATTLE;
        waveManager.reset();
    }

    // ----------------------------------------------------------------
    // main
    // ----------------------------------------------------------------

    public static void main(String[] args) {
        String filePath = System.getProperty(
                "gameData",
                "gameData.properties"
        );
        Properties gameProps = IOUtils.readPropertiesFile(filePath);
        if (gameProps == null) {
            System.out.println("Error with " + filePath);
            System.exit(-1);
        }

        // Initialize Singleton
        // This is the only time it will be done throughout the entire program
        GameConfig.init(gameProps);

        new ShadowAliens(gameProps).run();
    }
}
