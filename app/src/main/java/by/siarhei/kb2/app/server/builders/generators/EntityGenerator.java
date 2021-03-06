package by.siarhei.kb2.app.server.builders.generators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import by.siarhei.kb2.app.R;
import by.siarhei.kb2.app.server.countries.Country;
import by.siarhei.kb2.app.server.entities.ArmyShop;
import by.siarhei.kb2.app.server.entities.Captain;
import by.siarhei.kb2.app.server.entities.Castle;
import by.siarhei.kb2.app.server.entities.CastleLeft;
import by.siarhei.kb2.app.server.entities.CastleRight;
import by.siarhei.kb2.app.server.entities.City;
import by.siarhei.kb2.app.server.entities.GoldenChest;
import by.siarhei.kb2.app.server.entities.GuidePost;
import by.siarhei.kb2.app.server.entities.MapNext;
import by.siarhei.kb2.app.server.entities.Metro;
import by.siarhei.kb2.app.server.entities.Spell;
import by.siarhei.kb2.app.server.models.MapPoint;

public class EntityGenerator implements Serializable {
    private static byte[] cityNamesMask;
    private static byte[] castleNamesMask;
    private final Random random;
    private final MapPoint[][] map;
    private final ArrayList<City> cities = new ArrayList<>();
    private final ArrayList<ArmyShop> armyShops = new ArrayList<>();
    private final ArrayList<Castle> castles = new ArrayList<>();
    private Spell spell;

    public EntityGenerator(MapPoint[][] map) {
        this.random = new Random();
        this.map = map;


        cityNamesMask = new byte[28];
        castleNamesMask = new byte[28];
    }

    public static void reset() {
        cityNamesMask = new byte[28];
        castleNamesMask = new byte[28];
    }

    public void citiesAndCastles() {
        updateNamesMask(castleNamesMask);
        castles();

        updateNamesMask(cityNamesMask);
        cities();
    }

    private void cities() {
        int count = 0;
        int x;
        int y;
        while (count < 5) {
            x = random.nextInt(Country.MAX_MAP_SIZE - 1);
            y = random.nextInt(Country.MAX_MAP_SIZE - 1);

            if (((map[x + 1][y].getLand() == R.drawable.water)
                    || (map[x - 1][y].getLand() == R.drawable.water)
                    || (map[x][y + 1].getLand() == R.drawable.water)
                    || (map[x][y - 1].getLand() == R.drawable.water))
                    && ((map[x][y].getLand() == R.drawable.land)
                    && (map[x][y].getEntity() == null))) {
                cities.add(createCity(map[x][y], castles.get(count)));
                count++;
            }
        }
    }

    private City createCity(MapPoint mp, Castle castle) {
        int nameId = 0;
        for (int i = 0; i < cityNamesMask.length; i++) {
            if (cityNamesMask[i] == 1) {
                nameId = i + 1;
                cityNamesMask[i] = -1;
                break;
            }
        }
        City city = new City(nameId, castle);
        mp.setEntity(city);
        return city;
    }

    public void guidePosts() {
        int count = 0;
        while (count < 5) {
            int y = random.nextInt(54) + 5;
            int x = random.nextInt(54) + 5;
            if ((map[x][y].getLand() == R.drawable.land)
                    || (map[x][y].getLand() == R.drawable.sand)) {
                map[x][y].setEntity(new GuidePost());
            }
            count++;
        }
    }

    public void goldChests(int frequency, int wealth) {
        for (int i = 5; i < Country.MAX_MAP_SIZE - 5; i++) {
            for (int j = 5; j < Country.MAX_MAP_SIZE - 5; j++) {
                if (((map[i][j].getLand() == R.drawable.land)
                        || (map[i][j].getLand() == R.drawable.sand))
                        && (map[i][j].getEntity() == null)) {
                    if (random.nextInt(frequency) == 1) {
                        map[i][j].setEntity(new GoldenChest(wealth));
                    }
                }
            }
        }
    }

    private void castles() {
        int count = 0;
        while (count < 5) {
            Castle castle = tryPlaceCastle(random.nextInt(Country.MAX_MAP_SIZE),
                    random.nextInt(Country.MAX_MAP_SIZE));
            if (castle != null) {
                castle.generateArmy(1000, 0);
                count++;
                castles.add(castle);
            }
        }
    }

