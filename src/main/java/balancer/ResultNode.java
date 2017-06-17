package balancer;

import java.util.HashMap;

/**
 * Created by sinaaskarnejad on 4/23/16.
 */
public class ResultNode {
   // Vector<ResultNode> connections=new Vector<ResultNode>();
    int id;
    int type;
    boolean choosed=false;
    boolean temp=false;
    boolean star=false;
    boolean addtotemp=false;
    boolean tempchoosed=false;
    HashMap<ResultNode , Integer> connections=new HashMap<ResultNode, Integer>();
    HashMap<ResultNode , Integer> tempconnections=new HashMap<ResultNode, Integer>();
    public ResultNode(int id, int type) {
        this.id = id;
        this.type = type;
    }
    public ResultNode createACopy(ResultNode r){
        id=r.id;
        type=r.type;
        choosed=r.choosed;
        temp=r.temp;
        star=r.star;
        connections= (HashMap<ResultNode, Integer>) r.connections.clone();
        return this;

    }
    public void changeChoosed(boolean t){
        if(!addtotemp) {
            choosed = t;
        }else{
            choosed = t;
            tempchoosed = true;
        }
    }
    public void addConnection(ResultNode c){
        connections.put(c , 1);
    }
    public void addTempConnection(ResultNode c){
        tempconnections.put(c , 1);
    }

    @Override
    public String toString() {
        return "id: "+id+" type "+type;
    }
}
