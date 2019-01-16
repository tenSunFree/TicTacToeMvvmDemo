package com.home.tictactoemvvmdemo.model;


import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import static com.home.tictactoemvvmdemo.utilities.StringUtility.isNullOrEmpty;

public class Game {

    private static final String TAG = Game.class.getSimpleName();
    private static final int BOARD_SIZE = 3;

    public Player player1;
    public Player player2;

    public Player currentPlayer;
    public Cell[][] cells;

    /**
     * 使用MutableLiveData來保存想要監視的數據, 然後再將MutableLiveData轉成LiveData, 以便在GameActivity提供observe()
     */
    public MutableLiveData<Player> winner = new MutableLiveData<>(); // MutableLiveData是方便我們使用的LiveData子類別, 提供setValue()和postValue()兩種方式更新value

    public Game(String playerOne, String playerTwo) {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE]; // 創建一個3*3的二維陣列空間, 用來儲存管理整個九宮格的每個格子的相關狀態
        player1 = new Player(playerOne, "★"); // 初始化第一位玩家的名稱與棋子類型
        player2 = new Player(playerTwo, "✿"); // 初始化第二位玩家的名稱與棋子類型
        currentPlayer = player1; // 設置由第一位玩家先手
    }

    /**
     * 判斷遊戲是否已經結束, 可分成兩種
     * 1. 連成一條線, 有勝利者
     * 2. 九個格子都點過了, 無勝利者, 平局
     */
    public boolean hasGameEnded() {
        if (hasThreeSameHorizontalCells() || hasThreeSameVerticalCells() || hasThreeSameDiagonalCells()) { // 如果有連成一條線, 表示當前玩家勝利
            winner.setValue(currentPlayer); // 對winner持有的數據進行賦值, 會進而調用GameActivity的onGameWinnerChanged
            return true;
        }
        if (isBoardFull()) { // 如果九宮格的每個格子都點擊過了, 表示平局
            winner.setValue(null); // 對winner持有的數據進行賦值, 會進而調用GameActivity的onGameWinnerChanged
            return true;
        }
        return false;
    }

    public boolean hasThreeSameVerticalCells() {
        try {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (areEqual(cells[0][i], cells[1][i], cells[2][i])) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public boolean hasThreeSameHorizontalCells() {
        try {
            for (int i = 0; i < BOARD_SIZE; i++)
                if (areEqual(cells[i][0], cells[i][1], cells[i][2]))
                    return true;
            return false;
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public boolean hasThreeSameDiagonalCells() {
        try {
            return areEqual(cells[0][0], cells[1][1], cells[2][2]) ||
                    areEqual(cells[0][2], cells[1][1], cells[2][0]);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 判斷九宮格上的格子是否都已經賦值了, 如果是, 則表示平局
     */
    public boolean isBoardFull() {
        for (Cell[] row : cells)
            for (Cell cell : row)
                if (cell == null || cell.isEmpty())
                    return false;
        return true;
    }

    /**
     * 2 cells are equal if:
     * - Both are none null
     * - Both have non null values
     * - both have equal values
     *
     * @param cells: Cells to check if are equal
     * @return
     */
    private boolean areEqual(Cell... cells) {
        if (cells == null || cells.length == 0)
            return false;
        for (Cell cell : cells)
            if (cell == null || isNullOrEmpty(cell.player.value))
                return false;
        Cell comparisonBase = cells[0];
        for (int i = 1; i < cells.length; i++)
            if (!comparisonBase.player.value.equals(cells[i].player.value))
                return false;
        return true;
    }

    /**
     * 切換當前的使用者, 如果是player1, 就會成player2; 否則反之
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    public void reset() {
        player1 = null;
        player2 = null;
        currentPlayer = null;
        cells = null;
    }
}
