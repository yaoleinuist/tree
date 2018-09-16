package com.tree.b;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: BTree
 * @Description: b-tree
 * @author yaolei
 * @date 2018年9月15日
 *
 */
public class BTree<K extends Comparable<K>, V> {

	private int m;// 度

	private Node<K, V> root;

	public BTree(int m, Node<K, V> root) {
		super();
		this.m = m;
		this.root = root;
	}

	public V get(K k) {
		return get(root, k);
	}

	public V get(Node<K, V> node, K k) {

		if (node == null || node.list.size() <= 0) {
			return null;
		}

		int compareTo;
		for (KV<K, V> kv : node.list) {
			if (null == kv.getKey()) {
				return get(kv.childNode, k);
			}
			compareTo = k.compareTo(kv.getKey());
			if (compareTo == 0) {
				return kv.getValue();
			} else if (compareTo < 0) {
				return get(kv.childNode, k);
			} else if (compareTo > 0) {
				continue;
			}
		}
		return null;
	}

	public void put(K k, V v) throws Exception {
		put(root, k, v);
	}

	public void put(Node<K, V> node, K k, V v) throws Exception {
		if (node == null || node.list == null) {
			return;
		}
		if (k == null) {
			throw new Exception("");
		}
		KV<K, V> kv = null;
		int compareTo;
		int index = -1;
		// 查询节点中的keylist
		for (int i = 0; i < node.list.size(); i++) {
			kv = node.list.get(i);

			if (null == kv.getKey()) {
				// tail节点，如果tail没有子节点，直接添加至tail之前
				if (null == kv.childNode) {
					index = node.list.size() - 1;
				}
				// tail节点，如果tail有子节点,递归
				if (null != kv.childNode) {
					put(kv.childNode, k, v);
				}
				break;
			}
			compareTo = k.compareTo(kv.getKey());
			if (compareTo == 0) {
				kv.value = v;
			} else if (compareTo < 0) {

				if (null == kv.getKey()) {
					// 如果没有子节点，直接添加至该节点之前
					if (null == kv.childNode) {
						index = i;
					}
					// tail节点，如果tail有子节点,递归
					if (null != kv.childNode) {
						put(kv.childNode, k, v);
					}
					break;
				}
			} else if (compareTo > 0) {
				continue;
			}
			if (index >= 0) {
				node.addKv(index, new KV<K,V>(k,v));
			}
		}
		// 平衡性操作，节点的key > m-1,m+1 包括tail节点
		if (node.list.size() >= m + 1) {
			spliteNode(node);
		}
	}

	private void spliteNode(Node<K, V> node) {
		if (null == node) {
			return;
		}
		// q确定要提升的KV
		int indexPro = (m - 1) / 2;// m-1去除tail
		KV<K, V> pro = node.list.get(indexPro);
		if (null == pro.key) {
			return;
		}
		// 开始分裂,研究一下subList，
		List<KV<K, V>> leftList = new ArrayList<KV<K, V>>(node.list.subList(0, indexPro));
		List<KV<K, V>> rightList = new ArrayList<KV<K, V>>(node.list.subList(indexPro + 1, node.list.size()));

		Node<K, V> leftNode = new Node<K, V>(leftList, pro);
		Node<K, V> rightNode = new Node<K, V>(rightList, pro);

		Node<K, V> parentNode = null;

		// 开始提升，如果没有父node
		if (null == node.parentKV) {
			List<KV<K, V>> list = new ArrayList<KV<K, V>>();
			list.add(pro);
			parentNode = new Node<K, V>(list, null);
			pro.childNode = leftNode;
			parentNode.list.get(1).childNode = rightNode;
			
			//处理root
			root = null;
			root= parentNode;
			
		} else {
			parentNode = node.parentKV.node;
			//父节点kv的childrennode操作
			pro.childNode = leftNode;
			parentNode.list.get(node.parentKV.getIndex()+1).childNode = rightNode;
			
			//添加到父节点
			parentNode.addKv(node.parentKV.getIndex()+1, pro);
			
			//父节点递归分裂
			if (parentNode.list.size() >= m + 1) {
				spliteNode(parentNode);
			}
		}
	}

	static class KV<K, V> {

		public KV(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public int getIndex() {
			return this.node.list.indexOf(this);
		}

		public K key;

		public V value;

		public Node<K, V> childNode;

		public Node<K, V> node;

		public Node<K, V> getNode() {
			return node;
		}

		public void setNode(Node<K, V> node) {
			this.node = node;
		}

		public Node<K, V> getChildNode() {
			return childNode;
		}

		public void setChildNode(Node<K, V> childNode) {
			this.childNode = childNode;
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

		@Override
		public String toString() {
			return "KV [key=" + key + ", value=" + value + ", childNode=" + childNode + ", node=" + node + "]";
		}

	}

	static class Node<K, V> {

		private List<KV<K, V>> list;// keys.size+1

		private KV<K, V> parentKV;

		public Node(List<KV<K, V>> list, KV<K, V> parentKV) {
			super();
			// 添加tail
			if (list != null && list.get(list.size() - 1).getKey() != null) {
				list.add(new KV<K, V>(null, null));
			}

			for (KV<K, V> kv : list) {
				kv.node = this;
			}

			this.list = list;
			this.parentKV = parentKV;
		}

		public void addKv(int index, KV<K, V> kv) {
			if (null == kv) {
				return;
			}
			kv.node = this;
			list.add(kv);
		}

		@Override
		public String toString() {
			return "Node [list=" + list.size() + ", parentKV=" + parentKV + "]";
		}

	}

	public static void main(String[] args) throws Exception {

		List<KV<Integer, String>> kvlist = new ArrayList<BTree.KV<Integer, String>>();
		kvlist.add(new KV<Integer, String>(8, "t8"));
//		kvlist.add(new KV<Integer, String>(9, "t9"));
//		kvlist.add(new KV<Integer, String>(10, "t10"));

		Node<Integer, String> root = new Node<Integer, String>(kvlist, null);
		BTree<Integer, String> ree = new BTree<Integer, String>(4, root);
		ree.put(8, "t8");
		ree.put(12, "t12");
		ree.put(21, "t21");
		ree.put(5, "t5");
        
		System.out.print(ree);

	}
}
