package BPTree.implementation;

import java.math.BigInteger;

/**
 * Created by sinaaskarnejad on 5/24/16.
 */
public class CapsulateReault<E> {
    public Node<E> node;
    public int position;
    public BigInteger keyIndex;

    public CapsulateReault(Node<E> node, int position, BigInteger keyIndex) {
        this.node = node;
        this.position = position;
        this.keyIndex = keyIndex;
    }
}
