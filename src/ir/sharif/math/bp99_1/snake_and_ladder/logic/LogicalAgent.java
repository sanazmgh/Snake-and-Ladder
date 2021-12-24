package ir.sharif.math.bp99_1.snake_and_ladder.logic;


import ir.sharif.math.bp99_1.snake_and_ladder.graphic.GraphicalAgent;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Board;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.GameState;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Bomber;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;

import java.util.Iterator;
import java.util.Map;

/**
 * This class is an interface between logic and graphic.
 * some methods of this class, is called from graphic.
 * DO NOT CHANGE ANY PART WHICH WE MENTION.
 */
public class LogicalAgent {
    private final ModelLoader modelLoader;
    private final GraphicalAgent graphicalAgent;
    private final GameState gameState;

    /**
     * DO NOT CHANGE CONSTRUCTOR.
     */
    public LogicalAgent() {
        this.graphicalAgent = new GraphicalAgent(this);
        this.modelLoader = new ModelLoader();
        this.gameState = loadGameState();
    }


    /**
     * NO CHANGES NEEDED.
     */
    private GameState loadGameState() {
        Board board = modelLoader.loadBord();
        Player player1 = modelLoader.loadPlayer(graphicalAgent.getPlayerNames(1), 1);
        Player player2;
        do {
            player2 = modelLoader.loadPlayer(graphicalAgent.getPlayerNames(2), 2);
        } while (player1.equals(player2));
        player1.setRival(player2);
        player2.setRival(player1);
        return modelLoader.loadState(player1, player2, board);
    }

    /**
     * NO CHANGES NEEDED.
     */
    public void initialize() {
        graphicalAgent.initialize(gameState);
    }

    /**
     * Give a number from graphic,( which is the playerNumber of a player
     * who clicks "ReadyButton".) you should somehow change that player state.
     * if both players are ready. then start the game.
     */
    public void readyPlayer(int playerNumber) {
        this.gameState.getPlayer(playerNumber).setReady(!this.gameState.getPlayer(playerNumber).isReady());

        if (this.gameState.getPlayer(playerNumber).isReady() && this.gameState.getPlayer((playerNumber%2)+1).isReady()){
            for(Map.Entry<Cell , Integer> st_cell : this.gameState.getBoard().getStartingCells().entrySet()) {
                for(Piece piece : gameState.getPlayer(st_cell.getValue()).getPieces()){
                    if(piece.getCurrentCell()==null &&piece.getColor().equals(st_cell.getKey().getColor())){
                        piece.setCurrentCell(st_cell.getKey());
                        st_cell.getKey().setPiece(piece);
                    }
                }
            }
            this.gameState.nextTurn();
            modelLoader.saveGameState(gameState);
        }

        // dont touch this line
        graphicalAgent.update(gameState);
    }

    /**
     * give x,y (coordinates of a cell) :
     * you should handle if user want to select a piece
     * or already selected a piece and now want to move it to a new cell
     */
    // ***
    public void selectCell(int x, int y) {
        Cell cell = this.gameState.getBoard().getCell(x, y);

        if (!gameState.isStarted())
            return;

        if (cell.getPiece() == null) {
            if (this.gameState.getCurrentPlayer().isDicePlayedThisTurn() && this.gameState.getCurrentPlayer().getSelectedPiece() != null) {
                this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(false);
                if (this.gameState.getCurrentPlayer().getSelectedPiece().isValidMove(cell, this.gameState.getCurrentPlayer().getMoveLeft())) {
                    this.gameState.getCurrentPlayer().getSelectedPiece().moveTo(cell);
                    this.gameState.nextTurn();
                    modelLoader.saveGameState(gameState);
                }
            }
        }

        else if (cell.getPiece().getPlayer().equals(this.gameState.getCurrentPlayer())) {
            if (this.gameState.getCurrentPlayer().getSelectedPiece() != null) {

                if (this.gameState.getCurrentPlayer().getSelectedPiece().getCurrentCell().equals(cell)) {

                    if(cell.getPiece().equals(this.gameState.getCurrentPlayer().getBomber())){
                        if(cell.getPiece().getIsActive()){
                            this.gameState.getCurrentPlayer().getBomber().Explode();

                            this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(false);
                            this.gameState.getCurrentPlayer().setSelectedPiece(null);
                            this.gameState.nextTurn();
                            modelLoader.saveGameState(gameState);
                        }
                    }

                    else if(cell.getPiece().equals(this.gameState.getCurrentPlayer().getThief())){
                        if(cell.getPiece().getIsActive()){
                            if(this.gameState.getCurrentPlayer().getThief().getCurrentPrize() != null)
                                this.gameState.getCurrentPlayer().getThief().DropPrize();

                            else
                                this.gameState.getCurrentPlayer().getThief().TakePrize();

                            this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(false);
                            this.gameState.getCurrentPlayer().setSelectedPiece(null);
                            this.gameState.nextTurn();
                            modelLoader.saveGameState(gameState);
                        }
                    }

                    else {
                        this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(false);
                        this.gameState.getCurrentPlayer().setSelectedPiece(null);
                    }
                }

                else {
                    this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(false);
                    this.gameState.getCurrentPlayer().setSelectedPiece(cell.getPiece());
                    this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(true);
                }
            }

            else if(cell.getPiece().getIsAlive()){
                this.gameState.getCurrentPlayer().setSelectedPiece(cell.getPiece());
                this.gameState.getCurrentPlayer().getSelectedPiece().setSelected(true);
            }
        }

        // dont touch this line
        graphicalAgent.update(gameState);
        checkForEndGame();
    }

