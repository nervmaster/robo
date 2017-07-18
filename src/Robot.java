public class Robot implements Navigator {
    //Construtor
    public Robot() {

    }
    //Metodos da interface
    public void goTo(int[] dir) {
        //Done
    }

    public boolean[] getObstacle() {
        return new boolean[]{true,false,false};
    }

    public int[] getPosition() {
        return new int[]{4,4,0,4};
    } 
}