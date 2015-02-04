package com.neschur.kb2.app.controllers.implementations;

import com.neschur.kb2.app.controllers.ActivateCallback;
import com.neschur.kb2.app.controllers.ApplicationController;
import com.neschur.kb2.app.controllers.MainViewController;
import com.neschur.kb2.app.entities.Entity;
import com.neschur.kb2.app.entities.Fighting;
import com.neschur.kb2.app.models.GameGrid;
import com.neschur.kb2.app.View;

public class MainViewControllerImpl extends ApplicationController implements MainViewController,
        ActivateCallback {
    private final View view;
    private GameGrid gameGrid;

    public MainViewControllerImpl() {
        view = getViewFactory().getMainView(this);
        setContentView(view);
    }

    @Override
    public GameGrid getGameGrid() {
        if (gameGrid == null)
            gameGrid = new GameGrid(getGame());
        gameGrid.update();
        return gameGrid;
    }

    @Override
    public void activateEntity(Entity entity) {
        new PlayerViewsControllerImpl(entity);
    }

    @Override
    public void activateBattle(Fighting fighting) {
        setContentView(getViewFactory().getWorkersMenuView(this));
        new BattleControllerImpl(this, fighting);
    }

    @Override
    public void touchDown() {
        playerMove(0, +1);
    }

    @Override
    public void touchUp() {
        playerMove(0, -1);
    }

    @Override
    public void touchRight() {
        playerMove(+1, 0);
    }

    @Override
    public void touchLeft() {
        playerMove(-1, 0);
    }

    @Override
    public void touchUpRight() {
        playerMove(+1, -1);
    }

    @Override
    public void touchUpLeft() {
        playerMove(-1, -1);
    }

    @Override
    public void touchDownRight() {
        playerMove(+1, +1);
    }

    @Override
    public void touchDownLeft() {
        playerMove(-1, +1);
    }

    @Override
    public void touchMenu(int i) {
        GameGrid grid = getGameGrid();
        switch (grid.getMode()) {
            case 0:
                switch (i) {
                    case 0:
                        grid.setMode(1);
                        view.refresh();
                        break;
                    case 1:
                        setContentView(getViewFactory().getWorkersMenuView(this));
                        break;
                    case 2:
                        grid.setMode(2);
                        view.refresh();
                        break;
                    case 3:
                        new PlayerViewsControllerImpl("status");
                        grid.setMode(0);
                        break;
                    case 4:
                        grid.setMode(3);
                        view.refresh();
                        break;
                }
                break;
            case 1:
                switch (i) {
                    case 0:
                        new PlayerViewsControllerImpl("army");
                        grid.setMode(0);
                        break;
                    case 4:
                        grid.setMode(0);
                        view.refresh();
                        break;
                }
                break;
            case 2:
                switch (i) {
                    case 0:
                        new PlayerViewsControllerImpl("magic");
                        grid.setMode(0);
                        break;
                    case 4:
                        grid.setMode(0);
                        view.refresh();
                        break;
                }
                break;
            case 3:
                switch (i) {
                    case 0:
                        new PlayerViewsControllerImpl("map");
                        break;
                    case 1:
                        if (getGame().getPlayer().inNave()) {
                            setContentView(getViewFactory().getCountryMenuView(this));
                        }
                        break;
                    case 2:
                        grid.setMode(0);
                        view.refresh();
                        break;
                }
                break;
        }
    }

    @Override
    public void viewClose() {
        setContentView(view);
    }

    private void playerMove(int dx, int dy) {
        if (getGame().move(dx, dy))
            view.refresh();
    }
}