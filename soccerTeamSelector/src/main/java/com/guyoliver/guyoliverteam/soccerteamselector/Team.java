package com.guyoliver.guyoliverteam.soccerteamselector;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guyolive on 3/5/2018.
 */

class Team {

    private static final String TAG = Team.class.getName();

    private String name;
    private Integer totalDefense;
    private Integer totalAttack;
    private Integer totalFactor;
    private Integer numberOfPlayer;
    private List<Player> players;
    private Integer m_attackFactor = 40;
    private Integer m_defenseFactor = 30;
    private Integer m_playMakerFactor = 0;
    private Integer m_fitnessFactor = 30;

    public Integer getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer(Integer playerNumber) {
        if (playerNumber <= numberOfPlayer) {
            return players.get(playerNumber);
        }
        else {
            Log.e(TAG, "exceeded max number of players, exist " + numberOfPlayer +
                    " requested " + playerNumber);

            return null;
        }
    }
    public List<Player> copyListAndShuffelTeam() {
        List<Player> tempPlayers = this.players;
        Collections.shuffle(tempPlayers);
        return tempPlayers;

    }

    public Team(String name, Integer m_attackFactor, Integer m_defenseFactor,
                Integer m_playMakerFactor, Integer m_fitnessFactor) {
        this.name = name;
        this.totalDefense = 0;
        this.totalAttack = 0;
        this.totalFactor = 0;
        this.numberOfPlayer = 0;
        this.players = new ArrayList();
        this.m_attackFactor = m_attackFactor;
        this.m_defenseFactor = m_defenseFactor;
        this.m_playMakerFactor = m_playMakerFactor;
        this.m_fitnessFactor = m_fitnessFactor;

    }


    public Integer getTotalFactor() {
        return totalFactor;
    }

    public void addPlayer(Player player) {
        players.add(player);
        numberOfPlayer = players.size();
        calculateTeamFactors();
    }
    public Integer calculateTeamFactors() {
        totalAttack = totalDefense = totalFactor = 0;

        for (Player player : players) {
            totalAttack += player.getAttack();
            totalDefense += player.getDefense();
            totalFactor += player.getTotalFactor(m_attackFactor, m_defenseFactor,
                    m_playMakerFactor, m_fitnessFactor);
        }
        return totalFactor;
    }
}