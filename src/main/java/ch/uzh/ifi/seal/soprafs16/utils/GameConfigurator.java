package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.*;

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


    private List<Loot> buildLootsForType(LootType type, int carNr, int nrOfLootsToBuild) {
        List<Loot> result = new ArrayList<>();
        for (int i = 0; i < nrOfLootsToBuild; i++) {
            result.add(new Loot(type, type.value(), carNr, Positionable.Level.BOTTOM));
        }
        return result;
    }

    private List<Loot> buildLootsForPURSETypes(int carNr, int nrOfLootsToBuild) {
        List<Loot> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < nrOfLootsToBuild; i++) {
            if (rnd.nextInt() % 2 == 0) {
                result.add(new Loot(LootType.PURSE_SMALL, LootType.PURSE_SMALL.value(),
                        carNr, Positionable.Level.BOTTOM));
            } else {
                result.add(new Loot(LootType.PURSE_BIG, LootType.PURSE_BIG.value(),
                        carNr, Positionable.Level.BOTTOM));
            }
        }
        return result;
    }

    private List<Loot> buildLootsForCarConfiguration(CarConfiguration cc, int carNr) {
        List<Loot> loots = new ArrayList<>();
        loots.addAll(buildLootsForPURSETypes(carNr, cc.getNrOfPurses()));
        loots.addAll(buildLootsForType(LootType.JEWEL, carNr, cc.getNrOfJewels()));
        loots.addAll(buildLootsForType(LootType.STRONGBOX, carNr, cc.getNrOfBoxes()));
        return loots;
    }

    protected List<Loot> generateLootsForNCars(int nrOfCars) {
        ArrayList<Loot> loots = new ArrayList<>();

        // locomotive (car #0)
        loots.addAll(buildLootsForCarConfiguration(new CarConfiguration(0, 0, 0, 1), 0));
        // cars
        Collections.shuffle(carConfigs);
        for (int i = 1; i <= nrOfCars; i++) {
            loots.addAll(buildLootsForCarConfiguration(carConfigs.get(i), i));
        }
        return loots;
    }

    /**
     *
     * @param nrOfPlayersForGame
     * @return
     */
    public Game createGameEmptyGameShellForNrOfPlayers(int nrOfPlayersForGame) {

        if(MIN_PLAYERS > nrOfPlayersForGame || MAX_PLAYERS < nrOfPlayersForGame) {
            throw new IllegalArgumentException("Illegal nr of players, must be between "+MIN_PLAYERS +" - "+ MAX_PLAYERS +" but was "+ nrOfPlayersForGame);
        }

        int nrOfCars = (nrOfPlayersForGame < MIN_CARS_PER_GAME ? MIN_CARS_PER_GAME : nrOfPlayersForGame);

        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setNumberOfPlayers(nrOfPlayersForGame);
        game.setNrOfCars(nrOfCars);
        game.setLoots(new HashSet<>(generateLootsForNCars(nrOfCars)));

        return game;
    }


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
