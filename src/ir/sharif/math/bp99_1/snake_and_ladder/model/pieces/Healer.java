package ir.sharif.math.bp99_1.snake_and_ladder.model.pieces;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;

public class Healer extends Piece{
    public Healer(Player player, Color color) {
        super(player, color);
    }

    @Override
    public void setIsAlive(boolean isAlive) {this.isAlive = true;}

    @Override
    public boolean getIsAlive() { return true; }

    public void Heal() {
        for (Cell cell : this.getCurrentCell().getAdjacentCells()) {
            if (this.getIsActive()) {
                if (cell.getPiece() != null) {
                    if (cell.getPiece().getPlayer().equals(this.getPlayer()) && !cell.getPiece().getIsAlive()) {
                        cell.getPiece().setIsAlive(true);
                        this.setIsActive(false);
                    }
                }
            }
        }
    }

    @Override
    public void moveTo(Cell destination) {
        super.moveTo(destination);
        this.Heal();
    }
}
