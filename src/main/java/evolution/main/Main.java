package evolution.main;

import com.jogamp.opengl.GL2;
import evolution.GLDisplay.GLDisplay;
import evolution.main.entity.Unit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static evolution.main.Utility.*;


public class Main implements GLDisplay.Draw {

    private Field field = new Field();
    public volatile boolean hasDelay = true;

    public void startSimulation() {
        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < NEW_GENERATION_SIZE; i++) {
            units.add(new Unit());
        }
        int number = 0;
        while (true) {
            units = makeTurn(units);
            number++;
            System.out.print(number + ". ");
        }
    }

    public List<Unit> makeTurn(List<Unit> units) {
        for (int i = 0; i < NEW_GENERATION_SIZE; i++) {
            for (int j = 0; j < NEW_GENERATION_SIZE - 1; j++) {
                units.add(units.get(i).copy());
            }
            units.get(i).mutate();
        }
        field.createField(units);

        final List<Unit> newUnits = new ArrayList<>();
        int f = 0;
        int ticks = 0;
        while (!units.isEmpty() && ticks < 1_000_000) {
            final long cycleStart = System.currentTimeMillis();
            for (final ListIterator<Unit> i = units.listIterator(); i.hasNext(); ) {
                final Unit unit = i.next();
                synchronized (field) {
                    unit.invoke(field);
                }
                if (!unit.isAlive()) {
                    if (units.size() <= NEW_GENERATION_SIZE) {
                        newUnits.add(unit.copy());
                        if (units.size() == 1) {
                            System.out.println(unit.getLifetime());
                            save(unit);
                        }
                    }
                    i.remove();
                }
            }
            ticks++;
            final long cycleEnd = System.currentTimeMillis();
            if (cycleEnd - cycleStart < 50 && hasDelay) {
                try {
                    Thread.sleep(50 - (cycleEnd - cycleStart));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!units.isEmpty()) {
            while(newUnits.size() < NEW_GENERATION_SIZE) {
                newUnits.add(units.get(0));
                units.remove(0);
            }
        }

        return newUnits;
    }

    private int maxLifetime = 0;

    private void save(Unit unit) {
        if (unit.getLifetime() > 3000 && unit.getLifetime() > maxLifetime) {
            maxLifetime = unit.getLifetime();
            try (FileWriter fw = new FileWriter("/home/andrey104/saves/" + unit.getLifetime())){
                final int[] code = unit.getCode();
                for (int i = 0; i < code.length; i++) {
                    fw.write(String.format("\t%d\t%d\n", i, code[i]));
                }
            } catch (IOException e) {
                System.err.println("IO Error");
            }
        }
    }

    @Override
    public void init(GL2 gl) {

    }

    @Override
    public void display(GL2 gl) {
        synchronized (field) {
            field.display(gl);
        }
        gl.glColor4d(1, 1, 1, 0.1);
        gl.glBegin(GL2.GL_LINES);
        for (int x = 0; x < WIDTH; x++) {
            gl.glVertex2d(x * CELL_SIZE, 0);
            gl.glVertex2d(x * CELL_SIZE, FIELD_HEIGHT);
        }
        for (int y = 0; y < HEIGHT; y++) {
            gl.glVertex2d(0, y * CELL_SIZE);
            gl.glVertex2d(FIELD_WIDTH, y * CELL_SIZE);
        }
        gl.glEnd();
    }

    public static void main(String[] args) {
        final Main main = new Main();
        final GLDisplay display = new GLDisplay("Evolution",
                FIELD_WIDTH,
                FIELD_HEIGHT,
                GLDisplay.CoordSystem.CORNER, main);
        display.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D:
                        main.hasDelay = !main.hasDelay;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        main.startSimulation();
    }
}
