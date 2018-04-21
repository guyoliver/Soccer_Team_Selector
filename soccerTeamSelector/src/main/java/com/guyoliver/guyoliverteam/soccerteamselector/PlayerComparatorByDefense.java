package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.Comparator;

/**
 * Created by guyolive on 1/25/2018.
 */

public class PlayerComparatorByDefense implements Comparator<Player>
{
    public int compare(Player left, Player right) {
        return right.getDefense().compareTo(left.getDefense());
    }
}

