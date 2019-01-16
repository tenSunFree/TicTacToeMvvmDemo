package com.home.tictactoemvvmdemo.model;

/** 記錄著使用者的名稱, 與對應的棋子種類(O or X) */
public class Player {

    public String name;
    public String value;

    public Player(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
