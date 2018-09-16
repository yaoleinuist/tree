package com.tree.b;


/**
* @ClassName: RedBlockTree  
* @Description: 红黑树
* @author yaolei  
* @date 2018年9月15日  
*
 */

public class RedBlackTree<K extends Comparable<K>,V> {

	private Node<K, V> root;

	static enum EnumRedBlockColor{
		  red,black;
	}
	
	public RedBlackTree(Node<K, V> root2) {
		this.root = root2;
	}

	public Node<K, V> rotateleft(Node<K, V> node) {
		Node<K, V> res = node.right;
		//颜色变化
        res.color = node.color;
        node.color = EnumRedBlockColor.red;
		//子节点变化
   		node.right = res.left;
   		res.right = node;
		return res;
	}
	
	public Node<K, V> rotatecolor(Node<K, V> node) {
		
		node.left.color = node.color;
		node.right.color = node.color;
		node.color = EnumRedBlockColor.red;
		return node;
	}
	
	public Node<K, V> rotateright(Node<K, V> node) {
		
		Node<K, V> res = node.left;
		//颜色变化
        res.color = node.color;
        node.color = EnumRedBlockColor.red;
		//子节点变化
   		node.left = res.right;
   		res.right = node;
		return res;

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
		//插入操作
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
		//2-4平衡操作
		
		if(idRed(node) && !idRed(node)) {
			node = rotateleft(node);
		}
		if(idRed(node.getLeft()) && idRed(node.getLeft().getLeft())) {
			node = rotateright(node);
		}
		if(idRed(node.getLeft()) && idRed(node.getRight())) {
			node = rotateright(node);
		}

		return node;
	}	
	private boolean idRed(Node<K, V> node) {
	  if(node == null) {
		  return Boolean.FALSE;
	  }
	  return EnumRedBlockColor.red == node.color ? true:false;
	}
	static class Node<K, V> {

		private Node<K, V> left;
		private Node<K, V> right;
		private K key;
		private V value;
		private EnumRedBlockColor color;
		
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
			this.color = EnumRedBlockColor.red;
		}
	
	}
}
