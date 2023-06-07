package ma.enset.islandModel;



import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


import java.util.Random;

public class IslandAgent extends Agent {
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private final double EPS = 0.4;
    private final int MAX_EPOCH = 200;
    private final int GRID_SIZE = 6;
    private final int ACTION_SIZE = 4;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTION_SIZE];
    private int[][] actions;
    private int stateI;
    private int stateJ;
    @Override
    protected void setup() {

        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                actions = new int[][]{
                        {0, -1},//left
                        {0, 1},//right
                        {1, 0},//bottom
                        {-1, 0}//top
                };
                grid = new int[][]{
                        {0, 0,0,0,0,0},
                        {0, 0,-1,0,0,-1},
                        {0, -1,0,0,0,0},
                        {0, 0,-1,0,0,0},
                        {-1, 0,0,0,0,0},
                        {0, 0,0,0,0,1}
                };

            }
        });
        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int it = 0;
            int currentState;
            int nextState;
            int act, act1;

            @Override
            public void action() {
                resetState();
                while (!finished()) {
                    currentState = stateI * GRID_SIZE + stateJ;
                    act = chooseAction(0.4);
                    nextState = executeAction(act);
                    act1 = chooseAction(0);

                    qTable[currentState][act] = qTable[currentState][act] + ALPHA * (grid[stateI][stateJ] + GAMMA * qTable[nextState][act1] - qTable[currentState][act]);
                }
                it++;
            }
            @Override
            public boolean done() {
                return it > MAX_EPOCH || it == MAX_EPOCH;
            }
        });
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfAgentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("QLearningAlgorithm");
                dfAgentDescription.addServices(serviceDescription);
                DFAgentDescription[] dfAgentDescriptions;
                try {
                    dfAgentDescriptions = DFService.search(getAgent(), dfAgentDescription);
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }
                ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                aclMessage.addReceiver(dfAgentDescriptions[0].getName());
                aclMessage.setContent(showResult());
                send(aclMessage);
            }
        });
        addBehaviour(sequentialBehaviour);
    }

    private void resetState() {
        stateI = 0;
        stateJ = 0;
    }

    private int chooseAction(double eps) {
        Random rnd = new Random();
        double bestQ = 0;
        int action = 0;
        if (rnd.nextDouble() < eps) {
            //Exploration
            action = rnd.nextInt(ACTION_SIZE);

        } else {
            //Exploitation
            int st = stateI * GRID_SIZE + stateJ;
            for (int i = 0; i < ACTION_SIZE; i++) {
                if (qTable[st][i] > bestQ) {
                    bestQ = qTable[st][i];
                    action = i;
                }
            }


        }
        return action;
    }

    private int executeAction(int act) {
        stateI = Math.max(0, Math.min(actions[act][0] + stateI, GRID_SIZE-1));
        stateJ = Math.max(0, Math.min(actions[act][1] + stateJ, GRID_SIZE-1));
        return stateI * GRID_SIZE + stateJ;

    }

    private boolean finished() {
        return grid[stateI][stateJ] == 1;
    }

    private String showResult() {
        String result = "";
        resetState();
        while (!finished()) {
            int act = chooseAction(0);
            result = result + stateI + " " + stateJ + " Action: " + act + "\n";
            executeAction(act);
        }
        result = result + "Final state :" + stateI + " " + stateJ;
        return result;

    }
}