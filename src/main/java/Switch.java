import java.util.Properties;

/**
 * Created by sinaaskarnejad on 3/29/16.
 */
public class Switch extends Node2{
    char type;
    int id;
    Properties routingTable;
    public Switch(char type , int id){
        this.type = type;
        this.id= id;
        routingTable=new Properties();
    }

    @Override
    public String toString() {
        return "Switch: "+id;
    }
}
