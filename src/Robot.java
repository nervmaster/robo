import java.util.Arrays;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;	
import lejos.nxt.LCD;
	

public class Robot implements Navigator {
    int direcao;
    private UltrasonicSensor sonic;

    private boolean[] obstacles;
    
    public Robot() {
    	this.direcao = 1;
    	this.sonic = new UltrasonicSensor(SensorPort.S4);
    	this.obstacles = new boolean[3];
    }
    
    //Metodos da interface
    public void goTo(int[] comandos) {
        for(int i=0; i < comandos.length; i++){
        	if(comandos[i] == -1)
        		break;
        	// Se esta na dir correta
        	if(comandos[i] == direcao){
				this.forward();
        	}else if(comandos[i] == direcao+1%4){
				// Vizinho a direita
        		this.turnRight();
        		this.forward();
        	}else if(comandos[i] == direcao-1 || (comandos[i] == 3 && direcao == 0)){
				// vizinho a esquerda 
				this.turnLeft();
    			this.forward();
    		}else{
				//180 graus
    			this.turnLeft();
    			this.turnLeft();
    			this.forward();
			}
			// direcao do robo é igual ao ultimo comando
        	direcao = comandos[i];
		}
		// Corrigir a orientação após o ultimo comando
        if(direcao != 1) {
        	if(direcao == 2) {
        		this.turnLeft();
        	} else if(direcao == 0) {
        		this.turnRight();
        	} else {
        		this.turnRight();
        		this.turnRight();
        	}
        }
        direcao = 1;
	}
	
	private int getMinSonicRead() {
		int low = Integer.MAX_VALUE;
		for(int i = 0; i < 1000; i++) {
			int ted = sonic.getDistance();
			if(ted < low)
				low = ted;
		}
		return low;
	}

    public boolean[] getObstacle() {
    	obstacles[0] = true;
    	obstacles[1] = true;
    	obstacles[2] = true;

		int distLeft, distFront, distRight;
		
		int MINDIST = 20;
     
     	distFront = this.getMinSonicRead();
     	Motor.C.rotate(-90,false);
     	distLeft = this.getMinSonicRead();
     	Motor.C.rotate(180,false);
     	distRight = this.getMinSonicRead();
     	Motor.C.rotate(-90,false);
     	
     	if( distLeft >= MINDIST )
     		obstacles[0] = false;
     	if( distFront >= MINDIST )
     		obstacles[1] = false;
     	if( distRight >= MINDIST )
     		obstacles[2] = false;
     	
//     	TESTE
//     	LCD.drawInt(distLeft, 0, 0);
//     	LCD.drawString(Boolean.toString(left), 4, 0);
//     	LCD.drawInt(distFront, 0, 2);
//     	LCD.drawString(Boolean.toString(front), 4, 2);
//     	LCD.drawInt(distRight, 0, 4);
//     	LCD.drawString(Boolean.toString(right), 4, 4);
     	
    	return obstacles;
    }
    
    public void turnRight() {
		//virar a direita
    	//minimamente satisfatorio nota 5.7
		//487 = Nota 8.7
		int VALOR = 475;

		Motor.A.rotate(-VALOR,true);
		Motor.B.rotate(VALOR,false);
	}
	
	public void turnLeft() {
		//Virar esquerda
		//Nota 5.5
		//475 = nota 6.7
		int VALOR = 475;
		Motor.A.rotate(VALOR,true);
		Motor.B.rotate(-VALOR,false);
	}
	
	public void forward() {
		//Noooota 9.9
		int VALOR = 700;

		Motor.A.rotate(VALOR, true);
		Motor.B.rotate(VALOR, false);
	}
   
    public int[] getPosition() {
    	int pos = 0;
    	String str;
    	while(Button.waitForAnyPress(200) == 0) {
    		LCD.clear();
    		pos = sonic.getDistance()/20;
    		str = String.valueOf(pos) + ",6  0,6";
    		LCD.drawString(str, 1, 1);
    	}
    	pos = (pos >= 12) ? 11 : pos;
    	return new int[]{pos,6,0,6};
    } 
}