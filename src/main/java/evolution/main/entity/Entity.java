package evolution.main.entity;

import evolution.GLDisplay.GLHelper;
import com.jogamp.opengl.GL2;

import static evolution.main.Utility.CELL_SIZE;

public abstract class Entity {
    public boolean isEmpty() {
        return false;
    }

    public boolean isWall() {
        return false;
    }

    public boolean isFood() {
        return false;
    }

    public boolean isPoison() {
        return false;
    }

    public boolean isUnit() {
        return this instanceof Unit;
    }

    public Unit getUnit() {
        return (Unit) this;
    }

    public int value() {
        if (isUnit()) return 3;
        switch (((StaticEntity) this).getSet()) {
            case EMPTY:
                return 5;
            case WALL:
                return 2;
            case FOOD:
                return 4;
            case POISON:
                return 1;
        }
        return 0;
    }

    public void draw(GL2 gl, int x, int y) {
        GLHelper.drawRect(gl, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, true);
    }
}
