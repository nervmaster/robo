import java.util.Arrays;

public class FooRobot implements Navigator {
    private int[][] obs;


    public FooRobot() {
        obs = new int[][]{{3,4},{4,2},{3,3},{3,5},{1,4},{0,5}};
    }
    //Metodos da interface
    public void goTo(int[] dir) {
        //Done
    }

    // public boolean[] getObstacle() {
    //     return new boolean[]{false,true,false};
    // }

    public boolean[] getObstacle(int[] pos) {
        int x,y;
        boolean[] result = new boolean[]{false,false,false};
        for(int[] obs : this.obs) {
            //abaixo
            x = pos[0];
            y = pos[1]-1;
            if(Arrays.equals(new int[]{x,y}, obs)) {
                result[0] = true;
            }
            
            //esquerda
            x = pos[0]-1;
            y = pos[1];
            if(Arrays.equals(new int[]{x,y}, obs)) {
                result[1] = true;
            }

            //acima
            x = pos[0];
            y = pos[1]+1;
            if(Arrays.equals(new int[]{x,y}, obs)) {
                result[2] = true;
            }
        }
        return result;
    }
        
    public int[] getPosition() {
        return new int[]{4,4,0,4};
    } 
}