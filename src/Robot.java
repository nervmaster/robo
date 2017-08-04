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
    private int[][] obs;
    int direcao = 1;

    public Robot() {
        obs = new int[][]{{3,4},{4,2},{3,3},{3,5},{1,4},{0,5}};
    }
    //Metodos da interface
    public void goTo(int[] dir) {
        //Done
    	int init = 0;
    	if(direcao !=1){
    		if(direcao == 3){
    			for(int j:dir){
    				if(j == 4)
    					j=2;
    				else
    					j +=2;
    			}
    		}
    		if(direcao == 2 || direcao == 4){
    			for(int j:dir){
    				if(j == 4)
    					j=1;
    				else
    					j++;
    			}
    		}
    	}	
    	
    	
    	for(int i:dir){
    		//direita
    		if(i == 2 ){
    			this.turnRight();
    			this.forward();
    			direcao++;
    			if (direcao == 5)
    				direcao = 1;
    			
    			for(int j:dir){
    				if(j == 4)
    					j=1;
    				else
    					j++;
    			}    				
    		}else if(i == 4){
    			//Esquerda
    			this.turnLeft();
    			this.forward();
    			direcao--;
    			if (direcao == 0)
    				direcao = 4;
    			
    			for(int j:dir){
    				if(j==4) 
    					j=1;
    				else
    					j++;
    			}
    		}else if(i == 1){
    			this.forward();
    		}

    		
    		
    	}
    }


    public boolean[] getObstacle() {
    	boolean left = true, front = true, right = true;
         
        int distLeft, distFront, distRight;
     	UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S2);
     	distFront = sonic.getDistance();
     	Motor.C.rotate(-90,false);
     	distLeft = sonic.getDistance();
     	Motor.C.rotate(180,false);
     	distRight = sonic.getDistance();
     	Motor.C.rotate(-90,false);
     	
     	if( distLeft >= 20 )
     		left = false;
     	if( distFront >= 20 )
     		front = false;
     	if( distRight >= 20 )
     		right = false;
     	
     	
     	LCD.drawInt(distLeft, 0, 0);
     	LCD.drawString(Boolean.toString(left), 4, 0);
     	LCD.drawInt(distFront, 0, 2);
     	LCD.drawString(Boolean.toString(front), 4, 2);
     	LCD.drawInt(distRight, 0, 4);
     	LCD.drawString(Boolean.toString(right), 4, 4);
     	
     	
    	return new boolean[]{left,front,right};
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


    public boolean[] fooGetObstacle(int[] pos) {
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