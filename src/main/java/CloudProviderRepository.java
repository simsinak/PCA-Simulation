import BPTree.implementation.BPTree;
import BPTree.implementation.Node;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by sinaaskarnejad on 5/22/16.
 */
public class CloudProviderRepository {
    ArrayList<Cloud> clouds;

    BPTree<BigInteger> providers=new BPTree<BigInteger>(20 , 20 , 1);
    ArrayList<ValueNode> vn;
    public CloudProviderRepository(ArrayList<Cloud> clouds) {
        this.clouds = clouds;
        vn=new ArrayList<ValueNode>(clouds.size());
    }

    public void createBPlusTree(){
        for (Cloud c:clouds){
            Node<BigInteger> tempNode=providers.search2(c.index);
            ValueNode vl;
            if(tempNode==null){
                vl=new ValueNode();
                vl.clouds.add(c);
                providers.insertNode(c.index , vl);
                vn.add(vl);
            }else{
                vl= (ValueNode) tempNode.getPointers().get(tempNode.getKeys().indexOf(c.index));
                vl.clouds.add(c);
            }
        }
//        for (ValueNode tempvn:vn) {
//            BigInteger temp=tempvn.clouds.get(0).index;
//            for (ValueNode tempvn2:vn) {
//                BigInteger temp2=tempvn2.clouds.get(0).index;
//                if (temp.equals(temp2)){continue;}
//                if(temp.and(temp2).xor(temp).compareTo(BigInteger.ZERO)==0){
//                    tempvn.betterValueNodes.add(tempvn2);
//                }
//            }
//        }
        for (int i = 0; i < vn.size(); i++) {
            BigInteger temp=vn.get(i).clouds.get(0).index;
            for (int j = 0; j < vn.size(); j++) {
                if(i!=j){
                    BigInteger temp2=vn.get(j).clouds.get(0).index;
                    if(temp.testBit(47)==temp2.testBit(47) && temp.testBit(46)==temp2.testBit(46) && temp.and(temp2).xor(temp).compareTo(BigInteger.ZERO)==0){
                        vn.get(i).betterValueNodes.add(vn.get(j));
                    }
                }
            }
        }
    //    System.out.println(vn.size());
    }
}
