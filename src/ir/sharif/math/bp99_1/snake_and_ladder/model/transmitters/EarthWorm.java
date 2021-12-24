package ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

import java.util.Random;

public class EarthWorm extends Transmitter {

    public EarthWorm(Cell firstCell, Cell lastCell) {
        super(firstCell, lastCell);
        this.setColor(Color.YELLOW);
    }

    @Override
    public void transmit(Piece piece) {

        Random random = new Random();
        int x = random.nextInt(7)+1;
        int y = random.nextInt(16)+1;
        Cell cell = piece.getCurrentCell().getBoard().getCell(x, y);

        if(cell.canEnter(piece)) {
            piece.getCurrentCell().setPiece(null);
            piece.setCurrentCell(cell);
            piece.getCurrentCell().setPiece(piece);
        }

        piece.getPlayer().applyOnScore(-3);
    }
}
