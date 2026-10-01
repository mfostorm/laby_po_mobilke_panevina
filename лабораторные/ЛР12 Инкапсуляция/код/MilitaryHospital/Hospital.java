package com.example.militaryhospital;

public class Hospital {

    void healSoldier(Soldier soldierToHeal) {
        int health = soldierToHeal.getHealth();   // получаем через геттер
        health = health + 15;
        soldierToHeal.setHealth(health);          // возвращаем через сеттер
    }
}
