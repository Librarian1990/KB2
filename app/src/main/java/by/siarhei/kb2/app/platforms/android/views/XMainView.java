package by.siarhei.kb2.app.platforms.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import by.siarhei.kb2.app.R;
import by.siarhei.kb2.app.controllers.MainViewController;
import by.siarhei.kb2.app.controllers.implementations.MainViewControllerImpl;
import by.siarhei.kb2.app.platforms.android.views.helpers.Click;
import by.siarhei.kb2.app.platforms.android.views.helpers.Painter;
import by.siarhei.kb2.app.server.GameGrid;
import by.siarhei.kb2.app.server.Request;
import by.siarhei.kb2.app.server.Server;
import by.siarhei.kb2.app.server.ServerView;
import by.siarhei.kb2.app.server.Game;
import by.siarhei.kb2.app.server.models.TrainingData;
import by.siarhei.kb2.app.ui.menus.Menu;

public class XMainView extends ViewImpl {
    private final MainViewController mainViewController;
    private boolean trainingMode = false;
//    private int trainingStep = 0;

    public XMainView(Context context, MainViewControllerImpl mainViewController) {
        super(context, mainViewController);
        this.mainViewController = mainViewController;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        ServerView serverView = Server.getView();
        if(serverView.getViewMode() == Game.VIEW_MODE_GRID) {
// TODO
//        if(trainingMode) {
//            trainingMode = false;
////            trainingStep++;
//            refresh();
//            super.onTouchEvent(event);
//        }

            Click click = getClick(event);
            int x = click.getX();
            int y = click.getY();
            if (x > this.stepX() * 5) {
                int item = (y / stepY());
                mainViewController.touchMenu(item);
            } else {
                int height_2_5 = stepY() * 2;
                int height_3_5 = stepY() * 3;
                int width_2_5 = stepX() * 2;
                int width_3_5 = stepX() * 3;
                if (y > height_3_5) {
                    if (x > width_2_5 && x < width_3_5) {
                        mainViewController.touchDown();
                    }
                    if (x < width_2_5) {
                        mainViewController.touchDownLeft();
                    }
                    if (x > width_3_5) {
                        mainViewController.touchDownRight();
                    }
                }
                if (y < height_2_5) {
                    if (x > width_2_5 && x < width_3_5) {
                        mainViewController.touchUp();
                    }
                    if (x < width_2_5) {
                        mainViewController.touchUpLeft();
                    }
                    if (x > width_3_5) {
                        mainViewController.touchUpRight();
                    }
                }
                if (x > width_3_5 && y > height_2_5 && y < height_3_5) {
                    mainViewController.touchRight();
                }
                if (x < width_2_5 && y > height_2_5 && y < height_3_5) {
                    mainViewController.touchLeft();
                }
            }
        }

        if(serverView.getViewMode() == Game.VIEW_MODE_MESSAGE) {
            System.out.println("OK-------------------------");
            Request request = new Request();
            request.setAction(Request.ACTION_OK);
            Server.getServer().request(request);
        }

        if(serverView.getViewMode() == Game.VIEW_MODE_MENU) {
            Menu menu = Server.getView().getMenu();
            double y = event.getY();
            int item = (int) y / menuItemHeight();
            boolean result = false;
            Request request = new Request();

            if (item < menu.getCount()) {
                request.setAction(Request.ACTION_SELECT);
                request.setMenuItem(item);
//                result = menu.select(item);
//                refresh();
            }
            if (menu.withExit()) {
//                request.setAction(Request.ACTION_EXIT);
                // TODO: check logic
//                if (item == menu.getCount() || result)
//                    if (menu.getMenuMode() > 0) {
//                        menu.resetMenuMode();
//                        refresh();
//                    } else {
//                        viewController.viewClose();
//                    }
            } else {
                request.setAction(Request.ACTION_EXIT);
//                if (result)
//                    viewController.viewClose();
            }
            Server.getServer().request(request);
        }

        refresh();
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        ServerView serverView = Server.getView();
        if(serverView.getViewMode() == Game.VIEW_MODE_GRID) {
            Painter painter = getPainter(canvas);
            canvas.drawColor(Color.BLACK);

            GameGrid grid = mainViewController.getGameGrid();

            for (int x = 0; x < GameGrid.STEP_X; x++) {
                for (int y = 0; y < GameGrid.STEP_Y; y++) {
                    if (x < 5) {
                        painter.drawBitmap(getImageCache().getImage(grid.getBackgroundBuyXY(x, y)),
                                stepX() * x, stepY() * y);
                    }
                    if (grid.getImageBuyXY(x, y) > -1) {
                        painter.drawBitmap(getImageCache().getImage(grid.getImageBuyXY(x, y)),
                                stepX() * x, stepY() * y);
                    }
                }
            }

            showTraining(canvas);
        }

        if(serverView.getViewMode() == Game.VIEW_MODE_MESSAGE) {
            int realWidth = stepX() * 6;
            Painter painter = getPainter(canvas);
            canvas.drawColor(Color.BLACK);

            String text = serverView.getMessage().getText();
            float textLength = getDefaultPaint().measureText(text);
            if (textLength < realWidth ) {
                painter.drawText(text, 10, 100, getDefaultPaint());
            } else {
                int count = (int) (text.length() / (textLength / realWidth ));
                int length = (text.length() + count - 1) / count;
                for (int ix = 0, pos = 0; ix < length; ix++, pos += count) {
                    painter.drawText(text.substring(pos, Math.min(text.length(), pos + count)).trim()
                            , 10, 100 * (ix + 1), getDefaultPaint());
                }
            }
        }

        if(serverView.getViewMode() == Game.VIEW_MODE_MENU) {
            Painter painter = getPainter(canvas);
            canvas.drawColor(Color.BLACK);

            Menu menu = serverView.getMenu();
            int i;
            for (i = 0; i < menu.getCount(); i++) {
                painter.drawText(menu.getItemDescription(i), 10,
                        textHeight() + menuItemHeight() * i, getDefaultPaint());
            }
            if (menu.withExit())
                painter.drawText(i18n.translate(R.string.mainMenu_exit), 10,
                        textHeight() + menuItemHeight() * i, getDefaultPaint());
            if (menu.withMoney()) {
                // TODO
//                String money = i18n.translate(R.string.player_attrs_money) + ": "
//                        + viewController.getGame().getPlayer().getMoney();
//                painter.drawText(money,
//                        getDefaultPaint().measureText(money) + 1,
//                        textHeight() + menuItemHeight() * i, getDefaultPaint(), Painter.ALIGN_RIGHT);
            }
        }
    }

