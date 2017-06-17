package balancer;

/**
 * Created by sinaaskarnejad on 4/29/16.
 */
public class ResultLink {
    Node none;
    Node ntwo;
    int type;
    int value;
    public ResultLink(Node none, Node ntwo , int type) {
        this.none = none;
        this.ntwo = ntwo;
        this.type=type;
    }
    public String toString() {
        return none.id+"->"+ntwo.id;
    }

}
