package by.siarhei.kb2.app.platforms.android.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import by.siarhei.kb2.app.R;
import by.siarhei.kb2.app.controllers.ViewController;
import by.siarhei.kb2.app.platforms.android.helpers.ImageCache;
import by.siarhei.kb2.app.platforms.android.helpers.Painter;
import by.siarhei.kb2.app.ui.menus.GoldChestMenu;
import by.siarhei.kb2.app.ui.menus.Menu;

class Menu2View extends ViewImpl {
    private final GoldChestMenu menu;

    public Menu2View(Context context, ViewController viewController, Menu menu) {
        super(context, viewController);
        this.menu = (GoldChestMenu)menu;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Painter painter = getPainter(null);
        double y = event.getY();
        double x = event.getX();
        int item = -1;
        if(x > painter.getXOffset() && y > stepY() * 2 && y < stepY() * 3) {
            item = ((int) x - painter.getXOffset()) / stepX();
        }
        select(item);
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        Painter painter = getPainter(canvas);
        canvas.drawColor(Color.BLACK);

        painter.drawText(menu.getTitle(), 0,
                textHeight(), getDefaultPaint(), Painter.ALIGN_CENTER);

        ImageCache imageCache = getImageCache();//ImageCache.getInstance(getResources(), (int)(stepX() * 1.5), (int)(stepY()*1.5));

        Paint paint = new Paint(getDefaultPaint());
        paint.setTextSize(textHeight() / 2);

        int i;
        for (i = 0; i < menu.getCount(); i++) {
            painter.drawText(menu.getItemDescription(i), stepX() * i, stepY() * 2 - textHeight() / 1.5f, paint);
            Bitmap image = imageCache.getImage(menu.getItemImageId(i));
            painter.drawBitmap(image, stepX() * i, stepY() * 2);
        }
        if(menu.withExit()) {
            String text = i18n.translate("mainMenu_exit");
            painter.drawText(text , stepX() * (i + 1) - paint.measureText(text), stepY() * 2 - textHeight() / 1.5f, paint);
            Bitmap image = imageCache.getImage(R.drawable.status_back);
            painter.drawBitmap(image, stepX() * i, stepY() * 2);
        }
    }

    private void select(int item) {
        boolean result = false;
        if (item < menu.getCount() && item > -1) {
            result = menu.select(item);
            refresh();
        }
        if (menu.withExit()) {
            if (item == menu.getCount() || result)
                if (menu.getMenuMode() > 0) {
                    menu.resetMenuMode();
                    refresh();
                } else {
                    viewController.viewClose();
                }
        } else {
            if (result)
                viewController.viewClose();
        }
    }

}
