package ir.sharif.math.bp99_1.snake_and_ladder.model.pieces;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Board;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;
import org.omg.CORBA.MARSHAL;

public class Piece {
    private Cell currentCell;
    private final Color color;
    private final Player player;
    private boolean isSelected;
    protected boolean isActive;
    protected boolean isAlive;

    public Piece(Player player, Color color) {
        this.color = color;
        this.player = player;
        this.isAlive = true;
        this.isActive = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Color getColor() {
        return color;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public boolean getIsAlive() {return this.isAlive;}

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
        if (!isAlive) {
            this.setIsActive(false);
        }
    }

    public boolean getIsActive(){return this.isActive;}

    public void setIsActive(boolean isActive) {this.isActive = isActive;}

    /**
     * @return "true" if your movement is valid  , else return " false"
     * <p>
     * In this method, you should check if the movement is valid of not.
     * <p>
     * You can use some methods ( they are recommended )
     * <p>
     * 1) "canEnter" method in class "Cell"
     * <p>
     * if your movement is valid, return "true" , else return " false"
     */
    public boolean isValidMove(Cell destination, int diceNumber) {
        if(!(Math.abs(this.currentCell.getY()-destination.getY()) == diceNumber && this.currentCell.getX()==destination.getX()) &&
                !(Math.abs(this.currentCell.getX()-destination.getX()) == diceNumber && this.currentCell.getY()==destination.getY()))
            return false;

        if(!destination.canEnter(this))
            return false;

        if(destination.getPiece()!=null)
            return false;

        if(destination.getY() == this.currentCell.getY()) {
            Cell crnt_cell = this.currentCell;
            boolean down = destination.getX()>this.currentCell.getX();

            for(int i=0 ; i<diceNumber ; i++){
                for(int j=0 ; j<crnt_cell.getAdjacentOpenCells().size() ; j++){
                    Cell adj = crnt_cell.getAdjacentOpenCells().get(j);
                    if(adj.getY()==destination.getY() && ((down && adj.getX()>crnt_cell.getX()) || (!down && adj.getX()<crnt_cell.getX()))) {
                        crnt_cell = adj;
                        break;
                    }
                }
            }

            if(crnt_cell.equals(destination))
                return true;
        }

        else if(destination.getX() == this.currentCell.getX()) {
            Cell crnt_cell = this.getCurrentCell();
            boolean right = destination.getY()>this.currentCell.getY();

            for(int i=0 ; i<diceNumber ; i++){
                for(int j=0 ; j<crnt_cell.getAdjacentOpenCells().size() ; j++){
                    Cell adj = crnt_cell.getAdjacentOpenCells().get(j);

                    if(adj.getX()==destination.getX() && ((adj.getY()>crnt_cell.getY() && right) || (adj.getY()<crnt_cell.getY() && !right))) {
                        crnt_cell = adj;
                        break;
                    }
                }
            }
            if(crnt_cell.equals(destination))
                return true;
        }

        return false;
    }

    /**
     * @param destination move selected piece from "currentCell" to "destination"
     */
    public void moveTo(Cell destination) {
        if(isValidMove(destination , this.player.getMoveLeft())) {
            if(destination.getColor().equals(this.color))
                this.getPlayer().applyOnScore(4);

            this.currentCell.setPiece(null);
            this.currentCell = destination;

            if(destination.getPrize()!=null)
                this.getPlayer().usePrize(destination.getPrize());

            if (destination.getTransmitter() != null) {
                if (destination.getTransmitter().getLastCell().canEnter(this)) {
                    destination.getTransmitter().transmit(this);
                }
            }
        }

        this.currentCell.setPiece(this);
    }

    public String getType()
    {
        if (this.getColor().equals(Color.BLUE))
            return "Thief";
        else if (this.getColor().equals(Color.YELLOW))
            return "Bomber";
        else if (this.getColor().equals(Color.RED))
            return "Sniper";
        return "Healer";
    }
}
