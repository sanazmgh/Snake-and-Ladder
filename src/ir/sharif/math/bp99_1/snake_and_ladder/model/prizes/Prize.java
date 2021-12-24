package ir.sharif.math.bp99_1.snake_and_ladder.model.prizes;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

public class Prize {
    private final Cell cell;
    private final int point;
    private final int chance, diceNumber;
    private Cell movedTo ;

    public Prize(Cell cell, int point, int chance, int diceNumber) {
        this.cell = cell;
        movedTo = null;
        this.point = point;
        this.chance = chance;
        this.diceNumber = diceNumber;
    }

    public int getPoint() {
        return point;
    }

    public Cell getCell() {
        return cell;
    }

    public int getChance() {
        return chance;
    }

    public int getDiceNumber() {
        return diceNumber;
    }


    /**
     * apply the prize.
     * you can use method "usePrize" in class "Player" (not necessary, but recommended)
     */
    public void using(Piece piece) {
        this.cell.getPiece().getPlayer().usePrize(this);
    }

}