    private void showTraining(@NonNull Canvas canvas) {
        if(!mainViewController.isTrainingMode()){
            return;
        }
        Painter painter = getPainter(canvas);
        TrainingData trainingData = mainViewController.getTrainingData();

        if(!trainingData.citiesDid()) {
            GameGrid grid = mainViewController.getGameGrid();
            for (int x = 0; x < GameGrid.STEP_X; x++) {
                for (int y = 0; y < GameGrid.STEP_Y; y++) {
                    if(grid.getImageBuyXY(x, y) == R.drawable.city) {
                        painter.drawTrainingCircle(x, y);
                    }
                }
            }

            drawTrainingText(painter, R.string.training_cities);

            trainingData.doneCities();
            trainingMode = true;
        }


        if(!trainingData.step2Did()) {
            painter.drawTrainingCircle(0, 0);
            painter.drawTrainingCircle(4, 4);
            painter.drawTrainingCircle(4, 0);
            painter.drawTrainingCircle(0, 4);

            drawTrainingText(painter, R.string.training_moving);

            trainingData.doneStep2();
            trainingMode = true;
        }

        if(!trainingData.step1Did()) {
            painter.drawTrainingCircle(0, 2);
            painter.drawTrainingCircle(2, 0);
            painter.drawTrainingCircle(4, 2);
            painter.drawTrainingCircle(2, 4);

            drawTrainingText(painter, R.string.training_moving);

            trainingData.doneStep1();
            trainingMode = true;
        }
    }

    private void drawTrainingText(Painter painter, int textId) {
        Paint textPaint = getDefaultPaint();
        int helpTextHeight = 40;
        textPaint.setTextSize(helpTextHeight);
        String text = i18n.translate(textId);
        List<String> textArray = splitText(textPaint, text);

        Paint paint = new Paint(getDefaultPaint());
        paint.setColor(Color.GRAY);
        paint.setTextSize(5);
        painter.drawRect(10, 10,
                stepX() * 5 - 10,
                (helpTextHeight + 20) * (textArray.size() + 1) - 20, paint);

        int y = 0;
        for (String line : textArray) {
            y++;
            painter.drawText(line, 20, (helpTextHeight + 20) * y, textPaint);
        }
    }

    private List<String> splitText(Paint paint, String text) {
        float textWidth = paint.measureText(text);
        float screenWidth = stepX() * 5;
        List<String> result = new ArrayList<String>();
        String[] words = text.split(" ");

        int i = 0;
        while(i < words.length) {
            String newLine = new String();
            boolean x = paint.measureText(newLine + words[i] + " ") < screenWidth;
            while (i < words.length && paint.measureText(newLine + words[i] + " ") < screenWidth) {
                newLine = newLine + words[i] + " ";
                i++;
            }
            if(newLine == "") {
                newLine = words[i];
                i++;
            }
            result.add(newLine);
        }
        return result;
    }
}
