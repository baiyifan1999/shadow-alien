package game;

import bagel.util.Point;

import java.util.Properties;

public abstract class Enemy extends Entity {
    protected double speed;
    protected int arrivalTime;
    protected double posX;

    private final MovementStrategy movementStrategy;

    public Enemy(String imagePath, Point pos, double speed,
                 int arrivalTime, MovementStrategy movementStrategy) {
        super(imagePath, pos);
        this.speed = speed;
        this.arrivalTime = arrivalTime;
        this.posX = pos.x;
        this.movementStrategy = movementStrategy;
    }

    @Override
    public void update(double timescale) {
        this.position = movementStrategy.move(
                position, speed, timescale, image.getWidth()
        );
    }

    public boolean isOffScreen(double screenHeight) {
        return position.y - image.getHeight() / 2.0 > screenHeight;
    }

    public int getArrivalTime() { return arrivalTime; }
}