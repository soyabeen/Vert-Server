package engine.rules;


import engine.User;
import engine.effects.Effect;
import engine.effects.MoveEffect;
import engine.effects.PunchEffect;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by alexanderhofmann on 20/03/16.
 */
public class PunchRuleEngine implements RuleEngineFactory {
    private static final Logger log = Logger.getLogger(PunchRuleEngine.class.getName());

    private List<IRule> rulesToCheck;
    private List<Effect> effectsToExecute;
    private User owner;
    private User target;


    public PunchRuleEngine(User owner) {
        target = new User("Player 2");
        rulesToCheck = new ArrayList<>();
        effectsToExecute = new ArrayList<>();

        rulesToCheck.add(new PunchRule(owner));
        rulesToCheck.add(new MoveRuleLR(owner));

        effectsToExecute.add(new PunchEffect(target));
        effectsToExecute.add(new MoveEffect(target));
    }

    @Override
    public void execute() {
        if (isValid()) {

            for (Effect effect : effectsToExecute) {
                effect.execute();
            }
        } else {
            log.error("Invalid move\nPassing turn");
        }
    }

    @Override
    public boolean isValid() {

        Scanner keyboard = new Scanner(System.in);
        log.info("Enter direction(l/r)");
        String direction = keyboard.nextLine();


        boolean directionRight = true;
        if (direction.equals("l")) {
            directionRight = false;
        }

        ((PunchRule) rulesToCheck.get(0)).setTarget(target);
        ((MoveRuleLR) rulesToCheck.get(1)).setDirectionRight(directionRight);
        ((MoveRuleLR) rulesToCheck.get(1)).setDistance(1);
        ((MoveRuleLR) rulesToCheck.get(1)).updateNewPosition();
        ((MoveEffect) effectsToExecute.get(1)).setDirectionRight(directionRight);
        ((MoveEffect) effectsToExecute.get(1)).setdistance(1);

        for (IRule currentRule : rulesToCheck) {
            if (!currentRule.isValid()) {
                return false;
            }
        }

        return true;
    }
}
