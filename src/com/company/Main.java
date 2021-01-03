package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        Board boardClass = new Board();

	// write your code here
        boardClass.fillBoardsWithWater();
        //boardClass.printBoard();

        boardClass.placeShipComputer();
        boardClass.printComputerBoard();
        //boardClass.placeShipsPlayer();

        for (int i=0;;i++){
            boardClass.shoot();
        }
    }


}
