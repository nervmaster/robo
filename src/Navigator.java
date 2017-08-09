public interface Navigator {
    //Funcao para o robot deslocar uma celula
    //Params:
    //int[] dir = //Referentes a posição global do robô
    //      0 -> abaixo 
    //      1 -> esquerda
    //      2 -> acima
    //      3 -> direita
    // ex:  robot.goTo([1,1,1,1,2,3,3,3,1,1,2]);
    public void goTo(int[] dir);

    //Pergunta ao robo quais obstaculos estao em volta
    //Retorno:
    //Vetor bool[] com 3 posicoes que retorna 0 ou 1 para a presença de obstaculo
    //bool[0] -> celula abaixo
    //bool[1] -> celula esquerda
    //bool[2] -> celula acima
    //ex: robot.getObstacle() == [false,true,false]
    public boolean[] getObstacle();
    public boolean[] getObstacle(int[] pos);

    //Pergunta ao robo em que celula do mundo esta situado
    //Funcao chamada ao inicio do algoritmo onde sera determinado a posicao do robo e da chegada
    //[x_saida, y_saida, x_chegada, y_chegada]
    public int[] getPosition();
}