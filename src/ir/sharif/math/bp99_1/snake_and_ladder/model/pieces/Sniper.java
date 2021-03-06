package ir.sharif.math.bp99_1.snake_and_ladder.model.pieces;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;

public class Sniper extends Piece {

    public Sniper(Player player, Color color) {
        super(player, color);
    }

    public void Shoot() {
        for (Cell cell : this.getCurrentCell().getAdjacentCells()) {
            if (this.getIsActive()) {
                if (cell.getPiece() != null) {
                    if (cell.getPiece().getPlayer().equals(this.getPlayer().getRival()) && !(cell.getPiece() instanceof Healer)) {
                        cell.getPiece().setIsAlive(false);
                        cell.getPiece().setIsActive(false);
                        this.setIsActive(false);
                    }
                }
            }
        }
    }

    @Override
    public void moveTo(Cell destination) {
        super.moveTo(destination);
        this.Shoot();
    }
}
