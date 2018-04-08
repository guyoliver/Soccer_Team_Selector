package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.Comparator;

/**
 * Created by guyolive on 1/25/2018.
 */

public class PlayerAttackComparator implements Comparator<Player>
{
    Integer m_attackDefenseRation = 50;

    public PlayerAttackComparator(Integer m_attackDefenseRation) {
        this.m_attackDefenseRation = m_attackDefenseRation;
    }


    public int compare(Player left, Player right) {
        return right.getTotalFactor(m_attackDefenseRation).compareTo(left.getTotalFactor(m_attackDefenseRation));
    }
}

