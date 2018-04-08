package com.guyoliver.guyoliverteam.soccerteamselector;

public class Player {

    private int id;
    private String name;
    private Integer defense;
    private Integer attack;
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

    public Integer getTotalFactor(Integer attackDefenseRation) {
        return ((this.attack*attackDefenseRation/100) + (this.defense*((100-attackDefenseRation)/100)));
    }

    public Player(int id, String name,Integer defense, Integer attack, boolean isPlayNextMatch){
        this.id = id;
        this.name = name;
        this.defense = defense;
        this.attack = attack;
        this.isPlayNextMatch = isPlayNextMatch;
    }


}
