package balancer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by sinaaskarnejad on 4/12/16.
 */
public class Node implements Comparable {
    HashMap<Node , Integer[]> forwardNodes=new HashMap<Node, Integer[]>();
    HashMap<Node , Link[]> forwardLinks=new HashMap<Node, Link[]>();
    Vector<Node> fathers=new Vector<Node>();
    int value;
    int id;
    int place;
    int sum=0;
    int subtract=0;
    boolean sio=false;
    boolean addtotemp=false;
    Vector<Node> interactingNodes=new Vector<Node>();
    int[][] interactions2;
    int[][] tempinteraction;
    public void createConnection(){
        interactions2=new int[2][interactingNodes.size()];
        tempinteraction=new int[2][interactingNodes.size()];
        for (int i=0;i<interactingNodes.size();i++){
            interactions2[0][i]=interactingNodes.get(i).id;
            interactions2[1][i]=0;
            tempinteraction[0][i]=interactingNodes.get(i).id;
            tempinteraction[1][i]=0;
        }
    }
    public void addsum(int id){
        int row=Arrays.binarySearch(interactions2[0], id);
        if(!addtotemp){
        if(interactions2[1][row]!=1){
            interactions2[1][row]=1;
            sum++;
        }}
        else{
            if(interactions2[1][row]!=1){
                interactions2[1][row]=1;
                tempinteraction[1][row]=1;
                sum++;
                subtract++;
            }
        }
    }
//    public Node(int value){
//        this.value = value;
//    }
    public boolean checkslecting(){
        boolean temp=sio;
        for (int i = 0; i < interactingNodes.size(); i++) {
            temp = temp && interactingNodes.get(i).sio;
        }
        return temp;
    }
    public boolean checking2(){
        if(sum==interactingNodes.size())
            return true;
        else return false;
    }
    public Node(){

    }

    @Override
    public String toString() {
        return id+"";
    }

    public Node(int id){
        this.id = id;
    }
    public int compareTo(Object o) {
        if(o instanceof Node){
            Node t=(Node)o;
            if(t.value>this.value){
                return 1;
            }else if(t.value<this.value){
                return -1;
            }else{
                return 0;
            }
        }
        else return 0;
    }
}
