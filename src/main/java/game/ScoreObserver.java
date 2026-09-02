package game;

/**
 * Observer pattern: implemented by any class that wants to
 * be notified when the score changes.
 */
public interface ScoreObserver {
    void onScoreChanged(int newScore);
}
