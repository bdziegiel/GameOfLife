import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Graphic  {

    int[][] setNiezmienne(int width, int height) {
        int[][] niezmienne = new int[width][height];
        int X = width / 2;
        int Y = height / 2;

        setZeros(niezmienne);
        niezmienne[X][Y - 1] = 1;
        niezmienne[X + 1][Y] = 1;
        niezmienne[X + 1][Y + 1] = 1;
        niezmienne[X][Y + 2] = 1;
        niezmienne[X - 1][Y] = 1;
        niezmienne[X - 1][Y + 1] = 1;
        return niezmienne;
    }

    int[][] setRecznie(int width, int height, JButton[][] board){
        int[][] recznie = new int[width][height];
        setZeros(recznie);
         for(int i=0; i<width;i++){
             for(int j=0; j<height;j++){
                 if(board[i][j].getBackground()==Color.WHITE)
                     recznie[i][j]=0;
                 else if(board[i][j].getBackground()==Color.BLACK)
                     recznie[i][j]=1;
             }
         }
        return recznie;
    }

    int[][] setOscylator(int width, int height) {
        int[][] oscylator = new int[width][height];
        int X = width / 2;
        int Y = height / 2;

        setZeros(oscylator);
        oscylator[X - 1][Y - 1] = 1;
        oscylator[X - 1][Y - 2] = 1;
        oscylator[X - 1][Y - 3] = 1;
        return oscylator;
    }

    int[][] setGlider(int width, int height){
        int X = width / 2;
        int Y = height / 2;
        int[][] glider = new int[width][height];
        setZeros(glider);
        glider[X+1][Y-1]=1;
        glider[X+1][Y]=1;
        glider[X+1][Y+1]=1;
        glider[X][Y+1]=1;
        glider[X-1][Y]=1;

        return glider;
    }

    int[][] setLosowo(int width, int height) {
        Random rand = new Random();

        int[][] losowo = new int[width][height];
        setZeros(losowo);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                losowo[i][j] = rand.nextInt(2);
            }
        }
        return losowo;
    }

    void setBoard(int[][] array, JButton[][] board) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[array.length - 1].length; j++) {
                if (array[i][j] == 0) board[i][j].setBackground(Color.WHITE);
                else if (array[i][j] == 1) board[i][j].setBackground(Color.BLACK);
            }
        }
    }

    private void setZeros(int[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[tab.length - 1].length; j++) {
                tab[i][j] = 0;
            }
        }
    }

    int[][] setNextGen(int[][] area) {
        int cols = area.length;
        int rows = area[cols-1].length;
        int[][] nextGen = new int[cols][rows];
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    int neighbours = countNeighbors(area, i,j);
                    if(area[i][j]==0){
                        if(neighbours == 3 ) nextGen[i][j]=1;
                        else nextGen[i][j]=0;
                    }
                    else if(area[i][j]==1) {
                        if(neighbours==2 || neighbours ==3) nextGen[i][j]=1;
                        else nextGen[i][j]=0;
                    }
                }
            } return nextGen;
        }

    private int countNeighbors(int[][] area, int x, int y) {
        int[][] tmp =area.clone();
        int cols = tmp.length;
        int rows = tmp[cols-1].length;
        int sum = 0;
        for (int i = -1; i <2; i++) {
            for (int j = -1; j <2; j++) {
                int col = (x+i+cols)%cols;
                int row = (y+j+rows)%rows;
                sum+= tmp[col][row];
            }
        }
        sum-=tmp[x][y];
        return sum;
        }

}
