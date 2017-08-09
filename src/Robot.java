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
//import lejos.nxt.LCD;
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
//        	Se esta na dir correta
        	if(comandos[i] == direcao){
        		this.forward();
        	}else if(comandos[i] == direcao+1%4){
        			this.turnRight();
        			this.forward();
        	}else if(comandos[i] == direcao-1 || (comandos[i] == 3 && direcao == 0)){
    				this.turnLeft();
    				this.forward();
    		
        	}else{
    				this.turnLeft();
    				this.turnLeft();
    				this.forward();
			}
        	
        	direcao = comandos[i];
        }
//        Corrigir orientacao
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
    	
    	
    	
    	//Done
//    	if(direcao !=1){
//    		if(direcao == 3){
//    			for(int j:dir){
//    				if(j == 4)
//    					j=2;
//    				else
//    					j +=2;
//    			}
//    		}
//    		if(direcao == 2 || direcao == 4){
//    			for(int j:dir){
//    				if(j == 4)
//    					j=1;
//    				else
//    					j++;
//    			}
//    		}
//    	}	
//    	
//    	
//    	for(int i:dir){
//    		//direita
//    		if (i == -1){
//    				break;
//    		}else{
//	    		if(i == 2 ){
//	    			this.turnRight();
//	    			this.forward();
//	    			direcao++;
//	    			if (direcao == 5)
//	    				direcao = 1;
//	    			
//	    			for(int j:dir){
//	    				if(j == 4)
//	    					j=1;
//	    				else
//	    					j++;
//	    			}    				
//	    		}else if(i == 4){
//	    			//Esquerda
//	    			this.turnLeft();
//	    			this.forward();
//	    			direcao--;
//	    			if (direcao == 0)
//	    				direcao = 4;
//	    			
//	    			for(int j:dir){
//	    				if(j==4) 
//	    					j=1;
//	    				else
//	    					j++;
//	    			}
//	    		}else if(i == 1){
//	    			this.forward();
//	    		}
//	
//	    		
//	    		
//	    	}
//    	}


    public boolean[] getObstacle() {
    	obstacles[0] = true;
    	obstacles[1] = true;
    	obstacles[2] = true;

    	int distLeft, distFront, distRight;
     
     	distFront = sonic.getDistance();
     	Motor.C.rotate(-90,false);
     	distLeft = sonic.getDistance();
     	Motor.C.rotate(180,false);
     	distRight = sonic.getDistance();
     	Motor.C.rotate(-90,false);
     	
     	if( distLeft >= 20 )
     		obstacles[0] = false;
     	if( distFront >= 20 )
     		obstacles[1] = false;
     	if( distRight >= 20 )
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
		Motor.A.rotate(-475,true); //521
		Motor.B.rotate(475,false);//521

	}
	
	public void turnLeft() {
	//Virar esquerda
	//Nota 5.5
		//475 = nota 6.7
	Motor.A.rotate(475,true);
	Motor.B.rotate(-475,false);
	
	}
	
	public void forward() {
		
		int tacoInicial = 0;
		
	
		//Noooota 9.9
		Motor.A.rotate(700, true);
		Motor.B.rotate(700, false);
		
		
		
		
	}
   
    public int[] getPosition() {
//    	CALCULA POSIÇÃO   	
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