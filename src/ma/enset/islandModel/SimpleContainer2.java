package ma.enset.islandModel;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class SimpleContainer2{
    public static void main(String[] args) throws ControllerException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController masterAgent=agentContainer.createNewAgent("masterAgent", MasterAgent.class.getName(), new Object[]{});
        masterAgent.start();
        for (int i=0;i <2;i++){
            AgentController islandAgent=agentContainer.createNewAgent(String.valueOf(i), IslandAgent.class.getName(), new Object[]{});
            islandAgent.start();
        }



    }
}
