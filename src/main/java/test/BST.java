package test;

import java.util.LinkedList;
import java.util.List;

public class BST
{
  
  Node root;

  class Node
  {
    int val;
    Node left;
    Node right;

    public void addNode(int i)
    {
      if (i < val) {
        if (left == null) {
          left = new Node();
          left.val = i;
        } else {
          left.addNode(i);
        }
      } else {
        if (right == null) {
          right = new Node();
          right.val = i;
        } else {
          right.addNode(i);
        }
      }
    }
  }

  public void addNode(int i)
  {
    if (root == null) {
      root = new Node();
      root.val = i;
      return;
    }
    root.addNode(i);
  }
  
  
  public Node[] findInOnSpace()
  {
    List<Node> nl = new LinkedList<BST.Node>();
    visitOnSpace(root, null, nl);
    Node[] swap = new Node[2];
    int i = Integer.MIN_VALUE;
    int j = 0;
    for (Node node : nl) {
      if(node.val < i){
        j++;
      }
      if(j>=2){
        return swap;
      }
      swap[j] = node;
    }
    return swap;
  }


  private void visitOnSpace(Node n, Node p, List<Node> nl)
  {
    if(n == null)
      return;
    visitOnSpace(n.left, n, nl);
    nl.add(n);
    visitOnSpace(n.right, n, nl);
  }
  
  
  
  public Node[] findInO1Space()
  {
    Node[] swap = new Node[2];
    visitO1Space(root, swap, 0);
    return swap;
  }


  private void visitO1Space(Node n, Node[] swap, int index)
  {
    if(n==null)
      return;
    visitO1Space(n.left,  swap, index);
    if(swap[index]==null){
      swap[index] = n;
      return;
    } else {
      if(swap[index].val > n.val){
        if(index == 0)
          index++;
        else return;
      }
      swap[index] = n;
    }
    visitO1Space(n.right, swap, index);
  }

  

}
