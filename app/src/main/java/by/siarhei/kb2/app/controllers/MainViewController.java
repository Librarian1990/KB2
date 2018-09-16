package by.siarhei.kb2.app.controllers;

import by.siarhei.kb2.app.server.GameGrid;
import by.siarhei.kb2.app.server.models.TrainingData;

public interface MainViewController extends ViewController {
    GameGrid getGameGrid();

    void touchMenu(int item);

    void touchUp();

    void touchUpLeft();

    void touchUpRight();

    void touchDown();

    void touchDownLeft();

    void touchDownRight();

    void touchRight();

    void touchLeft();

    boolean isTrainingMode();

    TrainingData getTrainingData();
}
