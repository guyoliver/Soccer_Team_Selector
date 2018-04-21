package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.Comparator;

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
        return right.getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor, m_fitnessFactor).
                compareTo(left.getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor, m_fitnessFactor));
    }
}