    public String getCellDetails(int x, int y) { return "cell at " + x + "," + y ;}
    /**
     * check for endgame and specify winner
     * if player one in winner set winner variable to 1
     * if player two in winner set winner variable to 2
     * If the game is a draw set winner variable to 3
     */
    private void checkForEndGame() {
        if (this.gameState.getTurn()==40) {
            // game ends
            int winner = 1;

            if(this.gameState.getPlayer2().getScore() > this.gameState.getPlayer1().getScore())
                winner = 2 ;

            else if(this.gameState.getPlayer2().getScore() == this.gameState.getPlayer1().getScore())
                winner = 3 ;

            // dont touch it
            graphicalAgent.playerWin(winner);
            /* save players*/
            modelLoader.savePlayer(gameState.getPlayer1());
            modelLoader.savePlayer(gameState.getPlayer2());
            modelLoader.archive(gameState.getPlayer1(), gameState.getPlayer2());
            modelLoader.deleteSavedGames(gameState);
            LogicalAgent logicalAgent = new LogicalAgent();
            logicalAgent.initialize();
        }
    }


    /**
     * Give a number from graphic,( which is the playerNumber of a player
     * who left clicks "dice button".) you should roll his/her dice
     * and update *****************
     */
    public void rollDice(int playerNumber) {
        if(this.gameState.isStarted()) {
            Player player = this.gameState.getPlayer(playerNumber);

            if (this.gameState.getCurrentPlayer().getPlayerNumber() == playerNumber && !player.isDicePlayedThisTurn()) {
                player.setDicePlayedThisTurn(true);
                player.setMoveLeft(player.getDice().roll());

                if(player.getMoveLeft() == 1){
                    this.gameState.getCurrentPlayer().getHealer().setIsActive(true);
                }

                if(player.getMoveLeft() == 2) {
                    this.gameState.getCurrentPlayer().getDice().resetDice();
                }

                if(player.getMoveLeft() == 3) {
                    this.gameState.getCurrentPlayer().getBomber().setIsActive(true);
                }

                if(player.getMoveLeft() == 5) {
                    this.gameState.getCurrentPlayer().getSniper().setIsActive(true);
                }

                if(player.getMoveLeft() == 6)
                    player.applyOnScore(4);

                if(!player.hasMove(gameState.getBoard() , player.getMoveLeft())) {
                    this.gameState.getCurrentPlayer().applyOnScore(-3);
                    this.gameState.nextTurn();
                }
            }
        }
        modelLoader.saveGameState(gameState);
        // dont touch this line
        graphicalAgent.update(gameState);
    }


    /**
     * Give a number from graphic,( which is the playerNumber of a player
     * who right clicks "dice button".) you should return the dice detail of that player.
     * you can use method "getDetails" in class "Dice"(not necessary, but recommended )
     */
    public String getDiceDetail(int playerNumber) {
        return this.gameState.getPlayer(playerNumber).getDice().getDetails();
    }
}
