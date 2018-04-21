package com.guyoliver.guyoliverteam.soccerteamselector;

public class Player {

    private int id;
    private String name;
    private Integer defense;
    private Integer attack;
    private Integer fitness;
    private Integer playMaker;

    private boolean isPlayNextMatch;

    public void setPlayNextMatch(boolean playNextMatch) {
        this.isPlayNextMatch = playNextMatch;
    }

    public boolean isPlayNextMatch() {
        return isPlayNextMatch;
    }


    public int getId() {return id;}
    public String getName() {
        return name;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getAttack() {
        return attack;
    }



    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }
    public Integer getFitness() {
        return fitness;
    }

    public void setFitness(Integer fitness) {
        this.fitness = fitness;
    }

    public Integer getPlayMaker() {
        return playMaker;
    }

    public void setPlayMaker(Integer playMaker) {
        this.playMaker = playMaker;
    }

    public Integer getTotalFactor(Integer attackFactor, Integer DefenseFactor,
                                  Integer playMakerFactor, Integer fitness) {
        return ((this.attack*attackFactor/100) + (this.defense*DefenseFactor/100) +
                (this.playMaker*playMakerFactor/100) + (this.fitness*fitness/100));
    }

    public Player(int id, String name,Integer defense, Integer attack, Integer playMaker,
                  Integer fitness, boolean isPlayNextMatch){
        this.id = id;
        this.name = name;
        this.defense = defense;
        this.attack = attack;
        this.fitness = fitness;
        this.playMaker = playMaker;
        this.isPlayNextMatch = isPlayNextMatch;
    }

}
