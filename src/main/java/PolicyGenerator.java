import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by sinaaskarnejad on 6/30/16.
 */
public class PolicyGenerator {
    //write policies in this array

    Scanner sc=new Scanner(System.in);
    String[] policies=new String[]{
            "allServices permit   providers A" ,
            "allGroups permit bandwidth 5000000",
            "allServices permit   providers A B"};
    public void generate(){
        long startTime = System.currentTimeMillis();
        OrganizationPolicyParser p=new OrganizationPolicyParser();
        p.init();
        for (int i = 0; i < policies.length; i++) {
           boolean result=p.parse(policies[i]);
           while (result!=true){
               System.out.println("correct the policy--> { "+policies[i]+" }");
               policies[i]= sc.nextLine();
               System.out.println("checking again");
               result=p.parse(policies[i]);
           }
        }
        try {
            test.bw.write("POLICIES CREATED IN "+ (System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   System.out.println((Set<String>)MapRepository.allServicesPermit.get("all").get("providers"));


    }
}
