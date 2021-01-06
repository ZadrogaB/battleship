package com.company;

import java.util.*;

public class Board {
    private char[][] board = new char[10][10];
    private char[][] computerBoard = new char[10][10];
    List<Ship> playerShips = createShips();
    List<Ship> computerShips = createShips();
    Scanner scanner = new Scanner(System.in);

    public void fillBoardsWithWater(){
        int rozmiar = board.length;

        for (int row=0; row<rozmiar; row++){
            for (int column=0; column<rozmiar; column++){
                board[row][column]='W';
                computerBoard[row][column]='W';
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

    public void printComputerBoard(){
        int rozmiar = computerBoard.length;

        System.out.print("\t");
        for (int i=0; i<rozmiar; i++){ //drukuje nagłówek kolumn
            System.out.print(i+"\t");
        }
        System.out.println();
        for (int row=0; row<rozmiar;row++){
            System.out.print(row+":\t");
            for (int column=0; column<rozmiar; column++){
                System.out.print(computerBoard[row][column]+"\t");
            }
            System.out.println();
        }
    }

    public void placeShipsPlayer(){

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
                    setNeighbours(ship, row, column, ship.isHorizontal()); //Ship ship, int row, int column, boolean horizontal
                }
            } else if (isHorizontal.equals("T")){
                for (int i=0; i<ship.getNumberOfSquares(); i++) {
                    ship.setHorizontal(true);
                    board[row][column+i] = 'S';
                    ship.setPositions(row, column+i);
                    setNeighbours(ship, row, column, ship.isHorizontal());
                }
            }
            printBoard();
        }
    } //Do poprawy (dodać brak kolizji + zmienić rysowanie na metodę drawShip)

    public void placeShipComputer(){
        int row, column;
        Random random = new Random();
        boolean testInterference;

        for (Ship ship : computerShips) {
            //System.out.println("Losowanie pozycji statku: " + ship.getNumberOfSquares());
            boolean horizontal = random.nextBoolean();

            do {
                ship.deleteAllPositions();
                if (horizontal) {
                    row = getRandomNumberInRange(0, 9);
                    column = getRandomNumberInRange(0, 9 - ship.getNumberOfSquares());
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setPositions(row, column + i);
                        setNeighbours(ship, row, column, true);
                    }
                } else {
                    row = getRandomNumberInRange(0, 9 - ship.getNumberOfSquares());
                    column = getRandomNumberInRange(0, 9);
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setPositions(row + i, column);
                        setNeighbours(ship, row, column, false);
                    }
                }
                testInterference=isShipInterfere(computerShips, ship);
            } while (testInterference);
            ship.setHorizontal(horizontal);
            //System.out.println("Row = " + row + ", Column = " + column);
            drawShip(ship);
            //printComputerBoard();
        }
        //computerShips.get(2).printNeighbours();
    }

    public void drawShip(Ship ship){
        int row, column;
        Set<UnitPosition> drawPositions = ship.getPositions();

        for (UnitPosition unitPosition : drawPositions){
            row = unitPosition.getRow();
            column = unitPosition.getColumn();
            computerBoard[row][column] = 'C';
        }
    }

    public void shoot(){
        int row, column;

        System.out.println("W który rząd oddać strzał?");
        row = scanner.nextInt();
        System.out.println("W którą kolumnę oddać strzał");
        column = scanner.nextInt();
        scanner.nextLine();
        computerBoard[row][column] = 'X';
        checkIfHit(row, column, false);
        printComputerBoard();

    } // Shotter

    public void shootComputer(){
        int row, column;
        row = getRandomNumberInRange(0,9);
        column = getRandomNumberInRange(0,9);
        board[row][column]='X';
        checkIfHit(row, column, true);
        printBoard();
    } // Shotter

    public void checkIfHit(int row, int column, boolean isComputer){

        if(isComputer) {
            for (Ship ship : playerShips) {
                Set<UnitPosition> positions = ship.getPositions();
                for (UnitPosition unitPosition : positions) {
                    if (unitPosition.getColumn() == column && unitPosition.getRow() == row) {
                        ship.setLife(ship.getLife() - 1);
                        System.out.println("! ! ! HIT ! ! !");
                        System.out.println("Trafionemu statkowi potało: " + ship.getLife() + " żyć!");
                    }
                }
            }
        } else {
            for (Ship ship : computerShips) {
                Set<UnitPosition> positions = ship.getPositions();
                for (UnitPosition unitPosition : positions) {
                    if (unitPosition.getColumn() == column && unitPosition.getRow() == row) {
                        ship.setLife(ship.getLife() - 1);
                        System.out.println("! ! ! HIT ! ! !");
                        System.out.println("Trafionemu statkowi potało: " + ship.getLife() + " żyć!");
                    }
                }
            }
        }
        playerShips.removeIf(ship -> ship.getLife()==0);
        System.out.println("Graczowi pozostało " + playerShips.size() + " statków");
    } // Shotter

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

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    } //Util

    public void setNeighbours (Ship ship, int row, int column, boolean horizontal){
        for (int i=0; i<ship.getNumberOfSquares(); i++) {
           //System.out.println("Liczba kratek statku = " + ship.getNumberOfSquares() + " Iteracja = " + i);
            if (horizontal) {
                ship.setNeighbours(row, column + i);//pozycja statku
                if (row != 9) {
                    ship.setNeighbours(row + 1, column + i);//rząd poniżej
                }
                if (row != 0) {
                    ship.setNeighbours(row - 1, column + i);//rząd powyżej
                }
                if (column+i != 9 && i == ship.getNumberOfSquares()-1 ) {
                    ship.setNeighbours(row, column + i + 1);//po prawej
                }
                if (column != 0 && i == 0) {
                    ship.setNeighbours(row, column - 1);//po lewej
                }
            } else {
                ship.setNeighbours(row + i, column);//pozycja statku
                if (column != 9) {
                    ship.setNeighbours(row + i, column + 1);//kolumna po prawej
                }
                if (column != 0) {
                    ship.setNeighbours(row + i, column - 1);//kolumna po lewej
                }
                if (row+i != 9 && i == ship.getNumberOfSquares()-1) {
                    ship.setNeighbours(row + i + 1, column); //poniżej statku
                }
                if (row != 0 && i == 0){
                    ship.setNeighbours(row - 1, column); //powyżej statkiem
                }
            }
        }
    }

    public boolean isShipInterfere(List<Ship> shipList, Ship newShip){
        boolean isShipInterfere = false;

        for (Ship ship : shipList) {
            if (ship.getNumberOfSquares() < newShip.getNumberOfSquares()) {
                for (UnitPosition newShipUnitPosition : newShip.getPositions()) {
                    for (UnitPosition shipNeighboursUnitPosition : ship.getNeighbours()) {
                        if (shipNeighboursUnitPosition.getRow() == newShipUnitPosition.getRow()
                                && shipNeighboursUnitPosition.getColumn() == newShipUnitPosition.getColumn()) {
                            isShipInterfere = true;
                        }
                    }
                }
            }
        }

       // System.out.println("isShipInterfere = "+isShipInterfere);

        return  isShipInterfere;
    }
}
