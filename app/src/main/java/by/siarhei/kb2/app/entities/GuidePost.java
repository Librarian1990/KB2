package by.siarhei.kb2.app.entities;

import by.siarhei.kb2.app.R;
import by.siarhei.kb2.app.models.MapPoint;

public class GuidePost extends EntityImpl {

    public GuidePost(MapPoint point) {
        super();
    }

    @Override
    public int getID() {
        return R.drawable.guidepost;
    }
}
