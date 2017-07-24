import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class Star{
    //Coordenadas em X,Y
    int[] saida, chegada;
    int[] pos;

    //Acesso de matriz é em mat[lin][col] ou mat[y][x]
    int[][] desl;
    int[][] est;
    

    Robot robot;
    

    //Metodos privadas

    //Estima o custo de target ate a chegada
    private int estimateCost(int x, int y){
        int custo_x = Math.abs(x - this.chegada[0]);
        int custo_y = Math.abs(y - this.chegada[1]);
        return custo_x + custo_y;
    }

    private boolean isNeighbor(int x, int y){
        return (Math.abs(x - pos[0]) <= 1 || Math.abs(y = pos[1]) <= 1);
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
        int lowest = -1;
        int[] result = {-1,-1};
        for(int x = 0; x < desl.length; x++) {
            for(int y = 0; y < desl[0].length; y++) {
                int value = desl[y][x] + est[y][x];
                if(value > 0 && (lowest < 0 || value < lowest)) {
                    System.out.printf("old: %d new %d pos %d,%d\n", lowest, value, x, y);
                    lowest = value;
                    result = new int[]{x,y};
                }

                //Prioridade para uma celula vizinha conseguir o valor
                // else if(value > 0 && (value <= lowest && isNeighbor(x,y))) {
                //     System.out.printf("old: %d new %d pos %d,%d\n", lowest, value, x, y);
                //     lowest = value;
                //     result = new int[]{x,y};
                // }
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
    
    public Star(Robot robot, int[] saida, int[] chegada){
        this.robot = robot;
        this.chegada = chegada;
        this.saida = saida;
        this.pos = saida;
        this.desl = this.createMat(12, 12);
        this.est = this.createMat(12, 12);
    }

    public void print() {
        //Inverte os valores y para que o 0,0 fique no canto inferior esquerdo ao invés do canto superior
        for(int y = desl.length-1; y >= 0; y--) {
            for(int x = 0; x < desl[0].length; x++) {
                int[] cell = {x,y};
                if(Arrays.equals(this.pos, cell)) {
                    System.out.printf("R ");
                }
                else if(Arrays.equals(this.chegada, cell)) {
                    System.out.printf("C ");
                }
                else if(desl[y][x] == -1){
                    System.out.printf("X ");
                }
                else if(desl[y][x] == -10){
                    System.out.printf("- ");
                }
                else {
                    System.out.printf("%d ", desl[y][x] + est[y][x]);
                }
            }
            System.out.printf("\n");
        }
        System.out.print("\n");
    }
    public void start() {
        Scanner keyboard = new Scanner(System.in);

        while(!Arrays.equals(this.pos, this.chegada)){

            //Calcula pesos
            int x = this.pos[0];
            int y = this.pos[1];

            //Verifica se tem obstaculos vizinhos
            //boolean[] obs = robot.getObstacle();
            boolean[] obs = robot.fooGetObstacle(this.pos);

            updateCost(obs, this.pos);
            
            this.print();
            keyboard.next();

            //Vai ate a celula de menor valor
            int[] target_pos = lowestCellValue();

            //Calcula caminho JÁ visitado para a celula de menor valor
            //Manda para o robo o caminho
            //SIMULAÇÃO
            this.pos = target_pos;
            System.out.printf("%d,%d\n",this.pos[0], this.pos[1]);
        }
    }

    //MAIN
    public static void main(String[] args) {
        //Instancia do Robo
        Robot r = new Robot();
        //Pega as coordenadas do mundo
        int[] coords = r.getPosition();
        int[] saida = {coords[0], coords[1]};
        int[] chegada = {coords[2], coords[3]};

        //Cria instância A*
        Star star = new Star(r, saida, chegada);
        star.start();
        star.print();

    }
}