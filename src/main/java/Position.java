/**
 * Created by sinaaskarnejad on 7/5/16.
 */
public class Position {
    Cloud cloud;
    String location="";
    double cost;

    public Position(Cloud cloud, String location, double cost) {
        this.cloud = cloud;
        this.location = location;
        this.cost = cost;
    }
}
