package ir.sharif.math.bp99_1.snake_and_ladder.model;


import java.util.Random;

public class Dice {

    /**
     * add some fields to store :
     * 1) chance of each dice number ( primary chance of each number, should be 1 )
     * currently our dice has 1 to 6.
     * 2) generate a random number
     * <p>
     * initialize these fields in constructor.
     */

    public int[] ChanceOf = new int[10];
    public int DiceSides = 6;
    public int[] SumChances = new int[10];
    public int LastDice = 0;

    public Dice() {
        this.SumChances[0] = 0 ;
        for(int i=1 ; i<=DiceSides ; i++) {
            this.ChanceOf[i] = 1;
            this.SumChances[i] = i ;
        }
    }

    /**
     * create an algorithm generate a random number(between 1 to 6) according to the
     * chance of each dice number( you store them somewhere)
     * return the generated number
     */

    public int roll() {
        Random random = new Random();
        int Roll = random.nextInt(this.SumChances[this.DiceSides])+1;

        for(int i=0 ; i<this.DiceSides ; i++) {
            if (Roll > this.SumChances[i] && Roll <= this.SumChances[i + 1]) {
                if(this.LastDice == i+1 && this.ChanceOf[i+1]<8){
                    this.addChance(i+1 , 1);

                    this.LastDice = 0;
                }

                else{
                    LastDice = i+1;
                }

                return i+1;
            }
        }

        return 0;
    }

    public void resetDice() {

        this.SumChances[0] = 0 ;
        for(int i=1 ; i<=DiceSides ; i++) {
            this.ChanceOf[i] = 1;
            this.SumChances[i] = i ;
        }

        this.LastDice = 0 ;
    }

    /**
     * give a dice number and a chance, you should UPDATE chance
     * of that number.
     * pay attention chance of none of the numbers must not be negetive(it can be zero)
     */

    public void addChance(int number, int chance) {
        this.ChanceOf[number] += chance;

        if(this.ChanceOf[number] < 0)
            this.ChanceOf[number] = 0;

        for(int i=1 ; i<=this.DiceSides ; i++)
            this.SumChances[i] = this.SumChances[i-1] + this.ChanceOf[i];
    }

    public void setChance(int diceNumber, int chance)
    {
        this.ChanceOf[diceNumber] = chance;

        for(int i=1 ; i<=this.DiceSides ; i++)
            this.SumChances[i] = this.SumChances[i-1] + this.ChanceOf[i];
    }


    /**
     * you should return the details of the dice number.
     * sth like:
     * "1 with #1 chance.
     * 2 with #2 chance.
     * 3 with #3 chance
     * .
     * .
     * . "
     * where #i is the chance of number i.
     */

    public String getDetails() {
        String Detail = "";

        for(int i=1 ; i<=this.DiceSides ; i++)
            Detail += i + " with " + this.ChanceOf[i] + " chance \n";

        return Detail;
    }
}
