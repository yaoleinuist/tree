package com.tree.b;

/**
 * @ClassName: BinaryTree
 * @Description: 平衡查找树
 * @author yaolei
 * @date 2018年9月15日
 * 
 * @param <K>
 * @param <V>
 */

public class BinaryTree<K extends Comparable<K>, V> {
	// Comparable和Compartor的区别，
	// Comparable适用于内部比较，compartor更像是外部比较器。如当String类的比较，就不可以适用Comparable

	private Node<K, V> root;

	public BinaryTree(Node<K, V> root2) {
		this.root = root2;
	}

	public V get(K k) {
		return get(root, k);
	}

	public V get(Node<K, V> node, K k) {

		if (node == null) {
			return null;
		}
		int compareTo = k.compareTo(node.getKey());

		if (compareTo == 0) {
			return node.getValue();
		} else if (compareTo < 0) {
			return get(node.getLeft(), k);
		} else {
			return get(node.getRight(), k);
		}
	}

	public void put(K k, V v) {
		put(root,k, v);
	}

	public Node<K, V> put(Node<K, V> node, K k, V v) {

		if (node == null) {
		    node = new Node(k, v);
			return node;
		}
		
		int compareTo = k.compareTo(node.getKey());
		if (compareTo == 0) {
			node.setValue(v);
		} else if (compareTo < 0) {
			node.setLeft(put(node.getLeft(), k, v));
		} else {
			node.setRight(put(node.getRight(), k, v));
		}
		
		return node;
	}
	//中左右
	public static void preOrder(Node<Integer,String> node) {
		if(node == null) {
			return;
		}
		System.out.println(node.getKey());
		preOrder(node.getLeft());
	//	System.out.println(node.getKey());
		preOrder(node.getRight());
	}
	
	//左中右
	public static void inOrder(Node<Integer,String> node) {
		
		if(node == null) {
			return;
		}
		inOrder(node.getLeft());
		System.out.println(node.getKey());
	//	System.out.println(node.getKey());
		inOrder(node.getRight());
		
	}
	
	public static void postOrder(Node<Integer,String> node) {
		if(node == null) {
			return;
		}
		postOrder(node.getRight());
		System.out.println(node.getKey());
	//	System.out.println(node.getKey());
		postOrder(node.getLeft());
		
	}
	
	public static void main(String[] args) {
	
		Node<Integer,String> root = new Node<Integer,String>(12,"t12");
		BinaryTree<Integer,String> tree = new BinaryTree<Integer,String>(root);
		tree.put(8, "t8");
		tree.put(5, "t5");
		tree.put(6, "t6");
		tree.put(7, "t7");
		tree.put(19, "t19");
		tree.put(15, "t15");
		tree.put(13, "t13");
		tree.put(16, "t16");
		tree.put(21, "t21");
	//	preOrder(root);
	//	inOrder(root);
		postOrder(root);
	//	System.out.println(tree.toString());
	}
	

	static class Node<K, V> {

		private Node<K, V> left;
		private Node<K, V> right;
		private K key;
		private V value;

		public Node<K, V> getLeft() {
			return left;
		}

		public void setLeft(Node<K, V> left) {
			this.left = left;
		}

		public Node<K, V> getRight() {
			return right;
		}

		public void setRight(Node<K, V> right) {
			this.right = right;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Node(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

	}


	@Override
	public String toString() {
		return "BinaryTree [root=" + root.getKey()+"-"+root.getValue() +"-"+ root.getLeft().getKey()+"-"+root.getRight().getValue() + "]";
	}

}
