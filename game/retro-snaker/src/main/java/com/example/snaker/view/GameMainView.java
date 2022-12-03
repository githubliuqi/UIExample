package com.example.snaker.view;

import android.content.Context;
import android.graphics.Point;
import android.util.Size;

import com.example.common.utils.AppUtils;
import com.example.juiexample.common.grid.GridItemView;
import com.example.juiexample.common.grid.GridView;
import com.example.juiexample.event.EventHandler;
import com.example.snaker.model.GameDef;
import com.example.snaker.model.GameOnUpdateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMainView extends GridView {

    private static final String TAG = "GameMainView";

    private boolean gameOver = false;
    private final Snaker snakerView;
    private final List<FoodView> foodViewList = new ArrayList<>();
    private final Random RANDOM = new Random();
    private final Size gridSize = new Size(AppUtils.dip2px(20), AppUtils.dip2px(20));
    private final GameOnUpdateListener gridTypeListener = new GameOnUpdateListener();

    public GameMainView(Context context) {
        super(context);
        setGridSize(gridSize);
        setGridTypes(new int[20][16]);
        snakerView = new Snaker(context, this, gridSize);
        snakerView.setHeadPoint(new Point(5, 5));

        for (int i = 0; i < 10; i++) {
            FoodView foodView = new FoodView(context);
            foodView.setOnUpdateListener(gridTypeListener);
            foodView.setSize(gridSize);
            foodView.setGridType(2);
            resetFoodView(foodView);
            addView(foodView);
            foodViewList.add(foodView);
        }
    }

    @Override
    protected GridItemView getGridItemView() {
        GameGridItemView itemView = new GameGridItemView(context);
        itemView.setOnUpdateListener(gridTypeListener);
        itemView.setGridType(0);
        return itemView;
    }

    public void moveSnaker() {
        if (gameOver) {
            return;
        }
        gameOver = !snakerView.move();
        if (gameOver) {
            EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_OVER, null);
            return;
        }
        for (int i = 0; i < foodViewList.size(); i++) {
            FoodView foodView = foodViewList.get(i);
            if (snakerView.getHeadPoint().equals(foodView.getPoint())) {
                GameGridItemView view = new GameGridItemView(context);
                view.setGridType(1);
                snakerView.addNodeView(view);
                resetFoodView(foodView);
                EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SCORE_UPDATE, null);
            }
        }
    }

    private void resetFoodView(FoodView foodView) {
        Point newPoint = new Point();
        while (true) {
            int randx = RANDOM.nextInt(getColumnCount());
            int randy = RANDOM.nextInt(getRowCount());
            newPoint.x = randx;
            newPoint.y = randy;
            if (snakerView.overLapPoint(newPoint)) {
                continue;
            }
            int x = foodViewList.indexOf(foodView);
            boolean overLapPoint = false;
            for (int i = 0; i < foodViewList.size() && i != x; i++) {
                FoodView view = foodViewList.get(i);
                if (view != null && view.getPoint().equals(newPoint)) {
                    overLapPoint = true;
                    break;
                }
            }
            if (overLapPoint) {
                continue;
            } else {
                break;
            }
        }
        foodView.setPoint(newPoint);
        foodView.setRandColor();
    }

    public GameMainView setMoveDirection(GameDef.MoveType moveType) {
        snakerView.setMoveDirection(moveType);
        return this;
    }
}
