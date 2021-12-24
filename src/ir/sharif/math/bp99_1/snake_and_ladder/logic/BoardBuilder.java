package ir.sharif.math.bp99_1.snake_and_ladder.logic;

import ir.sharif.math.bp99_1.snake_and_ladder.model.Board;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Cell;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Color;
import ir.sharif.math.bp99_1.snake_and_ladder.model.Wall;
import ir.sharif.math.bp99_1.snake_and_ladder.model.prizes.Prize;
import ir.sharif.math.bp99_1.snake_and_ladder.model.transmitters.*;

import java.util.*;


public class BoardBuilder {

    private String BoardString;

    public BoardBuilder(String src) {
        this.BoardString = src;
    }

    /**
     * give you a string in constructor.
     * <p>
     * you should read the string and create a board according to it.
     */

    public Board build() {

        Board board = new Board();
        String[] words = this.BoardString.split("\n| ");
        int n=Integer.parseInt(words[1]) , m=Integer.parseInt(words[2]) , ind=4;

        for(int i=1 ; i<=n ; i++)
            for(int j=1 ; j<=m ; j++){
                Cell cell = new Cell(Color.valueOf(words[ind]) , i , j);
                board.getCells().add(cell);
                ind++;
            }

        for(int i=1 ; i<=n ; i++)
            for(int j=1 ; j<=m ; j++){
                Cell cell = board.getCell(i , j);

                if(i-1>=1){
                    Cell crnt = board.getCell(i-1 , j);
                    cell.getAdjacentCells().add(crnt);
                }

                if(i+1<=n){
                    Cell crnt = board.getCell(i+1 , j);
                    cell.getAdjacentCells().add(crnt);
                }

                if(j-1>=1){
                    Cell crnt = board.getCell(i , j-1);
                    cell.getAdjacentCells().add(crnt);
                }

                if(j+1<=m){
                    Cell crnt = board.getCell(i , j+1);
                    cell.getAdjacentCells().add(crnt);
                }

                for(int k=0 ; k<cell.getAdjacentCells().size() ; k++)
                    cell.getAdjacentOpenCells().add(cell.getAdjacentCells().get(k));
            }

        ind++;
        int cntStart = Integer.parseInt(words[ind]);
        ind+=2;

        for(int i=0 ; i<cntStart ; i++){
            int x=Integer.parseInt(words[ind]) , y=Integer.parseInt(words[ind+1]) ;
            Cell cell = board.getCell(x , y);
            board.getStartingCells().put(cell , Integer.parseInt(words[ind+2]));
            ind+=3;
        }

        ind++;
        int cntWalls = Integer.parseInt(words[ind]);
        ind+=2;

        for(int i=0 ; i<cntWalls ; i++) {
            int x1=Integer.parseInt(words[ind]) , y1=Integer.parseInt(words[ind+1]);
            Cell cell1 = board.getCell(x1 , y1);
            int x2=Integer.parseInt(words[ind+2]) , y2=Integer.parseInt(words[ind+3]);
            Cell cell2 = board.getCell(x2 , y2);
            Wall wall = new Wall(cell1 , cell2);
            board.getWalls().add(wall);
            ind+=4;

            for(int j=0 ; j<cell1.getAdjacentOpenCells().size() ; j++)
                if(cell1.getAdjacentOpenCells().get(j).equals(cell2))
                    cell1.getAdjacentOpenCells().remove(j);


            for(int j=0 ; j<cell2.getAdjacentOpenCells().size() ; j++)
                if(cell2.getAdjacentOpenCells().get(j).equals(cell1))
                    cell2.getAdjacentOpenCells().remove(j);
        }

        ind++;
        int CntTrans = Integer.parseInt(words[ind]);
        ind+=2;

        for(int i=0 ; i<CntTrans ; i++){
            int x1=Integer.parseInt(words[ind]) , y1=Integer.parseInt(words[ind+1]);
            Cell cell1 = board.getCell(x1 , y1);

            int x2=Integer.parseInt(words[ind+2]) , y2=Integer.parseInt(words[ind+3]);
            Cell cell2 = board.getCell(x2 , y2);

            if(words[ind+4].equals("o")){
                Ordinary trans = new Ordinary(cell1 , cell2);
                board.getTransmitters().add(trans);
                trans.setType(words[ind+4]);
            }

            if(words[ind+4].equals("m")){
                Magical trans = new Magical(cell1 , cell2);
                board.getTransmitters().add(trans);
                trans.setType(words[ind+4]);
            }

            if(words[ind+4].equals("d")){
                Deathful trans = new Deathful(cell1 , cell2);
                board.getTransmitters().add(trans);
                trans.setType(words[ind+4]);
            }

            if(words[ind+4].equals("e")){
                EarthWorm trans = new EarthWorm(cell1 , cell2);
                board.getTransmitters().add(trans);
                trans.setType(words[ind+4]);
            }

            ind+=5;
        }

        ind++;
        int CntPrize = Integer.parseInt(words[ind]);
        ind+=2;

        for(int i=0 ; i<CntPrize ; i++) {
            int x=Integer.parseInt(words[ind]) , y=Integer.parseInt(words[ind+1]);
            Cell cell = board.getCell(x , y);
            int score=Integer.parseInt(words[ind+2]) , chance=Integer.parseInt(words[ind+3]) , side=Integer.parseInt(words[ind+4]);
            Prize prize = new Prize(cell , score , chance , side);
            cell.setPrize(prize);
            ind+=5;
        }

        for(Cell cell : board.getCells()) {
            cell.setBoard(board);
        }

        return board;
    }
}
