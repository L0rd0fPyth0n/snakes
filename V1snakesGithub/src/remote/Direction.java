package remote;

import java.io.Serializable;

public enum Direction implements Serializable {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
    private final int x;
    private final int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }


//    public static Direction getRandomDirection(){
//        return Direction.values()[r.nextInt(4)];
//        return Direction.values()[new Random().nextInt(4)];
//    }
}