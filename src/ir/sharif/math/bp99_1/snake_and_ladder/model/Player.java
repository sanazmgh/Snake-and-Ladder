package ir.sharif.math.bp99_1.snake_and_ladder.model;

import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.*;
import ir.sharif.math.bp99_1.snake_and_ladder.model.prizes.Prize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final String name;
    private int score;
    private final List<Piece> pieces;
    private final Dice dice;
    private Player rival;
    private final int id;
    private int playerNumber;
    private boolean isReady;
    private boolean dicePlayedThisTurn;
    private int moveLeft;
    private Piece selectedPiece;
    private Bomber bomber;
    private Thief thief;
    private Sniper sniper;
    private Healer healer;

    public Player(String name, int score, int id, int playerNumber) {
        this.name = name;
        this.score = score;
        this.id = id;
        this.playerNumber = playerNumber;
        this.dice = new Dice();
        this.pieces = new ArrayList<>();
        bomber = new Bomber(this , Color.YELLOW);
        thief = new Thief(this , Color.BLUE);
        sniper = new Sniper(this , Color.RED);
        healer = new Healer(this , Color.GREEN);
        this.pieces.add(bomber);
        this.pieces.add(thief);
        this.pieces.add(sniper);
        this.pieces.add(healer);
        this.moveLeft = 0;
        this.selectedPiece = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Dice getDice() {
        return dice;
    }

    public int getScore() {
        return score;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Player getRival() {
        return rival;
    }

    public int getMoveLeft() {
        return moveLeft;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public boolean isDicePlayedThisTurn() {
        return dicePlayedThisTurn;
    }

    public void setDicePlayedThisTurn(boolean dicePlayedThisTurn) {
        this.dicePlayedThisTurn = dicePlayedThisTurn;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setRival(Player rival) {
        this.rival = rival;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void applyOnScore(int score) {
        this.score += score;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Sniper getSniper() { return this.sniper; }

    public Bomber getBomber() { return this.bomber; }

    public Thief getThief() { return this.thief; }

    public Healer getHealer() { return this.healer; }

    /**
     * @param prize according to input prize , apply necessary changes to score and dice chance
     *              <p>
     *              you can use method "addChance" in class "Dice"(not necessary, but recommended)
     */
    public void usePrize(Prize prize) {
        dice.addChance(prize.getDiceNumber() , prize.getChance());
        this.score += prize.getPoint();
    }


    /**
     * check if any of player pieces can move to another cell.
     *
     * @return true if at least 1 piece has a move , else return false
     * <p>
     * you can use method "isValidMove" in class "Piece"(not necessary, but recommended)
     */
    public boolean hasMove(Board board, int diceNumber) {
        for(int i=0 ; i<this.pieces.size() ; i++){
            Cell Crnt = this.pieces.get(i).getCurrentCell();

            if(Crnt.getY()+diceNumber<=16)
                if(this.pieces.get(i).isValidMove(board.getCell(Crnt.getX() , Crnt.getY()+diceNumber) , diceNumber ))
                    return true;

            if(Crnt.getY()-diceNumber>=1)
                if(this.pieces.get(i).isValidMove(board.getCell(Crnt.getX() , Crnt.getY()-diceNumber) , diceNumber ))
                    return true;

            if(Crnt.getX()+diceNumber<=7)
                if(this.pieces.get(i).isValidMove(board.getCell(Crnt.getX()+diceNumber , Crnt.getY()) , diceNumber ))
                    return true;

            if(Crnt.getX()-diceNumber>=1)
                if(this.pieces.get(i).isValidMove(board.getCell(Crnt.getX()-diceNumber , Crnt.getY()) , diceNumber ))
                    return true;
        }

        if(this.getBomber().getIsActive() || this.getThief().getIsActive())
            return true;

        return false;
    }


    /**
     * Deselect selectedPiece and make some changes in this class fields.
     */
    // **
    public void endTurn() {
        this.dicePlayedThisTurn=false;
        try {
            this.selectedPiece.setSelected(false);
        }
        catch(RuntimeException e){ }
        this.selectedPiece = null;
        this.moveLeft = 0 ;
    }


    /**
     * DO NOT CHANGE FOLLOWING METHODS.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

