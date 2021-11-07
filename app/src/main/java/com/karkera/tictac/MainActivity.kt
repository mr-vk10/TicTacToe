package com.karkera.tictac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnClick(view: View){

        val btnSelected = view as Button

        var cellID = 0

        when(btnSelected.id){
            R.id.btn1 -> cellID = 1  //or btn1.id -> cellID = 1
            R.id.btn2 -> cellID = 2
            R.id.btn3 -> cellID = 3
            R.id.btn4 -> cellID = 4
            R.id.btn5 -> cellID = 5
            R.id.btn6 -> cellID = 6
            R.id.btn7 -> cellID = 7
            R.id.btn8 -> cellID = 8
            R.id.btn9 -> cellID = 9
        }
        //Log.d("btnClicked:",btnSelected.id.toString())
        //Log.d("cellID:",cellID.toString())
        playGame(cellID,btnSelected)
    }

    var activePlayer = 1
    var winner = -1
    var p1Cells = ArrayList<Int>(5)
    var p2Cells = ArrayList<Int>(5)
    var p1wins = 0
    var p2wins = 0

    fun playGame(cellID:Int,btnSelected:Button){

        if(activePlayer==1){
            btnSelected.text = "X"
            btnSelected.setBackgroundResource(R.color.blue)
            p1Cells.add(cellID)
            btnSelected.isEnabled = false
            checkWinner(p1Cells)
            activePlayer=2
            if(winner==-1)
                bestMove()
        }
        else{
            btnSelected.text = "O"
            btnSelected.setBackgroundResource(R.color.red)
            p2Cells.add(cellID)
            btnSelected.isEnabled = false
            checkWinner(p2Cells)
            activePlayer=1
        }
    }

    fun checkWinner(T : ArrayList<Int>){

        if (T.containsAll(listOf<Int>(1,2,3)) || T.containsAll(listOf<Int>(1,5,9))
            || T.containsAll(listOf<Int>(1,4,7)) || T.containsAll(listOf<Int>(2,5,8))
            || T.containsAll(listOf<Int>(3,6,9)) || T.containsAll(listOf<Int>(4,5,6))
            || T.containsAll(listOf<Int>(7,8,9)) || T.containsAll(listOf<Int>(3,5,7))){
            winner = activePlayer
            if(activePlayer==1) p1wins++ else p2wins++
            Toast.makeText(this,"Player $winner WON the game!!!",Toast.LENGTH_LONG).show()
            off()
            endGame()
        }
    }
