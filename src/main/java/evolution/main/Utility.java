package evolution.main;

import java.util.Random;

public final class Utility {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 30;
    public static final int OBJECTS_COUNT = 400;
    public static final int START_HEALTH = 100;
    public static final int EAT_VALUE = 10;
    public static final int CODE_LENGTH = 7;
    public static final int INSTRUCTION_COUNT = 64;
    public static final int NEW_GENERATION_SIZE = 3;
    public static final int CELL_SIZE = 20;
    public static final int FIELD_WIDTH = WIDTH * CELL_SIZE;
    public static final int FIELD_HEIGHT = HEIGHT * CELL_SIZE;
    public static final double MUTATION_PROBABILITY = 0.1;


    public static final Random random = new Random();
}
