package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;

/**
 * Created by devuser on 15.05.2016.
 */
public class RoundEndFactory {

    public static RoundEnd chooseEnd(RoundEndEvent roundEndEvent) {

        switch(roundEndEvent) {
            case ANGRY_MARSHAL:
                return new AngryMarshal();

            case PIVOTTABLE_POLE:
                return new PivotablePole();

            case BRAKING:
                return new Braking();

            case GET_IT_ALL:
                return new GetItAll();

            case REBELLION:
                return new Rebellion();

            case PICKPOCKETING:
                return new Pickpocketing();

            case MARSHALS_REVENGE:
                return new MarshalsRevenge();

            case HOSTAGE:
                return new Hostage();

            case NONE:
                return new None();

            default:
                // TODO: What to do?
                return null;
        }

    }
}
