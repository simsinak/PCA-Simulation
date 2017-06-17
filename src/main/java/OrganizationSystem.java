/**
 * Created by sinaaskarnejad on 4/4/16.
 */
public class OrganizationSystem extends  Node2 {
    public int id = 1;
    private static OrganizationSystem instance;
    private OrganizationSystem(){

    }
    public static  OrganizationSystem getInstance(){
        if (instance == null){
            instance = new OrganizationSystem();
           instance.id = 1;
        }
        return instance;
    }
}
