import BPTree.implementation.CapsulateReault;
import BPTree.implementation.Node;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sinaaskarnejad on 5/23/16.
 */
public class RequestSender {
    public static ArrayList<Cloud>  sendResuest(CloudProviderRepository repo , BigInteger index , String name){
        long startTime=System.currentTimeMillis();
       ArrayList<Cloud> choosedclouds=new ArrayList<Cloud>();
//        Scanner sc=new Scanner(System.in);
//        System.out.println("waiting...");
//        sc.next();
        CapsulateReault<BigInteger> result=repo.providers.search(index);
        Node<BigInteger> tempNode=null;
        if(result!=null) {
            tempNode = result.node;
        }
        if (result==null || tempNode==null){
            try {
                test.bw.write("ERROR: No cloud can afford your request!!!:)\r\n");
            test.bw.write("ERROR: RETRIVE RELATED CLOUDS FOR ONE TYPE VM IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return choosedclouds;
        }
      //  System.out.println(result.position);
        ValueNode vl= (ValueNode) tempNode.getPointers().get(result.position);
    //    System.out.println("Orginal Type:");
        choosedclouds.addAll(vl.clouds);
//        for (int j = 0; j < vl.clouds.size(); j++) {
//            System.out.println("names:");
//            System.out.println(vl.clouds.get(j).name);
//
//        }
      //  System.out.println(vl.clouds.get(0).index.toString(2));
        if(index.compareTo(result.keyIndex)==0) {
            for (int i=0;i<vl.betterValueNodes.size();i++) {
             //   System.out.println("better Types:");
                ValueNode vlll=vl.betterValueNodes.get(i);
                choosedclouds.addAll(vlll.clouds);
//                for (int j = 0; j < vlll.clouds.size(); j++) {
//                    System.out.println("names:");
//                    System.out.println(vlll.clouds.get(j).name);
//                }

            }
            try {
                test.bw.write("RETRIVE RELATED CLOUDS FOR "+ name+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return choosedclouds;
        }
        tempNode = tempNode.getNext();
        while (tempNode!=null){
            for (int i = 0; i < tempNode.getKeys().size(); i++) {
                BigInteger temp=tempNode.getKeys().get(i);
                if (index.testBit(47)==temp.testBit(47) && index.testBit(46)==temp.testBit(46) &&index.and(temp).xor(index).compareTo(BigInteger.ZERO) == 0) {
                    vl= (ValueNode) tempNode.getPointers().get(i);
                    choosedclouds.addAll(vl.clouds);
//                    for (int j = 0; j < vl.clouds.size(); j++) {
//                        System.out.println("better names:");
//                        System.out.println(vl.clouds.get(j).name);
//                    }
                  //  System.out.println(vl.clouds.get(0).index.toString(2));
                }
            }
           tempNode = tempNode.getNext();
        }
        try {
            test.bw.write("RETRIVE RELATED CLOUDS FOR "+ name+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return choosedclouds;
    }
}
