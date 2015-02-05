package com.neschur.kb2.app.entities;

import com.neschur.kb2.app.R;
import com.neschur.kb2.app.models.MapPoint;
import com.neschur.kb2.app.models.iterators.Cities;

import java.util.Iterator;

public class City extends EntityImpl implements Cities {
    private final int[] workers = new int[4];

    public City(MapPoint point) {
        super(point);
        workers[0] = 4;
        workers[1] = 5;
        workers[2] = 6;
        workers[3] = 1;
    }

    @Override
    public int getID() {
        return R.drawable.city;
    }

    public int getWorkers(int id) {
        return workers[id];
    }

    public void changeWorkers(int id, int count) {
        workers[id] += count;
    }

    @Override
    public Iterator<City> getCities() {
        final City self = this;

        return new Iterator<City>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public City next() {
                hasNext = false;
                return self;
            }

            @Override
            public void remove() {}
        };

    }
}
