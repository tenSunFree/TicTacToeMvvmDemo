package com.home.tictactoemvvmdemo.model;


import com.home.tictactoemvvmdemo.utilities.StringUtility;

/** 每一個格子 */
public class Cell {

    public Player player;

    public Cell(Player player) {
        this.player = player;
    }

    public boolean isEmpty() {
        return player == null || StringUtility.isNullOrEmpty(player.value);
    }
}
