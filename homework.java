package com.ms;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class homework {
    private static final int  MAX = 456976;
    private static final int MIN = -456976;

    static String inputBytes;

    //n - width and height of the square board
    //p - number of fruit types
    static int n;
    static int p;
    static float time_remaining;

    static int[][] board;
    static char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'};
    static int count = 0;

    static int score1 = 0, score2 = 0;
    static Fruit best, best2;

    public static void main(String[] args) throws IOException {
        readInput();
        int[][] tempBoard = copyBoard(board);
        Fruit bestFruit = findBestFruitLocation(tempBoard);
        applyGravity(board, bestFruit.i, bestFruit.j);
        writeOutputFile(bestFruit.j, bestFruit.i+1);
    }

    private static int[][] copyBoard(int[][] b) {
        int temp[][] = new int[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                temp[i][j] = b[i][j];
            }
        }
        return temp;
    }

    private static void readInput() throws IOException {
        try {
            inputBytes = new String(Files.readAllBytes(Paths.get("input.txt")));
            String[] inputMatrixArray = inputBytes.split("\n");
            n = Integer.parseInt(inputMatrixArray[0].trim());
            p = Integer.parseInt(inputMatrixArray[1].trim());
            time_remaining = Float.parseFloat(inputMatrixArray[2].trim());
            board = new int[n][n];
            for (int i = 0; i < n; i++) {
                String[] inputArrayRow = inputMatrixArray[i+3].split("", n);
                for (int j = 0; j < n; j++) {
                    try {
                        board[i][j] = Integer.parseInt(inputArrayRow[j].trim());
                    } catch (NumberFormatException e) {
                        board[i][j] = '*';
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Fruit findBestFruitLocation(int[][] iBoard) {
        List<Fruit> fruitsList = new ArrayList<>();
        Fruit bestFruit = new Fruit(-1, -1, -1, -1);
        int[][] b = copyBoard(iBoard);
        fruitsList = getFruitsCluster(iBoard, fruitsList, b);
        bestFruit = minimax(iBoard, 0, true, MIN, MAX);
        return bestFruit;
    }

    private static Fruit minimax(int[][] iBoard, int depth, boolean turn, int alpha, int beta) {
        List<Fruit> fruitsList = new ArrayList<>();
        int[][] sortBoard = copyBoard(iBoard);
        fruitsList = getFruitsCluster(iBoard, fruitsList, sortBoard);

        if(depth == 3) {
            if(turn) return best;
            else return best2;
        }

        int[][] minimaxBoard = copyBoard(iBoard);

        if(turn) {
            best = new Fruit(-1, -1, -1, -1);
            best.score = MIN;
            for (int i = 0; i < fruitsList.size(); i++) {
                Fruit temp = fruitsList.get(i);
                findCluster(minimaxBoard, temp.i, temp.j, temp.fruit_type);
                score1 += Math.pow(temp.cluster_count, 2);
                applyGravity(minimaxBoard, temp.i, temp.j);
                minimax(minimaxBoard, depth + 1, !turn, alpha, beta);
                if (score1 > best.score) {
                    best.score = score1;
                    best.i = temp.i;
                    best.j = temp.j;
                    best.fruit_type = temp.fruit_type;
                }
                alpha = alpha > best.score ? alpha : best.score;

                if(beta <= alpha) break;
            }
            return best;
        }

        else {
            best2 = new Fruit(-1, -1, -1, -1);
            for (int i = 0; i < fruitsList.size(); i++) {
                Fruit temp = fruitsList.get(i);
                findCluster(minimaxBoard, temp.i, temp.j, temp.fruit_type);
                score2 += Math.pow(temp.cluster_count, 2);
                applyGravity(minimaxBoard, temp.i, temp.j);
                minimax(minimaxBoard, depth + 1, !turn, alpha, beta);
                if(score2 < best2.score) {
                    best2.score = score2;
                    best2.i = temp.i;
                    best2.j = temp.j;
                    best2.fruit_type = temp.fruit_type;
                }
                beta = best2.score < beta ? best2.score : beta;
                if(beta <= alpha) break;
            }
            return best2;
        }
    }

    private static List<Fruit> getFruitsCluster(int[][] iBoard, List<Fruit> fruitsList, int[][] sortBoard) {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                count = 0;
                findCluster(sortBoard, i, j, sortBoard[i][j]);
                if(count > 0)
                    fruitsList.add(new Fruit(i, j, count, iBoard[i][j]));
            }
        }
        Collections.sort(fruitsList, (f1, f2) -> f2.cluster_count - f1.cluster_count);
        return fruitsList;
    }

    private static void applyGravity(int[][] iBoard, int i, int j) {
        findCluster(iBoard, i, j, iBoard[i][j]);
        for (int col = 0; col < n; col++) {
            for (int k = 0; k < n-1; k++) {
                for(int row=0; row < n-1-k; row++ ) {
                    if (iBoard[row + 1][col] == '*' && iBoard[row][col] != '*') {
                        iBoard[row + 1][col] = iBoard[row][col];
                        iBoard[row][col] = '*';
                    }
                }
            }
        }
    }

    private static void findCluster(int localBoard[][],  int i, int j, int fruit) {
        if(localBoard[i][j] != fruit || localBoard[i][j] == '*') return;

        if(localBoard[i][j] == fruit) {
            localBoard[i][j] = '*';
            count++;
        }

        if(fruit != '*') {
            if (j - 1 >= 0)
                findCluster(localBoard, i, j - 1, fruit);
            if (i - 1 >= 0)
                findCluster(localBoard, i - 1, j, fruit);
            if (j + 1 < n)
                findCluster(localBoard, i, j + 1, fruit);
            if (i + 1 < n)
                findCluster(localBoard, i + 1, j, fruit);
        }
    }

    private static void writeOutputFile(int col, int row) {
        try(FileWriter fileWriter = new FileWriter("output.txt")) {
            fileWriter.write(chars[col] + "");
            fileWriter.write(row + "\r\n");
            for(int i =0; i<n; i++) {
                for(int j=0; j<n; j++) {
                    if(board[i][j] == '*')
                        fileWriter.write('*');
                    else
                        fileWriter.write(board[i][j]+"");
                }
                fileWriter.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showBoard() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isMovesLeft(int[][] iBoard) {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(iBoard[i][j] != '*')
                    return true;
            }
        }
        return false;
    }
}

class Fruit {
    int i, j; //location of the fruit
    int fruit_type; //Type of the fruit - 0,1,2,3,4,..9
    int cluster_count; //Number of same fruits in a cluster
    int score;
    Fruit(int i, int j, int c, int f) {
        this.i = i;
        this.j = j;
        this.fruit_type = f;
        this.cluster_count = c;
    }
}