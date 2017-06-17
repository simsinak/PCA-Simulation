package BPTree.implementation;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by sinaaskarnejad on 5/22/16.
 */
public class TestBPTree {
    public static void main(String[] args) {
        BPTree<Integer> bpTree=new BPTree<Integer>(2,10,3);
        bpTree.insertNode(12,"A");
        bpTree.insertNode(14 ,"D");

        bpTree.insertNode(13,"C");
        bpTree.insertNode(15,"E");
        bpTree.insertNode(11,"F");
        bpTree.insertNode(22,"AA");
        bpTree.insertNode(4 ,"DD");

        bpTree.insertNode(23,"CC");
        bpTree.insertNode(5,"EE");
        bpTree.insertNode(9,"FF");
     //   System.out.println(Arrays.toString(bpTree.getRoot().getKeys().toArray()));
     //   System.out.println(bpTree.search(13).getPointers().get(bpTree.search(13).getKeys().indexOf(new Integer(13))));
      //  System.out.println(bpTree.search(88));
    //    System.out.println(bpTree.print());
        BitSet bs=new BitSet(8);
      //  bs.set(10101110);
        bs.set(1);
        bs.set(2);
        bs.set(3);
        bs.set(5);
        bs.set(7);

        BitSet bs2=new BitSet(8);
      //  bs2.set(10101010);
        bs2.set(1);
        bs2.set(3);
        bs2.set(5);
        bs2.set(7);
        BigInteger b2=new BigInteger("11000101010001110011110101111011111100111100110000100101010101010100011111000", 2);
        BigInteger b=new BigInteger("11000101010001110011110101111011111100111100110000100101010101010100011111100", 2);
        System.out.println(b2.and(b).xor(b2).compareTo(BigInteger.ZERO));
        System.out.println(b2.toString(2));
       //  long a = Long.parseLong("11000101010001110011110101111011111100111100110000100101010101010100011111000", 2);
      //  int b = Integer.parseInt("10101010", 2);
        //System.out.println((b&a)^b);
        char[] bits=new char[5];
        Arrays.fill(bits , 0 , 1 , '1');
       // System.out.println(bs2.toString());
        //int[] bits=new int[12];
       // Arrays.fill(bits,48);
       // bits[12-5]=49;
        System.out.println(Arrays.toString(bits));
        System.out.println(new String(bits , 0 , bits.length));
    }
}
