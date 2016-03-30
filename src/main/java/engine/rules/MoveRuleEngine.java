package engine.rules;


import engine.Position;
import engine.User;
import engine.effects.MoveEffect;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alex on 19.03.2016.
 */
public class MoveRuleEngine implements RuleEngineFactory {
    private static final Logger log = Logger.getLogger(MoveRuleEngine.class.getName());

    private User owner;
    private Position newPositionIfValid;

    private boolean directionRight = true;
    private int distance;

    public MoveRuleEngine(User owner) {
        this.owner = owner;
    }

    public boolean isValid() {
        Scanner keyboard = new Scanner(System.in);
        log.info("Enter direction(l/r)");
        String direction = keyboard.nextLine();
        log.info("Enter distance");
        distance = keyboard.nextInt();

        if (direction.equals("l")) {
            distance *= -1;
            directionRight = false;
        }


        newPositionIfValid = owner.getPosition().move(distance, 0);

        return new MoveRuleLR(owner, newPositionIfValid).isValid();
    }

    public void execute() {
        if (isValid()) {
            MoveEffect moveEffect = new MoveEffect(owner);
            moveEffect.setDirectionRight(directionRight);
            moveEffect.setdistance(distance);
            moveEffect.execute();
        } else {
            log.log(Level.SEVERE, "Invalid move\nPassing turn");
        }
    }
}