    private Castle tryPlaceCastle(int x, int y) {
        if (((map[x][y].getLand() == R.drawable.land) &&
                (map[x - 1][y].getLand() == R.drawable.land) &&
                (map[x + 1][y].getLand() == R.drawable.land) &&
                (map[x][y + 1].getLand() == R.drawable.land)) &&
                ((map[x][y].getEntity() == null) &&
                        (map[x - 1][y].getEntity() == null) &&
                        (map[x + 1][y].getEntity() == null) &&
                        (map[x][y + 1].getEntity() == null))) {
            int nameId = 0;
            for (int i = 0; i < castleNamesMask.length; i++) {
                if (castleNamesMask[i] == 1) {
                    nameId = i + 1;
                    castleNamesMask[i] = -1;
                    break;
                }
            }
            Castle castle = new Castle(nameId);
            map[x][y].setEntity(castle);
            map[x - 1][y].setEntity(new CastleLeft());
            map[x + 1][y].setEntity(new CastleRight());
            tryPlaceCaptain(x, y + 1, 1000, 2000);
            return castle;
        }
        return null;
    }


    public void captains(int minAuthority, int maxAuthority) {
        int count = 0;
        while (count < 40) {
            if (tryPlaceCaptain(random.nextInt(Country.MAX_MAP_SIZE),
                    random.nextInt(Country.MAX_MAP_SIZE), minAuthority, maxAuthority))
                count++;
        }
    }

    private boolean tryPlaceCaptain(int x, int y, int minAuthority, int maxAuthority) {
        if (map[x][y].getLand() == R.drawable.land && map[x][y].getEntity() == null) {
            Captain captain = new Captain();
            map[x][y].setEntity(captain);
            int authority = minAuthority + random.nextInt(maxAuthority - minAuthority);
            captain.generateArmy(authority, 0);
            return true;
        }
        return false;
    }

    public void armies(int count, int... groups) {
        int run = 0;
        while (run < count) {
            MapPoint mp = map[random.nextInt(65)][random.nextInt(65)];
            if (mp.getEntity() == null && mp.getLand() == R.drawable.land) {
                armyShops.add(createArmy(mp, groups));
                run++;
            }
        }
    }

    private ArmyShop createArmy(MapPoint mp, int... groups) {
        ArmyShop armyShop = new ArmyShop(groups);
        mp.setEntity(armyShop);
        return armyShop;
    }

    public void mapNext() {
        getEmptyLand().setEntity(new MapNext());
    }

    public void metro() {
        getEmptyLand().setEntity(new Metro());
        getEmptyLand().setEntity(new Metro());
    }

    private MapPoint getEmptyLand() {
        int x;
        int y;
        do {
            y = random.nextInt(54) + 5;
            x = random.nextInt(54) + 5;
        } while (map[x][y].getLand() != R.drawable.land && map[x][y].getEntity() == null);
        return map[x][y];
    }

    public void updateSpell() {
        if (spell != null) {
            // TODO:
//            spell.destroy();
            spell = null;
        }
        if (Math.random() < 0.1) {
            // TODO - spell position
            spell = new Spell();
        }
    }

    private void updateNamesMask(byte[] namesMask) {
        int count0 = 0;
        for (int i = 0; i < namesMask.length; i++) {
            if (namesMask[i] == 1) {
                namesMask[i] = -1;
            }
            if (namesMask[i] == 0) {
                count0++;
            }
        }
        if (count0 < 5) {
            throw new NullPointerException();
        }
        Random rand = new Random();
        int i = 1;
        while (i < 5) {
            int n = rand.nextInt(namesMask.length);
            if (namesMask[n] == 0) {
                namesMask[n] = 1;
                i++;
            }
        }
    }

//    @Override
//    public Iterator<City> getCities() {
//        ArrayList<Iterator<City>> iterators = new ArrayList<>();
//        for (CitiesOwner city : cities) {
//            iterators.add(city.getCities());
//        }
//        return new EntityIterator<>(iterators);
//    }
//
//    @Override
//    public Iterator<ArmyShop> getArmyShops() {
//        ArrayList<Iterator<ArmyShop>> iterators = new ArrayList<>();
//        for (ArmyShopsOwner armyShop : armyShops) {
//            iterators.add(armyShop.getArmyShops());
//        }
//        return new EntityIterator<>(iterators);
//    }
}
