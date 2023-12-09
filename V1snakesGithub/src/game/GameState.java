package game;

import environment.BoardPosition;
import environment.Cell;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public record GameState(Cell[][] cells, LinkedList<Snake> snakes, LinkedList<Obstacle> obstacles, Goal goal,BoardPosition goalPosition,
                        boolean run) implements Serializable {
    @Override
    public BoardPosition goalPosition() {
        return goalPosition;
    }

    @Override
    public boolean run() {
        return run;
    }

    @Override
    public Cell[][] cells() {
        return cells;
    }

    @Override
    public LinkedList<Snake> snakes() {
        return snakes;
    }

    @Override
    public LinkedList<Obstacle> obstacles() {
        return obstacles;
    }

    @Override
    public Goal goal() {
        return goal;
    }

}
