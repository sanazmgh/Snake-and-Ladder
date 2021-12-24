package ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

public class Magical  extends Transmitter{
    public Magical(Cell firstCell, Cell lastCell) {
        super(firstCell, lastCell);
        this.setColor(Color.BLUE);
    }

    @Override
    public void transmit(Piece piece) {
        super.transmit(piece);

        piece.getPlayer().applyOnScore(6);
        piece.setIsActive(true);
    }
}
