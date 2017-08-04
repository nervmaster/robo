import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.lang.Math;

public class Star{
    //Coordenadas em X,Y
    int[] saida, chegada;
    int[] pos;

    //Acesso de matriz é em mat[lin][col] ou mat[y][x]
    int[][] desl;
    int[][] est;
    int[][] mini;
    int[][] nn;
    int[] path;

    FooRobot robot;
    
    //Metodos privadas
    private void resetMini(){
        for(int x = 0; x < mini.length; x++) {
            for(int y = 0; y < mini[0].length; y++) {
                mini[y][x] = 0;
            }
        }
    }

    private int[] getPathTo(int[] target) {
        int[] walk = this.pos;
        int lowest = Integer.MAX_VALUE;
        
        int x,y, index=0;
        
        resetMini();
        for(int i = 0; i < path.length; i++) {
            path[i] = -1;
        }

        while(!Arrays.equals(target, walk)) {
            x = walk[0];
            y = walk[1];
            
            //Neighbors
            if(y > 0) {
                nn[0][0] = x;   nn[0][1] = y-1;
            } else { 
                nn[0][0] = -1;  nn[0][1] = -1; 
            }
            
            if(y > 0) {
                nn[1][0] = x-1; nn[1][1] = y;
            } else { 
                nn[1][0] = -1;  nn[1][1] = -1; 
            }
            
            if(y < this.desl[0].length-1) {
                nn[2][0] = x;   nn[2][1] = y+1;
            } else { 
                nn[2][0] = -1;  nn[2][1] = -1; 
            }
            
            if(x < this.desl.length-1) {
                nn[3][0] = x+1; nn[3][1] = y;
            } else { 
                nn[3][0] = -1;  nn[3][1] = -1; 
            }

            //Para cada vizinho calcular o peso estimado
            for(int[] n : nn) {
                x = n[0]; y = n[1];
                
                if(x == -1) {
                    continue;
                }

                if(est[y][x] == -10) {
                    mini[y][x] = estimateCostTo(n, target);
                }
                else {
                    mini[y][x] = -1;
                }

            }
            
            //Ir para o vizinho de menor valor
            int low = Integer.MAX_VALUE;
            int[] low_pos = {0,0};
            int last_i = 0;
            for(int i =0; i < nn.length; i++) {
                x = nn[i][0]; y = nn[i][1];

                if(x == -1) {
                    continue;
                }
                if(Arrays.equals(nn[i],target)) {
                    //JACKPOT
                    low_pos = nn[i];
                    last_i = i;
                    break;
                }

                if(mini[y][x] >= 0 && mini[y][x] < low) {
                    low = mini[y][x];
                    low_pos = nn[i];
                    last_i = i;

                    //erase value for this cell
                    mini[y][x] = -10;
                }
            }

            path[index++] = last_i;
            walk = low_pos;
        }

        return target;
    }

    private int estimateCostTo(int[] cell, int[] target) {
        int custo_x = Math.abs(cell[0] - target[0]);
        int custo_y = Math.abs(cell[1] - target[1]);
        return custo_x + custo_y;
    }

    public void pause() 
    {
    	try
    	{
    		System.in.read();
    	}
    	catch(Exception e)
    	{
    		System.out.printf("Error?");
    	}
    }

    //Estima o custo de target ate a chegada
    private int estimateCost(int x, int y){
        int[] arg = {x,y};
        return estimateCostTo(arg, this.chegada);
    }

    private boolean isNeighbor(int x, int y){
    	int xdiff = Math.abs(x - pos[0]);
    	int ydiff = Math.abs(y - pos[1]);

    	return (xdiff == 1 && ydiff == 0) || (xdiff == 0 && ydiff == 1);
    }

