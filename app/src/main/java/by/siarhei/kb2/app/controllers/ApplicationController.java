package by.siarhei.kb2.app.controllers;

import by.siarhei.kb2.app.I18n;
import by.siarhei.kb2.app.Storage;
import by.siarhei.kb2.app.View;
import by.siarhei.kb2.app.ViewFactory;
import by.siarhei.kb2.app.server.Server;

public abstract class ApplicationController implements ViewController {
    private static PlatformController platformController;
    private static Storage storage;
    private static ViewFactory viewFactory;
    private static Server server;
    private static I18n i18n;

    protected ApplicationController() {
        if (viewFactory == null)
            viewFactory = platformController.getViewFactory(this);
    }

    public static void initApp(PlatformController _platformController) {
        platformController = _platformController;
        storage = _platformController.getStorage();
        i18n = _platformController.getI18n();
    }

    @Override
    public ViewFactory getViewFactory() {
        return viewFactory;
    }

//    @Override
    public Server getServer() {
        return server;
    }

    protected void setServer(Server server) {
        ApplicationController.server = server;
    }

    @Override
    public void setContentView(View view) {
        platformController.setContentView(view);
    }

    protected Storage getStorage() {
        return storage;
    }

    public void exit() {
        platformController.exit();
    }

    @Override
    public I18n i18n() {
        return i18n;
    }
}
