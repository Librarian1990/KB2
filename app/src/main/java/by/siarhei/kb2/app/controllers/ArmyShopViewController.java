package by.siarhei.kb2.app.controllers;

import by.siarhei.kb2.app.server.entities.ArmyShop;

public interface ArmyShopViewController extends ViewController, PlayerViewsController {
    void buyArmy(ArmyShop shop, int count);
}
