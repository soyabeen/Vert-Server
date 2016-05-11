package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 08.05.2016.
 */
public class FindMarshalTargets implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerExecRule.class);

    @Override
    public boolean evaluate(ActionCommand command) {
        Marshal marshal = new Marshal( command.getGame().getPositionMarshal() );
        return RuleUtils.isOnSameLevel(marshal, marshal.getLevel())
                && RuleUtils.isPlacedOnTrain(marshal, marshal.getCar());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {

        Marshal marshal = new Marshal(command.getGame().getPositionMarshal());
        List<Positionable> result = new ArrayList<>();
        result.add(marshal);

        return result;
    }

}