//Change off to end game....
    fun off(){
        btn1.isEnabled = false
        btn2.isEnabled = false
        btn3.isEnabled = false
        btn4.isEnabled = false
        btn5.isEnabled = false
        btn6.isEnabled = false
        btn7.isEnabled = false
        btn8.isEnabled = false
        btn9.isEnabled = false
    }



    fun bestMove(){

        var  emptyCells = ArrayList<Int>()
        //instead of -Infinity -300000 becoz total moves in tic tac toe is 255168
        var bestScore:Int = -300000
        var score:Int = 0;

        //We don't want 0 as random number.
        for(i in 1..9){
            if(!(p1Cells.contains(i)||p2Cells.contains(i)))
                emptyCells.add(i)
        }

        var cellID:Int = 10

        //autoPlay will be called even after all cells are selected.
        //This will cause emptyCells[randIndex] to throw exception as no cells are empty
        if(emptyCells.size!=0){
            //Iterating through each empty cells to see all possible outcomes
            for(i in emptyCells){
                p2Cells.add(i)
                score = minimax(false)
                p2Cells.remove(i)
                if(score > bestScore){
                    bestScore = score
                    cellID = i
                }
            }
        }


        //ANOTHER METHOD
        //val randIndex = List(1){ Random.nextInt(1,emptyCells.size+1)}
        //randIndex is a list of size 1 therefore [0]
        //val cellID = emptyCells[randIndex[0]]

        //In case there is a tie. playGame is not executed.
        var (btnSelected:Button,tie) = when{
            cellID == 1 -> btn1 to 0
            cellID == 2 -> btn2 to 0
            cellID == 3 -> btn3 to 0
            cellID == 4 -> btn4 to 0
            cellID == 5 -> btn5 to 0
            cellID == 6 -> btn6 to 0
            cellID == 7 -> btn7 to 0
            cellID == 8 -> btn8 to 0
            cellID == 9 -> btn9 to 0
            else -> btn1 to 1
        }

        if(tie!=1)
            playGame(cellID,btnSelected)
        else{
            Toast.makeText(this,"TIE!",Toast.LENGTH_LONG).show()
            endGame()
        }
    }

    fun minimax(isMaximizing:Boolean):Int{

        //This is the terminal condition
        var result = checkWin()
        if(result!=2) return result

        if(isMaximizing){
            var bestScore:Int = -300000
            var score:Int = 0;
            var  emptyCells = ArrayList<Int>()

            for(i in 1..9){
                if(!(p1Cells.contains(i)||p2Cells.contains(i)))
                    emptyCells.add(i)
            }

            if(emptyCells.size!=0){
                //Iterating through each empty cells to see all possible outcomes
                for(i in emptyCells){
                    p2Cells.add(i)
                    score = minimax(false)
                    p2Cells.remove(i)
                    if(score > bestScore){
                        bestScore = score
                    }
                }
            }
            return bestScore
        }

        else{
            var bestScore:Int = 300000
            var score:Int = 0;
            var  emptyCells = ArrayList<Int>()

            for(i in 1..9){
                if(!(p1Cells.contains(i)||p2Cells.contains(i)))
                    emptyCells.add(i)
            }

            if(emptyCells.size!=0){
                //Iterating through each empty cells to see all possible outcomes
                for(i in emptyCells){
                    p1Cells.add(i)
                    score = minimax(true)
                    p1Cells.remove(i)
                    if(score < bestScore){
                        bestScore = score
                    }
                }
            }
            return bestScore
        }
    }

    //This check winner is for minimax to see all possibilities.
    fun checkWin():Int{
        /*
        1:Won
        0:Tie
        -1:Loss
        2:None
        * */
        var  emtCells = ArrayList<Int>()
        var T = ArrayList<Int>()
        T=p2Cells
        if (T.containsAll(listOf<Int>(1,2,3)) || T.containsAll(listOf<Int>(1,5,9))
            || T.containsAll(listOf<Int>(1,4,7)) || T.containsAll(listOf<Int>(2,5,8))
            || T.containsAll(listOf<Int>(3,6,9)) || T.containsAll(listOf<Int>(4,5,6))
            || T.containsAll(listOf<Int>(7,8,9)) || T.containsAll(listOf<Int>(3,5,7))){
            return 1    //Meaning computer Won
        }

        T=p1Cells
        if (T.containsAll(listOf<Int>(1,2,3)) || T.containsAll(listOf<Int>(1,5,9))
            || T.containsAll(listOf<Int>(1,4,7)) || T.containsAll(listOf<Int>(2,5,8))
            || T.containsAll(listOf<Int>(3,6,9)) || T.containsAll(listOf<Int>(4,5,6))
            || T.containsAll(listOf<Int>(7,8,9)) || T.containsAll(listOf<Int>(3,5,7))){
            return -1    //Meaning Computer Lost
        }

        for(i in 1..9){
            if(!(p1Cells.contains(i)||p2Cells.contains(i)))
                emtCells.add(i)
        }

        if(emtCells.size==0) return 0

        return 2
    }

    fun endGame(){

        //Make ending elements visible
        val points = findViewById<TextView>(R.id.scoreTxt)
        points.text = "P1:$p1wins  P2:$p2wins"
        points.visibility = View.VISIBLE
        val text = findViewById<TextView>(R.id.contiTxT)
        text.visibility = View.VISIBLE
        val yesBtn = findViewById<Button>(R.id.yes)
        yesBtn.visibility = View.VISIBLE
        val noBtn = findViewById<Button>(R.id.no)
        noBtn.visibility = View.VISIBLE

    }

    fun continueGame(view: View){
        resetGame()
    }

    fun quitGame(view: View){
        p1wins = 0
        p2wins = 0
        resetGame()
    }

    fun resetGame(){
        activePlayer = 1
        winner = -1
        p1Cells.clear()
        p2Cells.clear()

        for(cellID in 1..9){
            var btnSelected = when(cellID){
                1 -> btn1
                2 -> btn2
                3 -> btn3
                4 -> btn4
                5 -> btn5
                6 -> btn6
                7 -> btn7
                8 -> btn8
                9 -> btn9
                else -> btn1
            }
            btnSelected.text = ""
            btnSelected.setBackgroundResource(R.color.btnColors)
            btnSelected.isEnabled = true
        }

        //Make ending elements invisible again
        val points = findViewById<TextView>(R.id.scoreTxt)
        points.visibility = View.INVISIBLE
        val text = findViewById<TextView>(R.id.contiTxT)
        text.visibility = View.INVISIBLE
        val yesBtn = findViewById<Button>(R.id.yes)
        yesBtn.visibility = View.INVISIBLE
        val noBtn = findViewById<Button>(R.id.no)
        noBtn.visibility = View.INVISIBLE
    }
}