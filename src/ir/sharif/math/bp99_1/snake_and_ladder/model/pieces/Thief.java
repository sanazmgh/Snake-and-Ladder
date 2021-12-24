package ir.sharif.math.bp99_1.snake_and_ladder.model.pieces;

import com.sun.org.apache.xerces.internal.xs.LSInputList;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;
import ir.sharif.math.bp99_1.snake_and_ladder.model.prizes.Prize;


public class Thief extends Piece {
    private Prize CurrentPrize;

    public Thief(Player player, Color color) {
        super(player, color);
    }

    @Override
    public boolean getIsActive() {
        if(!this.getIsAlive())
            return false ;

        if(this.getCurrentPrize() != null || this.getCurrentCell().getPrize() != null)
            return true;

        return false;
    }

    @Override
    public void setIsActive(boolean isActive) {
        if(!this.getIsAlive())
            this.isActive = false ;

        if(this.getCurrentPrize() != null || this.getCurrentCell().getPrize() != null)
            this.isActive = true;

        this.isActive = false;
    }

    public void setCurrentPrize(Prize prize){
        this.CurrentPrize = prize;
    }

    public Prize getCurrentPrize() { return this.CurrentPrize; }

    public void TakePrize(){
        this.CurrentPrize = this.getCurrentCell().getPrize();
        this.getCurrentCell().setPrize(null);
    }

    public void DropPrize(){
        this.getCurrentCell().setPrize(this.CurrentPrize);
        this.CurrentPrize = null;
    }

    @Override
    public boolean isValidMove(Cell destination, int diceNumber) {
        if(!(Math.abs(this.getCurrentCell().getY()-destination.getY()) == diceNumber && this.getCurrentCell().getX()==destination.getX()) &&
                !(Math.abs(this.getCurrentCell().getX()-destination.getX()) == diceNumber && this.getCurrentCell().getY()==destination.getY()))
            return false;

        if(destination.getPiece()!=null)
            return false;

        return true;
    }

    @Override
    public void moveTo(Cell destination) {
        if(isValidMove(destination , this.getPlayer().getMoveLeft())) {
            if(destination.getColor().equals(this.getColor()))
                this.getPlayer().applyOnScore(4);

            this.getCurrentCell().setPiece(null);
            this.setCurrentCell(destination);

            if(destination.getPrize()!=null && this.CurrentPrize!=null)
                this.getPlayer().usePrize(destination.getPrize());

            if (destination.getTransmitter() != null) {
                this.CurrentPrize = null;

                if (destination.getTransmitter() != null) {
                    if (destination.getTransmitter().getLastCell().canEnter(this)) {
                        destination.getTransmitter().transmit(this);
                    }
                }
            }
        }

        this.getCurrentCell().setPiece(this);
    }
}
