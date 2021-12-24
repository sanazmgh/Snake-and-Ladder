package ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

import java.util.Random;

public class Transmitter {
    private final Cell firstCell, lastCell;
    private Color color = Color.BLUE;
    private String type ;

    public Transmitter(Cell firstCell, Cell lastCell ) {
        this.firstCell = firstCell;
        this.lastCell = lastCell;
        this.getFirstCell().setTransmitter(this);
    }

    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    public Cell getFirstCell() {
        return firstCell;
    }
    public Cell getLastCell() {
        return lastCell;
    }

    /**
     * transmit piece to lastCell
     */
    public void transmit(Piece piece) {
        if(lastCell.canEnter(piece)) {
            piece.getCurrentCell().setPiece(null);
            piece.setCurrentCell(lastCell);
            piece.getCurrentCell().setPiece(piece);
        }

        piece.getPlayer().applyOnScore(-3);
    }

}
