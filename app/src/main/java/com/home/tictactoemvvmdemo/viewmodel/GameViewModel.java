package com.home.tictactoemvvmdemo.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayMap;
import com.home.tictactoemvvmdemo.model.Cell;
import com.home.tictactoemvvmdemo.model.Game;
import com.home.tictactoemvvmdemo.model.Player;

import static com.home.tictactoemvvmdemo.utilities.StringUtility.stringFromNumbers;

public class GameViewModel extends ViewModel {

    private static final int TEXT_COLOR_VALUE = 0xFF272424;
    public ObservableArrayMap<String, String> cells; // 使用key-value來存取數據
    public ObservableArrayMap<String, Integer> cell2s; // 使用key-value來存取數據
    private Game game;

    public void init(String player1, String player2) {
        game = new Game(player1, player2);
        cells = new ObservableArrayMap<>();
        cell2s = new ObservableArrayMap<>();
        for (int row = 0; row < 3; row++) { // 初始化每個TextView, 讓每個格子大小固定
            for (int column = 0; column < 3; column++) {
                cells.put(stringFromNumbers(row, column), "？");
            }
        }
    }

    /**
     * 當點擊九宮格的其中一個格子時, 就會先判斷該格子是否有賦值, 如果沒有的賦值的話, 才執行以下內容
     * 1. 將該格子賦值
     * 2. 透過cells.put(), 將該Key添加上對應的Value
     * 3. 判斷遊戲是否已經結束, 執行對應的行為
     */
    public void onClickedCellAt(int row, int column) {
        if (game.cells[row][column] == null) {
            game.cells[row][column] = new Cell(game.currentPlayer);
            cells.put(stringFromNumbers(row, column), game.currentPlayer.value); // 設定數值
            cell2s.put(stringFromNumbers(row, column), TEXT_COLOR_VALUE); // 設定顏色
            if (game.hasGameEnded()) // 如果遊戲已經結束
                game.reset();
            else
                game.switchPlayer(); // 切換當前的使用者
        }
    }

    /**
     * 轉成LiveData, 提供給GameActivity使用
     */
    public LiveData<Player> getWinner() {
        return game.winner;
    }
}
