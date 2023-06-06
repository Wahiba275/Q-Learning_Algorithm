package ma.enset.sequential;

import java.util.Random;

public class Qlearning {
    private final double ALPHA=0.1 ;
    private final double GAMMA=0.9 ;
    private final double EPS=0.4 ;
    private final int MAX_EPOCH=200;
    private final int GRID_SIZE=3;
    private final int ACTION_SIZE=4;
    private  int [][]grid;
    private double [][] qTable = new double[GRID_SIZE*GRID_SIZE][ACTION_SIZE];
    private int [][] actions;
    private int stateI , stateJ;

    public Qlearning() {
        actions= new int[][]{//Table of actions
                {0,-1},//left
                {0,1},//right
                {1,0},//bottom
                {-1,0}//top
        };
        grid=new int[][]{//Table of rewards
                {0,0,1},
                {0,-1,0},
                {0,0,0}
        };
    }
    private int chooseAction(double eps){
        Random rnd = new Random();
        double bestQ = 0;
        int action = 0;
       if(rnd.nextDouble()<EPS) {
           //Exploration strategy
            action = rnd.nextInt(ACTION_SIZE);
       }else {
           //Exploitation strategy
           int st = stateI*GRID_SIZE + stateJ;
           for (int i = 0; i < ACTION_SIZE; i++) {
               if(qTable[st][i] > bestQ){
                   bestQ = qTable[st][i];
                   action = i;
               }
           }
       }
        return action;
    }
    private int executeAction(int act){
        stateI = Math.max(0, Math.min(actions[act][0]+stateI, 2));
        stateJ = Math.max(0, Math.min(actions[act][1]+stateJ, 2));
        return stateI*GRID_SIZE + stateJ;
    }
    private  void resetState(){
         stateI=2;
         stateJ=0;
    }
     public  void run(){
         int it = 0;
         resetState();
         int currentState;
         int nextState;
         while (it<30){
             currentState = stateI*GRID_SIZE + stateJ;
             int act = chooseAction(currentState);
             nextState = executeAction(act);
             System.out.println(stateI + " " + stateJ + " " + grid[stateI][stateJ]);
             it++;
         }
     }
}
