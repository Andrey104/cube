package evolution.main.entity;

import com.jogamp.opengl.GL2;
import evolution.GLDisplay.GLHelper;
import evolution.main.Field;
import evolution.main.Utility;

import java.util.Arrays;

import static evolution.main.Utility.*;

public class Unit extends Entity {
    private int x;
    private int y;
    private int direction;
    private int health = START_HEALTH;
    private int lifetime = 0;

    private int[] code = new int[CODE_LENGTH];
    private int counter = 0;

    public Unit() {
        for (int i = 0; i < code.length; i++) {
            code[i] = random.nextInt(INSTRUCTION_COUNT);
        }
    }

    private Unit(int[] code) {
        assert code.length == this.code.length;
        System.arraycopy(code, 0, this.code, 0, code.length);
    }

    public Unit copy() {
        return new Unit(code);
    }

    public void mutate() {
        for (int i = 0; i < code.length; i++) {
            if (Utility.random.nextDouble() < MUTATION_PROBABILITY) {
                code[i] = Utility.random.nextInt(INSTRUCTION_COUNT);
            }
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int[] getCode() {
        return code;
    }

    private void die() {
        health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    private void tick() {
        health--;
        lifetime++;
    }

    private void eat() {
        health += EAT_VALUE;
    }

    private void inc(int value) {
        counter += value;
        if (counter >= CODE_LENGTH) {
            counter %= CODE_LENGTH;
        }
    }

    public void invoke(Field field) {
        tick();
        if (!isAlive()) {
            field.setEntity(x, y, StaticEntity.instance(StaticEntityType.EMPTY));
            return;
        }
        for (int i = 0; i < 10; i++) {
            int command = code[counter];
            assert command >= 0 && command < 64;
            final int d = (direction + command % 8 - 1) % 8;
            final Entity entity = field.getEntity(aheadX(d), aheadY(d));
            if (command < 8) {
                if (entity.isPoison()) {
                    die();
                    field.setEntity(x, y, StaticEntity.instance(StaticEntityType.POISON));
                } else if (entity.isEmpty()) {
                    field.setEntity(x, y, StaticEntity.instance(StaticEntityType.EMPTY));
                    x = aheadX(d);
                    y = aheadY(d);
                    field.setEntity(x, y, this);
                } else if (entity.isFood()) {
                    field.setEntity(x, y, StaticEntity.instance(StaticEntityType.EMPTY));
                    x = aheadX(d);
                    y = aheadY(d);
                    field.setEntity(x, y, this);
                    field.placeObject();
                    eat();
                }
                inc(entity.value());
                return;
            } else if (command < 16) {
                if (entity.isPoison()) {
                    field.setEntity(aheadX(d), aheadY(d), StaticEntity.instance(StaticEntityType.FOOD));
                } else if (entity.isFood()) {
                    field.setEntity(aheadX(d), aheadY(d), StaticEntity.instance(StaticEntityType.EMPTY));
                    field.placeObject();
                    eat();
                }
                inc(entity.value());
                return;
            } else if (command < 24) {
                inc(entity.value());
            } else if (command < 32) {
                direction += command - 24;
                if (direction > 7) {
                    direction %= 8;
                }
                inc(1);
            } else {
                inc(command);
            }
        }
    }

    private int aheadX(int direction) {
        if (direction > 1 && direction < 5) return x + 1;
        if (direction > 5 && direction < 8 || direction == 0) return x - 1;
        return x;
    }

    private int aheadY(int direction) {
        if (direction > -1 && direction < 3) return y + 1;
        if (direction > 3 && direction < 7) return y - 1;
        return y;
    }

    private double n(double v) {
        v *= 1.0 * health / START_HEALTH;
        if (v > 1) return 1;
        return v;
    }

    @Override
    public void draw(GL2 gl, int x, int y) {
        GLHelper.setColor(gl, n(0.4), n(0.4), n(0.9));
        super.draw(gl, x, y);
        float color = n(0.5) < 0.5 ? 1 : 0;
        GLHelper.setColor(gl, color, color, color);
        GLHelper.drawLine(gl,
                x * CELL_SIZE + CELL_SIZE / 2.0, y * CELL_SIZE + CELL_SIZE / 2.0,
                CELL_SIZE / 2.5 * Math.cos(direction / 7.0 * Math.PI * 2),
                CELL_SIZE / 2.5 * Math.sin(direction / 7.0 * Math.PI * 2));
    }

    @Override
    public String toString() {
        return direction + " : " + Arrays.toString(code);
    }
}
