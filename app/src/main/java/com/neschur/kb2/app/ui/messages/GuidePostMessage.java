package com.neschur.kb2.app.ui.messages;

import com.neschur.kb2.app.I18n;
import com.neschur.kb2.app.controllers.GameController;
import com.neschur.kb2.app.entities.Entity;

import java.util.Random;

public class GuidePostMessage extends Message {
    private static int COUNT = 12;

    GuidePostMessage(Entity entity, GameController gameController) {
        super(entity, gameController);
    }

    @Override
    public String getText() {
        return I18n.translate("entity_guidePost_message" + (new Random()).nextInt(COUNT));
    }

    @Override
    public void action() {

    }
}
