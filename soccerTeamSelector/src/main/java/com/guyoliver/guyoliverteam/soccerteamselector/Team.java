package com.guyoliver.guyoliverteam.soccerteamselector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guyolive on 3/5/2018.
 */

class Team {
    String name;
    Integer totalDefense;
    Integer totalAttack;
    Integer numberOfPlayer;
    List<Player> players;

    public Team(String name) {
        this.name = name;
        this.totalDefense = 0;
        this.totalAttack = 0;
        this.numberOfPlayer = 0;
        this.players = new ArrayList();
    }
}