package ir.sharif.math.bp99_1.snake_and_ladder.model.pieces;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Board;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;

import java.util.LinkedList;
import java.util.List;

public class Bomber extends Piece {
    private List<Cell> TurnedBlack = new LinkedList<>();

    public Bomber(Player player, Color color) {
        super(player, color);
    }

    public List<Cell> getTurnedBlack() { return TurnedBlack; }

    @Override
    public boolean getIsActive() {
        return this.getIsAlive();
    }

    public void Explode() {
        if (this.getIsActive()) {
            int x = this.getCurrentCell().getX();
            int y = this.getCurrentCell().getY();
            Board board = this.getCurrentCell().getBoard();

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    Cell cell = board.getCell(i, j);

                    if (cell != null) {
                        if (cell.getPiece() != null) {
                            cell.getPiece().setIsAlive(false);
                        }

                        if(cell.getPrize() != null){
                            cell.setPrize(null);
                        }
                    }
                }
            }

            this.getCurrentCell().TurnBlack();
            this.TurnedBlack.add(this.getCurrentCell());
            this.setIsActive(false);
        }
    }
}
