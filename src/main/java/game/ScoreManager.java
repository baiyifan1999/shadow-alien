package game;

import java.util.ArrayList;

/**
 * Observer pattern: Subject that holds the score and
 * notifies registered observers whenever it changes.
 */
public class ScoreManager {

    private int score = 0;
    private final ArrayList<ScoreObserver> observers = new ArrayList<>();

    /** Register an observer to receive score change notifications. */
    public void addObserver(ScoreObserver observer) {
        observers.add(observer);
    }

    /** Add points to the score and notify all observers. */
    public void add(int points) {
        score += points;
        if (score < 0) score = 0;
        notifyObservers();
    }

    /** Subtract points from the score (will not go below 0). */
    public void deduct(int points) {
        score -= points;
        if (score < 0) score = 0;
        notifyObservers();
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
        notifyObservers();
    }

    private void notifyObservers() {
        for (ScoreObserver o : observers) {
            o.onScoreChanged(score);
        }
    }
}