package by.siarhei.kb2.app.server;

import by.siarhei.kb2.app.server.entities.ArmyShop;
import by.siarhei.kb2.app.server.entities.City;
import by.siarhei.kb2.app.server.models.Player;
import by.siarhei.kb2.app.server.warriors.Warrior;
import by.siarhei.kb2.app.ui.menus.Menu;
import by.siarhei.kb2.app.ui.messages.Message;

public class ServerView {
    private transient GameGrid gameGrid;
    private int viewMode;
    private transient Message message;
    private transient Menu menu;
    private int money;
    private int authority;
    private int salary;
    private Warrior updatedWarrior;
    private City updatedCity;

    private transient Game game;
    private transient GameDispatcher gameDispatcher;
    private transient Player player;
    private ArmyShop armyShop;

    public ServerView(Game game, GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.game = game;
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }

    public int getViewMode() {
        return viewMode;
    }

    public Message getMessage() {
        return message;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getMoney() {
        return money;
    }

    public void refresh() {
        gameGrid = new GameGrid(game);
        gameGrid.setMode(gameDispatcher.getGameMenuMode());
        gameGrid.update();
        menu = gameDispatcher.getMenu();
        message = gameDispatcher.getMessage();
        viewMode = gameDispatcher.getViewMode();
        money = game.getPlayer().getMoney();
        updatedWarrior = game.getUpdatedWarrior();
        updatedCity = game.getUpdatedCity();
        authority = game.getPlayer().getAuthority();
        salary = game.getPlayer().getSalary();
        armyShop = gameDispatcher.getCurrentArmyShop();
        player = game.getPlayer();
    }

    public int getAuthority() {
        return authority;
    }

    public Warrior getUpdatedWarrior() {
        return updatedWarrior;
    }

    public City getUpdatedCity() {
        return updatedCity;
    }

    public int getSalary() {
        return salary;
    }

    public Player getPlayer() {
        return player;
    }

    public ArmyShop getArmyShop() {
        return armyShop;
    }
}
