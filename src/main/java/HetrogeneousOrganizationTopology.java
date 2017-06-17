import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * Created by sinaaskarnejad on 3/29/16.
 */
public class HetrogeneousOrganizationTopology implements OrganizationTopology{
    String[] os={"Linux", "RedHat", "SUSE", "Windows", "WindowsSQLS", "WindowsSQLW", "WindowsSQLE"};
    float[] ram={0.5f, 1f, 2f, 4f, 8f, 16f};
    int[] cores={1 , 2 ,3 ,4};
    double[] hertz={1, 1.1, 1.2, 1.3, 1.7, 2, 2.1, 2.2, 2.3, 2.7, 3.0, 3.1, 3.2, 3.3, 3.4, 3.7};
    int[] hard={128, 256, 512, 800, 1024};
    public Topology createTopology(int hostnumber , int k , double miu) {
        Long startTime=System.currentTimeMillis();
        SimpleWeightedGraph<Node2, DefaultWeightedEdge> topolgy=new SimpleWeightedGraph<Node2, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        Topology topo=new Topology();
        int number=(int)Math.pow(k/2,2);
        int id=1;
        Vector<Node2> cores=new Vector<Node2>(number);
        Vector<Node2> aggregations=new Vector<Node2>(number*2);
        Vector<Node2> edges=new Vector<Node2>(number*2);
        for (int i=0;i<number;i++){
            cores.add(new Switch('c' , id++));
            topolgy.addVertex(cores.lastElement());
        }
        for (int i=0;i<number*2;i++){
            aggregations.add(new Switch('a' , id++));
            topolgy.addVertex(aggregations.lastElement());
        }
        for (int i=0;i<number*2;i++){
            edges.add(new Switch('e' , id++));
            topolgy.addVertex(edges.lastElement());
        }
        int start=-1;
        int end = 0;
        int module = number/(k/2);
        for(int j=0;j<number;j++) {
            if (j%module ==0){
                start ++;
            }
            for (int i = start; i < k * (k/2); i = i + (k/2)) {
                DefaultWeightedEdge dwe=new DefaultWeightedEdge();
                topolgy.addEdge(cores.elementAt(j), aggregations.elementAt(i), dwe);
                topolgy.setEdgeWeight(dwe , 20000000000l);
            }
        }


        start=0;
        end=0;
        for(int j=0;j<number*2;j++){
            if(j%(k/2)==0){
                start = j;
                end = start+k/2;
            }
            for (int i = start; i <end; i++){
                DefaultWeightedEdge dwe=new DefaultWeightedEdge();

                topolgy.addEdge(aggregations.elementAt(j), edges.elementAt(i), dwe);
                topolgy.setEdgeWeight(dwe , 5000000000l);

            }
        }
        module = number*k;
        Switch s=null;
        char group='A'-1;
        int edgeSwitch=0;
        int increase=0;
        int counter=0;
        int numberOfHostsOnEachPort = (int)Math.ceil(((double)hostnumber)/module);
        int overload=numberOfHostsOnEachPort*module-hostnumber;
        for (int i=0;i<hostnumber;i++){
            if(i==(module-overload)*numberOfHostsOnEachPort ){

                numberOfHostsOnEachPort--;
                counter--;
            }
            if(counter==numberOfHostsOnEachPort || i==0){
                counter=0;
                if(increase==k/2){
      //              System.out.println("yes");
                    increase = 0;
                    edgeSwitch++;
                }else{
                    increase++;
                }
     //           System.out.println("Switch id->"+id);
                s = new Switch('h' , id++);
                topolgy.addVertex(s);
                group = (char)(++group);
                DefaultWeightedEdge dwe=new DefaultWeightedEdge();
                topolgy.addEdge(s,edges.get(edgeSwitch),dwe);
                topolgy.setEdgeWeight(dwe,5000000000l);
            }
            DefaultWeightedEdge dwe=new DefaultWeightedEdge();
            Host h=createHost(i , miu, ""+group);
            topo.nodes.add(h);
     //       System.out.println("id->"+i);
            topolgy.addVertex(h);
            topolgy.addEdge(h,s,dwe);
            topolgy.setEdgeWeight(dwe , 1000000000l);
            counter++;

        }
        try {
            test.bw.write("INTERNAL TOPOLOGY CREATED AT "+ (System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        topo.topology=topolgy;
        return topo;
    }

    private Host createHost(int id , double miu , String group) {
        Random r=new Random();

        Host h=new Host(id ,group,os[r.nextInt(os.length)],ram[r.nextInt(ram.length)],hard[r.nextInt(hard.length)],miu,cores[r.nextInt(cores.length)] , hertz[r.nextInt(hertz.length)]);
        return h;
    }
}
