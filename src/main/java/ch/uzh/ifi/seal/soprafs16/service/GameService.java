package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.GameConfigurator;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by soyabeen on 31.03.16.
 */
@Service("gameService")
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LootService lootService;

    @Autowired
    private PhaseLogicService logicService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private PlayerRepository playerRepo;

    private GameConfigurator gameConf;

    public GameService() {
        gameConf = new GameConfigurator();
    }

    public List<Game> listGames(String filter) {
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);

        if ("AVAILABLE".equals(filter)) {
            result = result.stream().filter(g -> g.getPlayers().size() < 4).collect(Collectors.toList());
        }

        return result;
    }

    private Game createGame(String gameName, Player owner, int players) {
        Game gameShell = new Game();
        gameShell.setStatus(GameStatus.PENDING);
        gameShell.setName(gameName);
        gameShell.setOwner(owner.getUsername());
        gameShell.addPlayer(owner);
        gameShell.setRoundId(1);
        logger.debug("game shell " + gameShell.toString());

        Game game = gameRepo.save(gameShell);
        logger.debug("game pending " + game.toString());
        return gameRepo.save(game);
    }

    public Game createGame(Game game, String userToken, int nrOfPlayers) {
        logger.debug("create game for name: {}, owner: {}, players: {}", game.getName(), userToken, nrOfPlayers);

        InputArgValidator.checkNotEmpty(game.getName(), "gamename");
        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        InputArgValidator.checkNotEmpty(tokenOwner.getUsername(), "owner");

        // game name available?
        if (gameRepo.findByName(game.getName()) != null) {
            throw new InvalidInputException("Invalid arg : Name of game is already used.");
        }

        int players = (nrOfPlayers < GameConfigurator.MIN_PLAYERS)
                ? GameConfigurator.MAX_PLAYERS : nrOfPlayers;
        return createGame(game.getName(), tokenOwner, players);
    }

    public void startGame(Long gameId, String userToken) {
        startGame(gameId, userToken, new RoundConfigurator());
    }

    public void startGame(Long gameId, String userToken, RoundConfigurator configurator) {
        logger.debug("Start game {} for {}", gameId, userToken );

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game pendingGame = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");

        // Game must be in pending state
        if (!GameStatus.PENDING.equals(pendingGame.getStatus())) {
            throw new IllegalStateException("Only games in " + GameStatus.PENDING
                    + " can be started. This game is " + pendingGame.getStatus());
        }

        // Belongs the game to the user?
        if (!tokenOwner.getUsername().equals(pendingGame.getOwner())) {
            throw new IllegalStateException("User is not allowed to start the game. User must be game owner.");
        }

        // Enough players?
        List<Player> players = playerService.listPlayersForGame(pendingGame.getId());
        int nrOfPlayers = players.size();
        if (nrOfPlayers < GameConfigurator.MIN_PLAYERS) {
            throw new IllegalStateException("Not enough players to start the game. Need at least "
                    + GameConfigurator.MIN_PLAYERS + " players.");
        }
        //Get Car and Loot configurations
        Game game = gameConf.configureGameForNrOfPlayers(pendingGame, nrOfPlayers);
        logger.debug("game with loots and cars" + game.toString());
        logger.debug("input val ok.");

        pendingGame.setStatus(GameStatus.PLANNINGPHASE);
        gameRepo.save(game);

        //Choose, initialize and save rounds for the new game
        List<Round> rounds = configurator.generateRoundsForGame(game);
        roundRepo.save(rounds);

        //Set start player
        logicService.setInitialPlayer(game.getId(), tokenOwner.getId());

        setPositionOfPlayers(game, players);
        playerRepo.save(players);


    }

    private void setPositionOfPlayers(Game game, List<Player> players) {

        int nrOfCars = game.getNrOfCars();

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            players.get(i).setCar(nrOfCars - (i % 2) - 1);
            players.get(i).setLevel(Positionable.Level.BOTTOM);
        }
    }
    protected Game loadGameFromRepo(long gameIdToLoad) {
        return gameRepo.findOne(gameIdToLoad);
    }

}
