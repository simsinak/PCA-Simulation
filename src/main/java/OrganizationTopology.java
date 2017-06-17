import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.FileNotFoundException;

/**
 * Created by sinaaskarnejad on 3/28/16.
 */
public interface OrganizationTopology {
    public Topology createTopology(int hostNumber , int k , double miu) throws FileNotFoundException;
}
