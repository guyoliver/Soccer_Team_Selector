package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Context;

public final class PlayerRoundValue {

    static private Integer roundToNearest5 (int value) {
        return (value + 2) / 5 * 5;
    }

    static public Integer compareWithRoundUpToNearest5IfNeeded (Integer right, Integer left) {
        //GUYO to update
        Boolean isRound = SettingDatabase.getInstance(GlobalApplication.getAppContext()).getIsToRoundValues();

        int compare_players;
        if (false == isRound ) {
            compare_players = right.compareTo(left);
        }
        else
        {
            compare_players = PlayerRoundValue.roundToNearest5(right).compareTo(PlayerRoundValue.roundToNearest5(left));
        }
        return compare_players;
    }
}
