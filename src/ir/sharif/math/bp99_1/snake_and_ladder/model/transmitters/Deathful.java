package ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Healer;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

public class Deathful extends Transmitter {

    public Deathful(Cell firstCell, Cell lastCell) {
        super(firstCell, lastCell);
        this.setColor(Color.BLACK);
    }


    @Override
    public void transmit(Piece piece) {
        super.transmit(piece);

        if (!(piece instanceof Healer)) {
            piece.setIsAlive(false);
        }
    }
}
