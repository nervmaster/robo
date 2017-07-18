import java.util.Arrays;
import java.util.List;
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

    private int getMatValue(int[][] mat, int x, int y) {
        return mat[y][x];
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
        for(int y = desl.length-1; y > 0; y--) {
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
                else {
                    System.out.printf("%d ", desl[y][x] + est[y][x]);
                }
            }
            System.out.printf("\n");
        }
        System.out.print("\n");
    }
    public void start() {
        //Calcula pesos
        int x = this.pos[0];
        int y = this.pos[1];

        //Verifica se tem obstaculos vizinhos
        boolean[] obs = robot.getObstacle();
        
        //custo deslocamento ate a celula atual
        int custo_atual = getMatValue(desl, x, y);

        //Custo de deslocamento para celulas vizinhas se não tem obstaculo
        desl[y-1][x] = obs[0] ? -1 : custo_atual + 1; //abaixo
        desl[y][x-1] = obs[1] ? -1 : custo_atual + 1; //esquerda
        desl[y+1][x] = obs[2] ? -1 : custo_atual + 1; //acima

        //Estima o custo de deslocamento das celulas vizinhas ate a chegada
        est[y-1][x] = obs[0] ? -1 : estimateCost(x, y-1);
        est[y][x-1] = obs[1] ? -1 : estimateCost(x-1, y);
        est[y+1][x] = obs[2] ? -1 : estimateCost(x, y+1);

        //Vai ate a celula de menor valor
        //Procura celular de menor valor
        //Calcula caminho JÁ visitado para a celula de menor valor
        //Manda para o robo o caminho

        //Repeat

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
        star.print();
        star.start();
        star.print();

    }
}