package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        Board boardClass = new Board();
        boolean isWin;

	// write your code here
        boardClass.fillBoardsWithWater();  //game controller
        //boardClass.printBoard();

        boardClass.placeShipComputer();
//        boardClass.printComputerBoard();
        boardClass.placeShipsPlayer();
//        boardClass.printBoard();

        for (int i=0;;i++){
            boardClass.printBoard();
            boardClass.shootPlayer();
            boardClass.isWin("Player");
            boardClass.shootComputer();
            boardClass.isWin("Computer");
        }
    }


}
