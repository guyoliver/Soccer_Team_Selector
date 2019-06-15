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

    public Double getTotalFactor(Integer attackFactor, Integer DefenseFactor,
                                 Integer playMakerFactor, Integer fitness) {
        Double tempSum = (this.attack*(double)attackFactor) + (this.defense*(double)DefenseFactor) +
                (this.playMaker*(double)playMakerFactor) + (this.fitness*(double)fitness);
        tempSum /= 100; //(same base)
        return tempSum; //Math.round(tempSum);
    }

    public Player(int id, String name,Integer attack,Integer defense,  Integer playMaker,
                  Integer fitness, boolean isPlayNextMatch){
        this.id = id;
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.fitness = fitness;
        this.playMaker = playMaker;
        this.isPlayNextMatch = isPlayNextMatch;
    }

}
