# SWEN20003 Semester 1, 2026
# Project 1
# Shadow Aliens

## Running Instructions

Ensure Java 25 is installed and available on your system path.

To run via IntelliJ IDEA:
1. Open the project in IntelliJ IDEA.
2. Ensure the Maven project is loaded (pom.xml recognised).
3. Edit the Run Configuration and set VM option: `-DgameData=gameData.properties`
4. Run `game.ShadowAliens` as the main class.

To run via command line from the project root:
mvn compile
java -DgameData=gameData.properties -cp target/classes:bagel-swen20003-2.0.0.jar game.ShadowAliens

## Assumptions

* Enemy projectiles use the same image as the player projectile, rotated 180 degrees, as no separate enemy projectile image was provided.
* Collecting a powerup does not award points, as no corresponding property key exists in the specification's sample properties file.
* The `score.destroyedEnemy.regular` value is used as a fallback score reference in the constructor, though per-type scoring is handled dynamically at collision time.

* 

## AI Statement

None

## Code References

None

* 

## Design Report

### Changes from Project 2a Design

The following design patterns were implemented as extensions to the Project 1 codebase. Each pattern was applied where it provided the clearest separation of responsibilities.

#### 1. Factory Method — `EnemyFactory`, `PowerupFactory`

**Changed:** `WaveManager` previously contained two private methods with `switch` statements that directly instantiated `Enemy` and `Powerup` subclasses.

**New files:** `EnemyFactory.java`, `PowerupFactory.java`

**Change:** The instantiation logic was extracted into dedicated factory classes. `WaveManager` now delegates object creation to `EnemyFactory.create(EnemySpawn)` and `PowerupFactory.create(PowerupSpawn)`. This means adding a new enemy or powerup type no longer requires modifying `WaveManager`, satisfying the open/closed principle.

#### 2. Singleton — `GameConfig`

**Changed:** `gameProps` (a `Properties` object) was previously passed as a constructor parameter to `WaveManager`, `StartScreen`, `PauseScreen`, and `EndScreen`.

**New file:** `GameConfig.java`

**Change:** `GameConfig` wraps the `Properties` object and exposes it via a static `getInstance()` method. It is initialised once in `ShadowAliens` and accessed globally, eliminating the need to pass configuration through constructor chains. The constructor of `ShadowAliens` retains the `Properties` parameter only because `super()` must be the first statement and requires width/height values before `GameConfig` can be initialised.

#### 3. Template Method — `Powerup.activate()`

**Changed:** `Powerup` previously declared `activate(PlayerShip)` as an abstract method, with each subclass implementing the full activation logic independently.

**Changed files:** `Powerup.java`, `ShieldPowerup.java`, `LifePowerup.java`, `CooldownPowerup.java`, `EnginePowerup.java`

**Change:** `activate()` is now a `final` concrete method in `Powerup` that defines a fixed two-step sequence: log the activation, then call the abstract `applyEffect(PlayerShip)`. Subclasses implement only `applyEffect()`. This ensures all powerups follow the same activation structure, and any future shared behaviour (e.g. sound effects) can be added in one place.

#### 4. Strategy — `MovementStrategy`

**Changed:** Each `Enemy` subclass overrode `update()` with its own movement logic embedded directly in the method body.

**New files:** `MovementStrategy.java`, `StraightDownMovement.java`, `StrafingMovement.java`

**Changed files:** `Enemy.java`, `EnemyRegular.java`, `EnemyStrafing.java`, `EnemyShooting.java`

**Change:** Movement logic was extracted into a `MovementStrategy` interface with a single `move()` method. `Enemy` now holds a `MovementStrategy` field and its `update()` delegates to it. Each subclass passes the appropriate strategy to the parent constructor. `EnemyShooting` overrides `update()` only to additionally increment its firing timer, calling `super.update()` for movement.

#### 5. Observer — `ScoreObserver`, `ScoreManager`

**Changed:** `ShadowAliens` previously held a raw `int score` field and modified it directly at every collision and wave completion site.

**New files:** `ScoreObserver.java`, `ScoreManager.java`

**Changed files:** `ShadowAliens.java`

**Change:** `ScoreManager` encapsulates the score value and a list of registered `ScoreObserver` instances. It notifies all observers whenever the score changes via `add()`, `deduct()`, or `reset()`. `ShadowAliens` implements `ScoreObserver` and registers itself with the manager. Score modification throughout `ShadowAliens` is now handled through `scoreManager` rather than direct field assignment, decoupling score state from game logic.

### Outcome

The five patterns collectively reduce coupling across the codebase. `WaveManager` is no longer responsible for object construction. Configuration is accessed globally without threading through constructors. The `Powerup` activation contract is enforced structurally rather than by convention. Enemy movement behaviour is interchangeable without modifying class hierarchies. Score state changes propagate to interested parties without tight dependencies.

Future extensions—such as adding a new enemy type, a new powerup, or a score-based UI element—can be made by adding new classes rather than modifying existing ones, which reflects the open/closed principle applied throughout this design.

## Design Report References

None

* 
