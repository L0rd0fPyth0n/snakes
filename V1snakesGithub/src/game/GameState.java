package game;

import java.io.Serializable;
import java.util.List;

public record GameState(List<Snake> snakeList) implements Serializable {
    @Override
    public List<Snake> snakeList() {
        return snakeList;
    }
}