    private void updateCost(boolean[] obs, int[] pos) {
            int x = pos[0];
            int y = pos[1];

            //custo deslocamento ate a celula atual
            int custo_atual = desl[y][x];

            //Custo de deslocamento para celulas vizinhas se não tem obstaculo
            if(y > 0 && desl[y-1][x] >= 0) {
                desl[y-1][x] = obs[0] ? -1 : custo_atual + 1; //abaixo
                est[y-1][x] = obs[0] ? -1 : estimateCost(x, y-1);
            }
            if(x > 0 && desl[y][x-1] >= 0) {
                desl[y][x-1] = obs[1] ? -1 : custo_atual + 1; //esquerda
                est[y][x-1] = obs[1] ? -1 : estimateCost(x-1, y);
            }
            if(y < desl[0].length && desl[y+1][x] >= 0) {
                desl[y+1][x] = obs[2] ? -1 : custo_atual + 1; //acima
                est[y+1][x] = obs[2] ? -1 : estimateCost(x, y+1);
            }

            //Nulifica os valores da celula atual
            est[y][x] = -10;
            desl[y][x] = -10;

    }

    private int[] lowestCellValue() {
        int lowest = Integer.MAX_VALUE;
        int[] result = {-1,-1};
        for(int x = 0; x < desl.length; x++) {
            for(int y = 0; y < desl[0].length; y++) {
                int value = desl[y][x] + est[y][x];
                if(value > 0 && value < lowest) {
                    lowest = value;
                    result[0] = x; result[1] = y;
                }

                // Prioridade para uma celula vizinha conseguir o valor
                else if(value > 0 && (value <= lowest && isNeighbor(x,y))) {
                    // System.out.printf("To Neighbor pos %d,%d\n", x, y);
                    lowest = value;
                    result[0] = x; result[1] = y;
                }
            }
        }
        return result;
    }

    //Retorna uma matriz NxM
    private int[][] createMat(int xsize, int ysize) {
        int[][] mat = new int[ysize][];
        for (int i = 0; i < ysize; i++) {
            mat[i] = new int[xsize];
        }

        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                mat[j][i] = 0;
            }
        }
        return mat;
    }
    
    public Star(FooRobot robot, int[] saida, int[] chegada){
        this.robot = robot;
        this.chegada = chegada;
        this.saida = saida;
        this.pos = saida;
        this.desl = this.createMat(12, 12);
        this.est = this.createMat(12, 12);
        this.mini = this.createMat(12, 12);
        this.nn = new int[4][2];
        this.path = new int[20];
    }

    public void print() {
        //Inverte os valores y para que o 0,0 fique no canto inferior esquerdo ao invés do canto superior
        for(int y = desl.length-1; y >= 0; y--) {
            for(int x = 0; x < desl[0].length; x++) {
                int[] cell = {x,y};
                if(Arrays.equals(this.pos, cell)) {
                    System.out.printf("RR ");
                }
                else if(Arrays.equals(this.chegada, cell)) {
                    System.out.printf("CC ");
                }
                else if(desl[y][x] == -1){
                    System.out.printf("XX ");
                }
                else if(est[y][x] <= -10){
                    System.out.printf("-- ", est[y][x]);
                }
                else {
                    System.out.printf("%02d ", desl[y][x] + est[y][x]);
                }
            }
            System.out.printf("\n");
        }
        System.out.print("\n");
    }
    public void start() {
        
        while(!Arrays.equals(this.pos, this.chegada)){

            //Calcula pesos
        
            //Verifica se tem obstaculos vizinhos
            //boolean[] obs = robot.getObstacle();
            boolean[] obs = robot.fooGetObstacle(this.pos);

            updateCost(obs, this.pos);
            
            // this.print();
            // pause();

            //Vai ate a celula de menor valor
            int[] target_pos = lowestCellValue();

            //Calcula caminho JÁ visitado para a celula de menor valor
            this.getPathTo(target_pos);

            //Manda para o robo o caminho
            //SIMULAÇÃO
            this.pos = target_pos;
        }
    }

    //MAIN
    public static void main(String[] args) {

        FooRobot r = new FooRobot();
        //Pega as coordenadas do mundo
        int[] coords = r.getPosition();
        int[] saida = {coords[0], coords[1]};
        int[] chegada = {coords[2], coords[3]};

        //Cria instância A*
        Star star = new Star(r, saida, chegada);
        star.start();
        // star.print();

    }
}
