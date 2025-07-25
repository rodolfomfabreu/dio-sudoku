package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {
    private final List<List<Space>> spaces;


    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return this.spaces;
    }

    public GameStatusEnum getStatus() {
        if (this.spaces
                .stream()
                .flatMap(Collection::stream)
                .noneMatch(s -> 
                    !s.isFixed() 
                    && nonNull(s.getActual()))) {
                return GameStatusEnum.NON_STARTED;
        }

        return this.spaces
                    .stream()
                    .flatMap(Collection::stream)
                    .anyMatch(s -> 
                        isNull(s.getActual())) 
                        ? GameStatusEnum.INCOMPLETED 
                        : GameStatusEnum.COMPLETED;
    }

    public boolean hasErrors() {
        if (getStatus() == GameStatusEnum.NON_STARTED) {
            return false;
        }

        return this.spaces
                    .stream()
                    .flatMap(Collection::stream)
                    .anyMatch(s -> 
                        nonNull(s.getActual()) 
                        && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(int col, int row, Integer value) {
        if (checkFixedSpace(col, row)) {
            return false;
        }
        Space space = getSpace(col, row);
        space.setActual(value);
        return true;
    }

    public boolean clearValue(int col, int row) {
        if (checkFixedSpace(col, row)) {
            return false;
        }
        Space space = getSpace(col, row);
        space.clearSpace();
        return true;
    }

    private Space getSpace(int col, int row) {
        return spaces.get(col).get(row);
    }

    private Boolean checkFixedSpace(int col, int row) {
        Space space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return true;
        }
        return false;
    }

    public void reset() {
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErrors() 
                && getStatus()
                    .equals(GameStatusEnum.COMPLETED);
    }
}
