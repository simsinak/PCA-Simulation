import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;
import java.util.Random;


/**
 * Created by sinaaskarnejad on 4/4/16.
 */
public class ExternalInternatTopology implements ExternalTopology{
    Long[] bw=new Long[]{5000000000l , 10000000000l , 20000000000l};
    public Topology createTopology(int connectionNumber, boolean equal , long bandwidth) {
        long startTime = System.currentTimeMillis();
        Random r=null;
        SimpleWeightedGraph<Node2, DefaultWeightedEdge> topolgy=new SimpleWeightedGraph<Node2, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        topolgy.addVertex(OrganizationSystem.getInstance());
        Topology topo=new Topology();
        for (int i=0;i<connectionNumber;i++){
            ISP isp=new ISP(i+1);
            //set isp upload and download cost
            topolgy.addVertex(isp);
            DefaultWeightedEdge dwe=new DefaultWeightedEdge();
            topolgy.addEdge(OrganizationSystem.getInstance() , isp , dwe);
            if(equal) {
                topolgy.setEdgeWeight(dwe, bandwidth);
                isp.remainingTraffic=bandwidth;
            }
            else {
                if(r == null){
                    r=new Random();
                }
                long bwtemp=bw[r.nextInt(bw.length)];
                topolgy.setEdgeWeight(dwe, bwtemp);
                isp.remainingTraffic=bwtemp;
            }
            topo.nodes.add(isp);
        }
        try {
            test.bw.write("EXTERNAL TOPOLOGY CREATED AT "+ (System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        topo.topology=topolgy;
        topo.sortTopo();
        return topo;
    }
}
