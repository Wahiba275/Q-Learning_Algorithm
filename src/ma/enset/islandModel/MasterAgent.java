package ma.enset.islandModel;




import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class MasterAgent extends Agent {
    @Override
    protected void setup(){
        DFAgentDescription dfad=new DFAgentDescription();
        dfad.setName(getAID());
        ServiceDescription sd=new ServiceDescription();
        sd.setType("QLearningAlgorithm");
        sd.setName("masterAgent");
        dfad.addServices(sd);
        try {
            DFService.register(this,dfad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage=receive();
                if(aclMessage!=null){
                    System.out.println(aclMessage.getSender()+" "+aclMessage.getContent());

                }else {
                    block();
                }
            }
        });
    }

}
