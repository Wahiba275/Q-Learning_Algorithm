package ma.enset.sequential;

import java.util.Arrays;
import java.util.Random;

public class QLearning {
    private final double ALPHA=0.1;
    private final double GAMMA=0.9;
    private final double EPS =0.4;
    private final int MAX_EPOCH=200;
    private final int GRID_SIZE=3;
    private final int ACTION_SIZE=4;


    private int [][] grid = new int[GRID_SIZE][GRID_SIZE];
    private double [][] qtable = new double[GRID_SIZE*GRID_SIZE][ACTION_SIZE];
    private int [][]actions;

    private int stateI;
    private int stateJ;


    public QLearning(){
        actions = new int[][]{
                {0,-1},//left
                {0,1},//right
                {1,0},//Bottom
                {-1,0}//top
        };
        grid = new int[][]{
                {0,0,1},
                {0,-1,0},
                {0,0,0}
        };
    }
    private void resetState(){
        stateI=2;
        stateJ=0;
    }

    private int chooseAction(double eps){
        Random rnd = new Random();
        double bestQ = 0;
        int act = 0;
        if(rnd.nextDouble()<EPS){
            //Exploration strategy
            act = rnd.nextInt(ACTION_SIZE);
        }else {
            //Exploitation strategy
            int st = stateI*GRID_SIZE+stateJ;
            for(int i = 0; i<ACTION_SIZE; i++){
                if(qtable[st][i]>bestQ){
                    bestQ=qtable[st][i];
                    act=i;
                }
            }

        }
        return act;
    }
    private int executeAction(int act){
        stateI = Math.max(0, Math.min(actions[act][0]+stateI, 2));
        stateJ = Math.max(0, Math.min(actions[act][1]+stateJ, 2));
        return stateI*GRID_SIZE+stateJ;
    }
    private boolean finished(){
        return grid[stateI][stateJ] == 1;
    }
    private void showResult(){
        for(double []line:qtable){
            System.out.printf("[");
            for(double qvalue:line){
                System.out.printf(qvalue+",");
            }
            System.out.println("]");
        }
        System.out.println(" ");
        resetState();
        while(!finished()){
            int act = chooseAction(0);
            System.out.println("State: "+(stateI*GRID_SIZE+stateJ)+" /action:: "+act);
            executeAction(act);
        }
        System.out.println("Final State: "+(stateI*GRID_SIZE+stateJ));

    }

    public void run(){
        int it = 0;
        int currentState;
        int nextState;
        int act, act1;
        resetState();
        while(it<MAX_EPOCH){
            resetState();
            while(!finished()){

                currentState=stateI*GRID_SIZE+stateJ;
                act = chooseAction(0.4);
                nextState= executeAction(act);
                act1=chooseAction(0);
                qtable[currentState][act] = qtable[currentState][act]+ALPHA
                        *(grid[stateI][stateJ]
                        +GAMMA*qtable[nextState][act1]);
            }
            it++;
        }

        showResult();
    }
}