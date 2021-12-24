package ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;

public class Ordinary extends Transmitter {

    public Ordinary(Cell firstCell, Cell lastCell) {
        super(firstCell, lastCell);
        this.setColor(Color.WHITE);
    }
}
