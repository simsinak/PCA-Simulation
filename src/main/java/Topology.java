import BPTree.implementation.Node;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by sinaaskarnejad on 7/6/16.
 */
public class Topology {
    SimpleWeightedGraph<Node2, DefaultWeightedEdge> topology;
    ArrayList<Node2> nodes=new ArrayList<Node2>();
    public void sortTopo(){
        nodes.sort(new Comparator<Node2>() {
            public int compare(Node2 o1, Node2 o2) {
                if(o1.downloadCost<o2.downloadCost){
                    return -1;
                }else if(o1.downloadCost==o2.downloadCost){
                    return 0;
                }else {
                    return +1;
                }
            }
        });
    }
    public void sortTopoIN(){
        nodes.sort(new Comparator<Node2>() {
            public int compare(Node2 o1, Node2 o2) {
                if(o1.hard<o2.hard){
                    return -1;
                }else if(o1.hard==o2.hard){
                    return 0;
                }else {
                    return +1;
                }
            }
        });
    }
}
