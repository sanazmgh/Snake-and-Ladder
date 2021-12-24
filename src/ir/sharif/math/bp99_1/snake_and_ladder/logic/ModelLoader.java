package ir.sharif.math.bp99_1.snake_and_ladder.logic;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Board;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.GameState;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Player;
import ir.sharif.math.bp99_1.snake_and_ladder.model.pieces.Piece;
import ir.sharif.math.bp99_1.snake_and_ladder.model.prizes.Prize;
import ir.sharif.math.bp99_1.snake_and_ladder.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ModelLoader {
    private final File boardFile, playersDirectory, archiveFile, savedGames;

    /**
     * DO NOT CHANGE ANYTHING IN CONSTRUCTOR.
     */
    public ModelLoader() {
        boardFile = Config.getConfig("mainConfig").getProperty(File.class, "board");
        playersDirectory = Config.getConfig("mainConfig").getProperty(File.class, "playersDirectory");
        archiveFile = Config.getConfig("mainConfig").getProperty(File.class, "archive");
        savedGames = Config.getConfig("mainConfig").getProperty(File.class, "savedGames");
        if (!playersDirectory.exists()) playersDirectory.mkdirs();
    }


    /**
     * read file "boardFile" and craete a Board
     * <p>
     * you can use "BoardBuilder" class for this purpose.
     * <p>
     * pay attention add your codes in "try".
     */
    public Board loadBord() {
        try {
            Scanner scanner = new Scanner(boardFile);
            String BoardString="";

            while (scanner.hasNext()) {
                BoardString += scanner.next();
                BoardString += "\n";
            }
            scanner.close();

            BoardBuilder Board = new BoardBuilder(BoardString);
            return Board.build();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("could not find board file");
            System.exit(-2);
        }
        return null;
    }

    /**
     * load player.
     * if no such a player exist, create an account(file) for him/her.
     * <p>
     * you can use "savePlayer" method of this class for that purpose.
     * <p>
     * add your codes in "try" block .
     */
    public Player loadPlayer(String name, int playerNumber) {
        try {
            File playerFile = getPlayerFile(name);
           // Scanner scanner = new Scanner(playerFile);
            Player player;

            if(playerFile==null){
                int id = 0 ;
                for (File file : playersDirectory.listFiles()) {
                    Scanner scanner = new Scanner(file);
                    id = Math.max(id , scanner.nextInt());
                    scanner.close();
                }
                player = new Player(name , 0 , id+1 , playerNumber );
                savePlayer(player);
            }

            else{
                Scanner scanner = new Scanner(playerFile);
                int id = scanner.nextInt();
                scanner.close();
                player = new Player(name , 0 , id , playerNumber );
            }

            return player;
        }

        catch (FileNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("could not find player file");
            System.exit(-2);
        }
        return null;
    }

    public String  saveGameState(GameState gameState){

        String saveState = "";

        try {
            String fileName = gameState.getPlayer1().getId() + "-" + gameState.getPlayer2().getId();
            File gameFile = null;
            for (File file : Objects.requireNonNull(savedGames.listFiles())) {
                if (file.getName().equals(fileName))
                    gameFile = file;
            }

            List<Cell> toBlack = new LinkedList<>();
            toBlack.addAll(gameState.getPlayer1().getBomber().getTurnedBlack());
            toBlack.addAll(gameState.getPlayer2().getBomber().getTurnedBlack());

            List<Prize> newPrizes = new LinkedList<>();
            for (Cell cell : gameState.getBoard().getCells())
                if (cell.getPrize() != null)
                    newPrizes.add(cell.getPrize());

            saveState += "TO_BLACK " + toBlack.size() + '\n';
            for (Cell cell : toBlack)
                saveState += cell.getX() + " " + cell.getY() + '\n';

            saveState += '\n';

            saveState += "NEW_PRIZES " + newPrizes.size() + '\n';
            for (Prize prize : newPrizes)
                saveState += prize.getCell().getX() + " " + prize.getCell().getY() + " "
                        + prize.getPoint() + " " + prize.getChance() + " " + prize.getDiceNumber() + '\n';

            saveState += '\n';

            saveState += "PLAYER1_PIECES : " + '\n';

            saveState += "BOMBER : ";
            saveState += gameState.getPlayer1().getBomber().getCurrentCell().getX() + " "
                    + gameState.getPlayer1().getBomber().getCurrentCell().getY() + " "
                    + gameState.getPlayer1().getBomber().getIsAlive() + " "
                    + gameState.getPlayer1().getBomber().getIsActive() + '\n';

            saveState += "THIEF : ";
            saveState += gameState.getPlayer1().getThief().getCurrentCell().getX() + " "
                    + gameState.getPlayer1().getThief().getCurrentCell().getY() + " "
                    + gameState.getPlayer1().getThief().getIsAlive() + " "
                    + gameState.getPlayer1().getThief().getIsActive() + '\n';

            saveState += "HEALER : ";
            saveState += gameState.getPlayer1().getHealer().getCurrentCell().getX() + " "
                    + gameState.getPlayer1().getHealer().getCurrentCell().getY() + " "
                    + gameState.getPlayer1().getHealer().getIsAlive() + " "
                    + gameState.getPlayer1().getHealer().getIsActive() + '\n';

            saveState += "SNIPER : ";
            saveState += gameState.getPlayer1().getSniper().getCurrentCell().getX() + " "
                    + gameState.getPlayer1().getSniper().getCurrentCell().getY() + " "
                    + gameState.getPlayer1().getSniper().getIsAlive() + " "
                    + gameState.getPlayer1().getSniper().getIsActive() + '\n';

            saveState += "ROBBED_PRIZE : ";

            if (gameState.getPlayer1().getThief().getCurrentPrize() != null) {
                Prize prize = gameState.getPlayer1().getThief().getCurrentPrize();

                saveState += prize.getCell().getX() + " " + prize.getCell().getY() + " "
                        + prize.getPoint() + " " + prize.getChance() + " " + prize.getDiceNumber() + '\n';
            }

            else
                saveState += "0" + '\n';

            saveState +=  "\n";

            saveState += "PLAYER2_PIECES : " + '\n';

            saveState += "BOMBER : ";
            saveState += gameState.getPlayer2().getBomber().getCurrentCell().getX() + " "
                    + gameState.getPlayer2().getBomber().getCurrentCell().getY() + " "
                    + gameState.getPlayer2().getBomber().getIsAlive() + " "
                    + gameState.getPlayer2().getBomber().getIsActive() + '\n';

            saveState += "THIEF : ";
            saveState += gameState.getPlayer2().getThief().getCurrentCell().getX() + " "
                    + gameState.getPlayer2().getThief().getCurrentCell().getY() + " "
                    + gameState.getPlayer2().getThief().getIsAlive() + " "
                    + gameState.getPlayer2().getThief().getIsActive() + '\n';

            saveState += "HEALER : ";
            saveState += gameState.getPlayer2().getHealer().getCurrentCell().getX() + " "
                    + gameState.getPlayer2().getHealer().getCurrentCell().getY() + " "
                    + gameState.getPlayer2().getHealer().getIsAlive() + " "
                    + gameState.getPlayer2().getHealer().getIsActive() + '\n';

            saveState += "SNIPER : ";
            saveState += gameState.getPlayer2().getSniper().getCurrentCell().getX() + " "
                    + gameState.getPlayer2().getSniper().getCurrentCell().getY() + " "
                    + gameState.getPlayer2().getSniper().getIsAlive() + " "
                    + gameState.getPlayer2().getSniper().getIsActive() + '\n';

            saveState += "ROBBED_PRIZE : ";

            if (gameState.getPlayer2().getThief().getCurrentPrize() != null) {
                Prize prize = gameState.getPlayer2().getThief().getCurrentPrize();

                saveState += prize.getCell().getX() + " " + prize.getCell().getY() + " "
                        + prize.getPoint() + " " + prize.getChance() + " " + prize.getDiceNumber() + '\n';
            } else
                saveState += "0" + '\n';

            saveState += '\n';

            saveState += "TURN " + gameState.getTurn() + '\n' + '\n';

            saveState += "DICE_NUMBER " + gameState.getCurrentPlayer().getMoveLeft() + '\n' + '\n';

            saveState += "DICE-DETAILS1 " + '\n';
            saveState += gameState.getPlayer1().getDice().getDetails();

            saveState += '\n';

            saveState += "DICE-DETAILS2 " + '\n';
            saveState += gameState.getPlayer2().getDice().getDetails();

            saveState += '\n';

            saveState += "SCORE1 : " + gameState.getPlayer1().getScore() + '\n'+'\n';
            saveState += "SCORE2 : " + gameState.getPlayer2().getScore() + '\n';

            if (gameFile == null) {
                gameFile = new File(savedGames + "/" + fileName);
            }
            PrintStream printStream = new PrintStream(new FileOutputStream(gameFile, false));
            printStream.println(saveState);
            printStream.flush();
            printStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.err.println("could not find player file");
            System.exit(-2);
        }

        return saveState ;
    }

    public GameState loadState(Player player1 , Player player2 , Board board){
        try {
            String fileName = player1.getId() + "-" + player2.getId();
            File gameFile = null;
            for (File file : Objects.requireNonNull(savedGames.listFiles())) {
                if (file.getName().equals(fileName))
                    gameFile = file;
            }

            if (gameFile == null)
                return new GameState(board, player1, player2);

            Scanner scanner = new Scanner(gameFile);

            scanner.next();
            int cnt = scanner.nextInt();

            for(int i=0 ; i<cnt ; i++){
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                Cell cell = board.getCell(x , y);
                cell.TurnBlack();
            }

            scanner.nextLine();
            scanner.next();

            cnt = scanner.nextInt();
            for(Cell cell : board.getCells())
                cell.setPrize(null);

            for(int i=0 ; i<cnt ; i++){
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int score = scanner.nextInt();
                int chance = scanner.nextInt();
                int diceNumber = scanner.nextInt();
                Cell cell = board.getCell(x , y);
                if (cell==null)
                    System.out.println(x + " " + y);
                Prize prize = new Prize(cell , score , chance , diceNumber);
                cell.setPrize(prize);
            }

            scanner.nextLine();
            scanner.next();
            scanner.next();

            for(int i=0 ; i<4 ; i++){
                scanner.next();
                scanner.next();

                int x = scanner.nextInt();
                int y = scanner.nextInt();
                Cell cell = board.getCell(x , y);
                String isAlive = scanner.next();
                String isActive = scanner.next();

                if(i==0) {
                    player1.getBomber().setCurrentCell(cell);
                    player1.getBomber().setIsAlive(isAlive.equals("true"));
                    player1.getBomber().setIsActive(isActive.equals("true"));
                    cell.setPiece(player1.getBomber());
                }

                if(i==1){
                    player1.getThief().setCurrentCell(cell);
                    player1.getThief().setIsAlive(isAlive.equals("true"));
                    player1.getThief().setIsActive(isActive.equals("true"));
                    cell.setPiece(player1.getThief());
                }

                if(i==2){
                    player1.getHealer().setCurrentCell(cell);
                    player1.getHealer().setIsAlive(isAlive.equals("true"));
                    player1.getHealer().setIsActive(isActive.equals("true"));
                    cell.setPiece(player1.getHealer());
                }

                if(i==3) {
                    player1.getSniper().setCurrentCell(cell);
                    player1.getSniper().setIsAlive(isAlive.equals("true"));
                    player1.getSniper().setIsActive(isActive.equals("true"));
                    cell.setPiece(player1.getSniper());
                }
            }

            scanner.next();
            scanner.next();

            int x1 = scanner.nextInt();

            if(x1!=0){
                int y = scanner.nextInt();
                int score = scanner.nextInt();
                int chance = scanner.nextInt();
                int diceNumber = scanner.nextInt();
                Cell cell = board.getCell(x1, y);
                Prize prize = new Prize(cell, score, chance, diceNumber);
                player1.getThief().setCurrentPrize(prize);
            }

            scanner.nextLine();
            scanner.next();
            scanner.next();

            for(int i=0 ; i<4 ; i++){
                scanner.next();
                scanner.next();

                int x = scanner.nextInt();
                int y = scanner.nextInt();
                Cell cell = board.getCell(x , y);
                String isAlive = scanner.next();
                String isActive = scanner.next();

                if(i==0) {
                    player2.getBomber().setCurrentCell(cell);
                    player2.getBomber().setIsAlive(isAlive.equals("true"));
                    player2.getBomber().setIsActive(isActive.equals("true"));
                    cell.setPiece(player2.getBomber());
                }

                if(i==1){
                    player2.getThief().setCurrentCell(cell);
                    player2.getThief().setIsAlive(isAlive.equals("true"));
                    player2.getThief().setIsActive(isActive.equals("true"));
                    cell.setPiece(player2.getThief());
                }

                if(i==2){
                    player2.getHealer().setCurrentCell(cell);
                    player2.getHealer().setIsAlive(isAlive.equals("true"));
                    player2.getHealer().setIsActive(isActive.equals("true"));
                    cell.setPiece(player2.getHealer());
                }

                if(i==3) {
                    player2.getSniper().setCurrentCell(cell);
                    player2.getSniper().setIsAlive(isAlive.equals("true"));
                    player2.getSniper().setIsActive(isActive.equals("true"));
                    cell.setPiece(player2.getSniper());
                }
            }

            scanner.next();
            scanner.next();

            int x2 = scanner.nextInt();

            if(x2!=0){
                int y = scanner.nextInt();
                int score = scanner.nextInt();
                int chance = scanner.nextInt();
                int diceNumber = scanner.nextInt();
                Cell cell = board.getCell(x2, y);
                Prize prize = new Prize(cell, score, chance, diceNumber);
                player2.getThief().setCurrentPrize(prize);
            }

            scanner.nextLine();
            scanner.next();

            int turn = scanner.nextInt();

            scanner.nextLine();
            scanner.next();

            int diceNumber = scanner.nextInt();
            if (diceNumber==0)
            {
                if (turn%2==0)
                    player2.setDicePlayedThisTurn(false);
                else
                    player1.setDicePlayedThisTurn(false);
            }
            else
            {
                if (turn%2==0) {
                    player2.setDicePlayedThisTurn(true);
                    player2.setMoveLeft(diceNumber);
                }
                else {
                    player1.setDicePlayedThisTurn(true);
                    player1.setMoveLeft(diceNumber);
                }
            }

            scanner.nextLine();
            scanner.next();

            for(int i=0 ; i<6 ; i++){
                int dice , chance ;
                dice = scanner.nextInt();
                scanner.next();
                chance = scanner.nextInt();
                scanner.next();

                player1.getDice().setChance(dice , chance);
            }

            scanner.nextLine();
            scanner.next();

            for(int i=0 ; i<6 ; i++){
                int dice , chance ;
                dice = scanner.nextInt();
                scanner.next();
                chance = scanner.nextInt();
                scanner.next();

                player2.getDice().setChance(dice , chance);
            }

            scanner.nextLine();
            scanner.next();
            scanner.next();

            int score1 = scanner.nextInt();
            player1.setScore(score1);

            scanner.nextLine();
            scanner.next();
            scanner.next();

            int score2 = scanner.nextInt();
            player2.setScore(score2);

            GameState newGameState = new GameState(board, player1, player2);
            newGameState.setTurn(turn);

            scanner.close();

            return newGameState;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.err.println("could not find player file");
            System.exit(-2);
        }
        return null;
    }

    public void deleteSavedGames (GameState gameState){
        String fileName = gameState.getPlayer1().getId() + "-" + gameState.getPlayer2().getId();
        File gameFile = null;
        for (File file : Objects.requireNonNull(savedGames.listFiles())) {
            if (file.getName().equals(fileName))
                gameFile = file;
        }
        gameFile.delete();
    }

    /**
     * if player does not have a file, create one.
     * <p>
     * else update his/her file.
     * <p>
     * add your codes in "try" block .
     */
    public void savePlayer(Player player) {
        try {
            File file = getPlayerFile(player.getName());
            if(file == null){
                file = new File(playersDirectory.getPath() + "//" + player.getName());
                PrintStream printStream = new PrintStream(new FileOutputStream(file));
                printStream.println(player.getId() + " " + 0);
                printStream.close();
            }

            Scanner scanner = new Scanner(file);
            scanner.next();
            int score = scanner.nextInt();
            scanner.close();

            file.delete();
            file = new File(playersDirectory.getPath() + "//" + player.getName());
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            printStream.println(player.getId() + " " + (player.getScore()+score));
            printStream.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("could not find player file");
            System.exit(-2);
        }
    }

    /**
     * give you a name (player name), search for its file.
     * return the file if exist.
     * return null if not.
     */
    private File getPlayerFile(String name) {
        for (File file : playersDirectory.listFiles()) {
            if(file.getName().equals(name))
                return file;
        }
        return null;
    }

    /**
     * at the end of the game save game details
     */
    public void archive(Player player1, Player player2) {
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(archiveFile, true));
            printStream.print(player1.getName()+" : "+player1.getScore() + "    " +player2.getName()+" : "+player2.getScore() + "   ");

            if(player1.getScore()==player2.getScore())
                printStream.println("DRAW");

            else if(player1.getScore()>player2.getScore())
                printStream.println("WINNER : " + player1.getName());

            else
                printStream.println("WINNER : " + player2.getName());
            printStream.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
