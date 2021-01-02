package com.company;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private int life;
    private int numberOfSquares;
    private List<ShipUnitPosition> positions = new ArrayList<>();
    private boolean horizontal;

    public Ship(int number) {
        this.life = number;
        this.numberOfSquares = number;
    }

    public int getLife() {
        return life;
    }

    public List<ShipUnitPosition> getPositions() {
        return positions;
    }

    public int getNumberOfSquares() {
        return numberOfSquares;
    }

    public void setPositions(int row, int column) {
        ShipUnitPosition shipUnitPosition = new ShipUnitPosition(row, column);
        positions.add(shipUnitPosition);
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
