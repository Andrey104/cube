package evolution.main.entity;

import evolution.GLDisplay.GLHelper;
import com.jogamp.opengl.GL2;

import java.util.HashMap;
import java.util.Map;

public final class StaticEntity extends Entity {
    private static Map<StaticEntityType, StaticEntity> instances = new HashMap<>();

    static {
        for (StaticEntityType set : StaticEntityType.values()) {
            instances.put(set, new StaticEntity(set));
        }
    }

    public static StaticEntity instance(StaticEntityType set) {
        return instances.get(set);
    }

    private final StaticEntityType set;

    private StaticEntity(StaticEntityType set) {
        this.set = set;
    }

    public StaticEntityType getSet() {
        return set;
    }

    @Override
    public boolean isEmpty() {
        return set == StaticEntityType.EMPTY;
    }

    @Override
    public boolean isWall() {
        return set == StaticEntityType.WALL;
    }

    @Override
    public boolean isFood() {
        return set == StaticEntityType.FOOD;
    }

    @Override
    public boolean isPoison() {
        return set == StaticEntityType.POISON;
    }

    @Override
    public void draw(GL2 gl, int x, int y) {
        switch (set) {
            case EMPTY:
                GLHelper.setColor(gl, 0.8, 0.8, 0.8);
                break;
            case WALL:
                GLHelper.setColor(gl, 0.2, 0.2, 0.2);
                break;
            case FOOD:
                GLHelper.setColor(gl, 0.2, 0.7, 0.2);
                break;
            case POISON:
                GLHelper.setColor(gl, 0.9, 0.2, 0.2);
                break;
        }
        super.draw(gl, x, y);
    }
}
