package com.home.tictactoemvvmdemo.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import com.home.tictactoemvvmdemo.R;
import com.home.tictactoemvvmdemo.databinding.ActivityGameBinding;
import com.home.tictactoemvvmdemo.model.Player;
import com.home.tictactoemvvmdemo.viewmodel.GameViewModel;

import static com.home.tictactoemvvmdemo.utilities.StringUtility.isNullOrEmpty;

public class GameActivity extends AppCompatActivity {

    private static final String GAME_BEGIN_DIALOG_TAG = "game_dialog_tag"; // 提供dialog.show()的參數
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag"; // 提供dialog.show()的參數
    private static final String NO_WINNER = "No one"; //
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promptForPlayers();
    }

    public void promptForPlayers() {
        GameBeginDialog dialog = GameBeginDialog.newInstance(this); // 傳入activity, 是因為想要直接從dialog裡面, 去調用activity的方法, 也就是activity.onPlayersSet(player1, player2)
        dialog.setCancelable(false); // 点击屏幕或物理返回键, dialog不消失
        dialog.show(getSupportFragmentManager(), GAME_BEGIN_DIALOG_TAG);
    }

    public void onPlayersSet(String player1, String player2) {
        initDataBinding(player1, player2);
    }

    private void initDataBinding(String player1, String player2) {
        ActivityGameBinding activityGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class); // 透過ViewModelProviders協助我們取得ViewModel, 其中of()的參數代表著ViewModel的生命範圍(scope)
        gameViewModel.init(player1, player2);
        activityGameBinding.setGameViewModel(gameViewModel);
        setUpOnGameEndListener();
    }

    /**
     * 添加觀察者對象來監聽LiveData所持有的数据
     * observe(), 第一个参数为LifecycleOwner, 在新的兼容库中, Activity, Fragment等都实现了LifecycleOwner接口, LifecycleOwner可以获取Lifecycle, 从而得到组件的各生命周期状态
     * observe(), 第二个参数为观察者对象, 当数据源变化时就会回调此觀察者對象
     * 類::方法
     */
    private void setUpOnGameEndListener() {
        gameViewModel.getWinner().observe(this, this::onGameWinnerChanged);
    }

    /**
     * @VisibleForTesting: 该注解只起到一个注释的作用, 告诉其他开发者被标记的代码为什么有这么大的可见程度(为了测试方便), 因此经常用来修饰public或protected, 用来修饰 private 并不会报错, 但是没有意义
     */
    @VisibleForTesting
    public void onGameWinnerChanged(Player winner) {
        String winnerName = winner == null || isNullOrEmpty(winner.name) ? NO_WINNER : winner.name;
        GameEndDialog dialog = GameEndDialog.newInstance(this, winnerName);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), GAME_END_DIALOG_TAG);
    }
}
