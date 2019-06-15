package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by guyolive on 1/25/2018.
 */

public class PlayerComparator implements Comparator<Player>
{
    private Integer m_attackFactor = 40;
    private Integer m_defenseFactor = 30;
    private Integer m_playMakerFactor = 0;
    private Integer m_fitnessFactor = 30;

    public PlayerComparator(Integer m_attackFactor, Integer m_defenseFactor,
                            Integer m_playMakerFactor, Integer m_fitnessFactor) {
        this.m_attackFactor = m_attackFactor;
        this.m_defenseFactor = m_defenseFactor;
        this.m_playMakerFactor = m_playMakerFactor;
        this.m_fitnessFactor = m_fitnessFactor;
    }


    public int compare(Player left, Player right) {
        int compare_players = PlayerRoundValue.compareWithRoundUpToNearest5IfNeeded(
                Integer.valueOf(right.getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor, m_fitnessFactor).intValue()),
                Integer.valueOf(left.getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor, m_fitnessFactor).intValue())
                );
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

