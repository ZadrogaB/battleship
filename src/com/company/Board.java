package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    private char[][] board = new char[10][10];
    List<Ship> playerShips = createShips();
    List<Ship> computerShips = createShips();
    Scanner scanner = new Scanner(System.in);

    public void fillBoardWithWater(){
        int rozmiar = board.length;

        for (int row=0; row<rozmiar; row++){
            for (int column=0; column<rozmiar; column++){
                board[row][column]='W';
            }
        }
    }

    public void printBoard(){
        int rozmiar = board.length;

        System.out.print("\t");
        for (int i=0; i<rozmiar; i++){ //drukuje nagłówek kolumn
            System.out.print(i+"\t");
        }
        System.out.println();
        for (int row=0; row<rozmiar;row++){
            System.out.print(row+":\t");
            for (int column=0; column<rozmiar; column++){
                System.out.print(board[row][column]+"\t");
            }
            System.out.println();
        }
    }

    public void placeShips(){

        int row, column;
        String isHorizontal;
        boolean horizontal;

        for (Ship ship : playerShips) {
            System.out.println("Wybierz położenie statku o wielkości " + ship.getNumberOfSquares());

            System.out.println("W którym rzędzie ma być statek?");
            row = scanner.nextInt();
            System.out.println("W której kolumnie ma być statek?");
            column = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Statek ma być położony horyzontalnie? (T/N)");
            //isHorizontal = scanner.nextLine();
            isHorizontal="N";

            if(isHorizontal.equals("N")){
                for (int i=0; i<ship.getNumberOfSquares(); i++) {
                    ship.setHorizontal(false);
                    board[row+i][column] = 'S';
                    ship.setPositions(row+i, column);
                }
            } else if (isHorizontal.equals("T")){
                for (int i=0; i<ship.getNumberOfSquares(); i++) {
                    ship.setHorizontal(true);
                    board[row][column+i] = 'S';
                    ship.setPositions(row+i, column);
                }
            }
            printBoard();
        }
    }

    public void shoot(){
        int row, column;

        System.out.println("W który rząd oddać strzał?");
        row = scanner.nextInt();
        System.out.println("W którą kolumnę oddać strzał");
        column = scanner.nextInt();
        scanner.nextLine();
        board[row][column] = 'X';
        checkIfHit(row, column);
        printBoard();

    }

    public void checkIfHit(int row, int column){
        for (Ship ship : playerShips){
            List<ShipUnitPosition> positions = ship.getPositions();
            for(ShipUnitPosition shipUnitPosition : positions){
                if(shipUnitPosition.getColumn()==column && shipUnitPosition.getRow()==row){
                    ship.setLife(ship.getLife()-1);
                    System.out.println("! ! ! HIT ! ! !");
                    System.out.println("Trafionemu statkowi potało: " + ship.getLife() + " żyć!");
                }
            }
        }
        playerShips.removeIf(ship -> ship.getLife()==0);
        System.out.println("Graczowi pozostało " + playerShips.size() + " statków");
    }

    public List<Ship> createShips(){
        List<Ship> shipList = new ArrayList<>();
        Ship shipOne = new Ship(1);
        Ship shipTwo = new Ship(2);
        Ship shipThree = new Ship(3);
        Ship shipFour = new Ship(4);
        Ship shipFive = new Ship(5);
        shipList.add(shipOne);
        shipList.add(shipTwo);
        shipList.add(shipThree);
        shipList.add(shipFour);
        shipList.add(shipFive);
        return shipList;
    }
}
