package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by guyolive on 1/25/2018.
 */

public class PlayerComparatorByAttack implements Comparator<Player>
{
    public int compare(Player left, Player right) {
        int compare_players = right.getAttack().compareTo(left.getAttack());
        if (0 == compare_players)
        {
            Random rand = new Random();

            //50 is the maximum and the 1 is our minimum
            int  n = rand.nextInt(50) + 1;
            if (25 < n)
                return 1;
            else
                return -1;
        }
        else
            return compare_players;
    }
}

