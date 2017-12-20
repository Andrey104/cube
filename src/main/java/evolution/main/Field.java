package evolution.main;


import com.jogamp.opengl.GL2;
import evolution.main.entity.Entity;
import evolution.main.entity.StaticEntity;
import evolution.main.entity.StaticEntityType;
import evolution.main.entity.Unit;

import java.util.List;

import static evolution.main.Utility.*;

public class Field {

    private Entity[][] field = new Entity[WIDTH][HEIGHT];

    public void createField(List<Unit> units) {
        createField();
        placeUnits(units);
        placeObjects();
    }

    private void createField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1) {
                    field[x][y] = StaticEntity.instance(StaticEntityType.WALL);
                } else {
                    field[x][y] = StaticEntity.instance(StaticEntityType.EMPTY);
                }
            }
        }
        createWalls();
    }

    private void createWalls() {
        //TODO implement
    }

    private void placeObjects() {
        for (int i = 0; i < OBJECTS_COUNT; i++) {
            placeObject();
        }
    }

    public void placeObject() {
        int x = 0;
        int y = 0;
        while (!field[x][y].isEmpty()) {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
        }
        field[x][y] = StaticEntity.instance(
                random.nextBoolean() ? StaticEntityType.FOOD : StaticEntityType.POISON);
    }

    private void placeUnits(List<Unit> units) {
        for (Unit unit : units) {
            placeUnit(unit);
        }
    }

    private void placeUnit(Unit unit) {
        int x = 0;
        int y = 0;
        while (!field[x][y].isEmpty()) {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
        }
        field[x][y] = unit;
        unit.setPosition(x, y);
    }

    public Entity getEntity(int x, int y) {
        return field[x][y];
    }

    public void display(GL2 gl) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (field[x][y] != null)
                    field[x][y].draw(gl, x, y);
            }
        }
    }

    public void setEntity(int x, int y, Entity entity) {
        field[x][y] = entity;
    }
}
