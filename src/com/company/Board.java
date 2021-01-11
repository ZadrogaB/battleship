package com.company;

import java.util.*;

public class Board {
    private char[][] playerBoard = new char[10][10];
    private char[][] computerBoard = new char[10][10];
    List<Ship> playerShips = createShips();
    List<Ship> computerShips = createShips();
    Set<Ship> deadShipsComputer = new HashSet<>();
    Set<Ship> deadShipsPlayer = new HashSet<>();
    Scanner scanner = new Scanner(System.in);
    Utils utils = new Utils();

    public void fillBoardsWithWater(){
        for (int row=0; row<playerBoard.length; row++){
            for (int column=0; column<playerBoard.length; column++){
                playerBoard[row][column]='W';
                computerBoard[row][column]='W';
            }
        }
    }

    public void printPlayerBoard(){
        printBoardCommon(playerBoard.length, false);
    }

    public void printComputerBoard(){
        printBoardCommon(computerBoard.length, true);
    }

    public void printBoardCommon(int length, boolean isComputer){
        System.out.print("\t");
        for (int i=0; i<length; i++){ //drukuje nagłówek kolumn
            System.out.print(i+"\t");
        }
        System.out.println();
        for (int row=0; row<length;row++){
            System.out.print(row+":\t");
            for (int column=0; column<length; column++){
                if(isComputer==true) {
                    System.out.print(computerBoard[row][column] + "\t");
                } else {
                    System.out.print(playerBoard[row][column] + "\t");

                }
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
            boolean testInterference;

            do {
                ship.deleteAllPositions();

                System.out.println("W którym rzędzie ma być statek?");
                row = scanner.nextInt();
                System.out.println("W której kolumnie ma być statek?");
                column = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Statek ma być położony horyzontalnie? (T/N)");
//                isHorizontal = scanner.nextLine();
                isHorizontal = "N";

                if (isHorizontal.equals("N")) {
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setHorizontal(false);
                        //board[row + i][column] = 'S';
                        ship.setPositions(row + i, column);
                        setNeighbours(ship, row, column, false); //Ship ship, int row, int column, boolean horizontal
                    }
                } else {
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setHorizontal(true);
                        //board[row][column + i] = 'S';
                        ship.setPositions(row, column + i);
                        setNeighbours(ship, row, column, true);
                    }
                }
                testInterference=isShipInterfere(playerShips, ship);
            }while(testInterference);
            drawShip(ship, false);
        }
    }

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
                    row = utils.getRandomNumberInRange(0, 9);
                    column = utils.getRandomNumberInRange(0, 9 - ship.getNumberOfSquares());
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setPositions(row, column + i);
                        setNeighbours(ship, row, column, true);
                    }
                } else {
                    row = utils.getRandomNumberInRange(0, 9 - ship.getNumberOfSquares());
                    column = utils.getRandomNumberInRange(0, 9);
                    for (int i = 0; i < ship.getNumberOfSquares(); i++) {
                        ship.setPositions(row + i, column);
                        setNeighbours(ship, row, column, false);
                    }
                }
                testInterference=isShipInterfere(computerShips, ship);
            } while (testInterference);
            ship.setHorizontal(horizontal);
            drawShip(ship, true);
        }
    }

    public void drawShip(Ship ship, boolean isComputer){
        int row, column;
        Set<UnitPosition> drawPositions = ship.getPositions();

        for (UnitPosition unitPosition : drawPositions){
            row = unitPosition.getRow();
            column = unitPosition.getColumn();
            if(isComputer==false) {
                playerBoard[row][column] = 'S';
            } else {
                computerBoard[row][column] = 'C';
            }
        }
    }

    public void shootPlayer(){
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
        do {
            row = utils.getRandomNumberInRange(0, 9);
            column = utils.getRandomNumberInRange(0, 9);
        }while(playerBoard[row][column]=='X');
        playerBoard[row][column]='X';
        checkIfHit(row, column, true);
        printPlayerBoard();
    } // Shotter

    public void checkIfHit(int row, int column, boolean isComputer){

        if(isComputer) {
            checkIfHitCommon(playerShips, isComputer, row, column);
            playerShips.removeIf(ship -> ship.getLife()==0);
            System.out.println("Graczowi pozostało " + playerShips.size() + " statków");
        } else {
            checkIfHitCommon(computerShips, isComputer, row, column);
            computerShips.removeIf(ship -> ship.getLife()==0);
            System.out.println("Komputerowi pozostało " + computerShips.size() + " statków");
        }


    } // Shotter

    public void checkIfHitCommon(List<Ship> shipList, boolean isComputer, int row, int column) {
        for (Ship ship : shipList) {
            Set<UnitPosition> positions = ship.getPositions();
            for (UnitPosition unitPosition : positions) {
                if (unitPosition.getColumn() == column && unitPosition.getRow() == row) {
                    ship.setLife(ship.getLife() - 1);
                    System.out.println("! ! ! HIT ! ! !");
                    System.out.println("Trafionemu statkowi potało: " + ship.getLife() + " żyć!");
                    if (ship.getLife() == 0 && isComputer==false) {
                        deadShipsPlayer.add(ship);
                    } else if (ship.getLife() == 0 && isComputer==true){
                        deadShipsComputer.add(ship);
                    }
                }
            }
        }
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

    public void isWin(String player){
        boolean win = false;
        if(playerShips.size()==0||computerShips.size()==0){
            System.out.println("Zwycięzcą jest: " + player);
            System.exit(0);
        }
    }
}
