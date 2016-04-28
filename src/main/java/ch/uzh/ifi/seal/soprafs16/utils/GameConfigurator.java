package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.*;

import java.util.*;

/**
 * This class handles the initialisation and configuration of a game.
 * The class generates the train configuration and the corresponding loot objects.
 */
public class GameConfigurator {
    public static final Integer MIN_PLAYERS = 2;
    public static final Integer MAX_PLAYERS = 4;
    public static final Integer MIN_CARS_PER_GAME = 3;

    public static final Integer LOOT_MAX_PURSES_IN_GAME = 18;
    public static final Integer LOOT_MAX_JEWELS_IN_GAME = 6;
    public static final Integer LOOT_MAX_BOXES_IN_GAME = 2;

    private List<CarConfiguration> carConfigs;

    public GameConfigurator() {
        initConfigurations();
    }

    private void initConfigurations() {
        carConfigs = new ArrayList<>();
        carConfigs.add(new CarConfiguration(1, 0, 3, 0));
        carConfigs.add(new CarConfiguration(2, 1, 1, 0));
        carConfigs.add(new CarConfiguration(3, 1, 0, 0));
        carConfigs.add(new CarConfiguration(4, 3, 1, 0));
        carConfigs.add(new CarConfiguration(5, 4, 1, 0));
        carConfigs.add(new CarConfiguration(6, 3, 0, 0));
    }


    /**
     * Generates loots for the given type and places them on the given car. <br/>
     * At game start, lots are placed on the lower level always.
     *
     * @param gameId The id of the game to which the loot belongs.
     * @param type The type of the loots to build.
     * @param carNr The coordinate for the car where the loots are placed
     * @param nrOfLootsToBuild The nr of loots to buidl.
     * @return A list of loots with the given type, placed on the given car.
     */
    private List<Loot> buildLootsForType(Long gameId, LootType type, int carNr, int nrOfLootsToBuild) {
        List<Loot> result = new ArrayList<>();
        for (int i = 0; i < nrOfLootsToBuild; i++) {
            result.add(new Loot(type, gameId, type.value(), carNr, Positionable.Level.BOTTOM));
        }
        return result;
    }

    /**
     * Generates the loots for the types <code>PURSE_SMALL</code> and <code>PURSE_BIG</code>.
     * These types are handled specially, because the car configuration knows only that it needs a purse, but
     * not if it needs a big purse (value 500) or a small purse (value 250).
     * We create them randomly, like in the original game.<br/>
     * At game start, lots are placed on the lower level always.
     *
     * @param gameId The id of the game to which the loot belongs.
     * @param carNr            The car coordinate on which the loot is places.
     * @param nrOfLootsToBuild The amount of loots to build.
     * @return List of loots with <code>PURSE_SMALL</code> or <code>PURSE_BIG</code> type.
     */
    private List<Loot> buildLootsForPURSETypes(Long gameId, int carNr, int nrOfLootsToBuild) {
        List<Loot> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < nrOfLootsToBuild; i++) {
            if (rnd.nextInt() % 2 == 0) {
                result.add(new Loot(LootType.PURSE_SMALL, gameId, LootType.PURSE_SMALL.value(),
                        carNr, Positionable.Level.BOTTOM));
            } else {
                result.add(new Loot(LootType.PURSE_BIG, gameId, LootType.PURSE_BIG.value(),
                        carNr, Positionable.Level.BOTTOM));
            }
        }
        return result;
    }

    /**
     * Builds the loots for the given <code>CarConfiguration</code>.
     *
     * @param gameId The id of the game to which the loot belongs.
     * @param cc The CarConfiguration object.
     * @param carNr The coordinate for the selected car.
     * @return A list of loots with different types, placed on the given car.
     */
    private List<Loot> buildLootsForCarConfiguration(Long gameId, CarConfiguration cc, int carNr) {
        List<Loot> loots = new ArrayList<>();
        loots.addAll(buildLootsForPURSETypes(gameId, carNr, cc.getNrOfPurses()));
        loots.addAll(buildLootsForType(gameId, LootType.JEWEL, carNr, cc.getNrOfJewels()));
        loots.addAll(buildLootsForType(gameId, LootType.STRONGBOX, carNr, cc.getNrOfBoxes()));
        return loots;
    }

    /**
     * Generates the loot objects for a new game and the given number of cars.<br/>
     * Uses the <code>CarConfiguration</code> objects to generate the loots.
     * The configuration objects will be chosen randomly. <br/>
     * The locomotive will always be placed on the first position of the car list.
     *
     * @param gameId The id of the game to which the loot belongs.
     * @param nrOfCars The nr of cars to create (excluding the locomotive).
     * @return List of loots of all types and placed on different cars.
     */
    protected List<Loot> generateLootsForNCars(Long gameId, int nrOfCars) {
        ArrayList<Loot> loots = new ArrayList<>();

        // locomotive (car #0)
        loots.addAll(buildLootsForCarConfiguration(gameId, new CarConfiguration(0, 0, 0, 1), 0));
        // cars
        Collections.shuffle(carConfigs);
        for (int i = 1; i <= nrOfCars; i++) {
            loots.addAll(buildLootsForCarConfiguration(gameId, carConfigs.get(i), i));
        }
        return loots;
    }

    /**
     * Initialises a newly configured game. <br/>
     * According to the given number of players, the length of the train will be defined. Min. length of
     * the train is locomotive + three cars. Even for two players.</br>
     * Attributes set in the shell:
     * <ul>
     *     <li>nrOfPlayers</li>
     *     <li>nrOfCars</li>
     *     <li>list with loots</li>
     * </ul>
     *
     * @param game The Game to configure.
     * @param nrOfPlayersForGame The number of players for this game configuration.
     * @return game A configured game object.
     * @throws IllegalArgumentException if the number of players is not allowed.
     */
    public Game configureGameForNrOfPlayers(Game game, int nrOfPlayersForGame) {

        if (MIN_PLAYERS > nrOfPlayersForGame || MAX_PLAYERS < nrOfPlayersForGame) {
            throw new IllegalArgumentException("Illegal nr of players, must be between " + MIN_PLAYERS + " - " + MAX_PLAYERS + " but was " + nrOfPlayersForGame);
        }

        if (game == null || game.getId() < 0) {
            throw new IllegalArgumentException("Game object must have an id");
        }

        int nrOfCars = nrOfPlayersForGame < MIN_CARS_PER_GAME ? MIN_CARS_PER_GAME : nrOfPlayersForGame;

        game.setNumberOfPlayers(nrOfPlayersForGame);
        game.setNrOfCars(nrOfCars);
        game.setLoots(generateLootsForNCars(game.getId(), nrOfCars));

        return game;
    }


    /**
     * Configuration of a car card.
     * Copy of the number and types of loots of an original car card.
     */
    public class CarConfiguration {

        private int ccId;
        private int nrOfPurses;
        private int nrOfJewels;
        private int nrOfBoxes;

        public CarConfiguration(int ccId, int nrOfPurses, int nrOfJewels, int nrOfBoxes) {
            this.ccId = ccId;
            this.nrOfPurses = nrOfPurses;
            this.nrOfJewels = nrOfJewels;
            this.nrOfBoxes = nrOfBoxes;
        }

        public int getCcId() {
            return ccId;
        }

        public int getNrOfPurses() {
            return nrOfPurses;
        }

        public int getNrOfJewels() {
            return nrOfJewels;
        }

        public int getNrOfBoxes() {
            return nrOfBoxes;
        }
    }
}
