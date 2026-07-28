package implementations;

import utilities.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BSTreeNode<E> root;
	private int size;
	
	public BSTree() {
		size = 0;
		root = null;
	}
	
	public BSTree(E entry) {
		
		if (entry == null) throw new NullPointerException();
		
		root = new BSTreeNode<>(entry);
		size = 1;
	}

	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException {

		if (root == null) throw new NullPointerException();
		
		return root;
	}

	@Override
	public int getHeight() {
		return height(root);
	}
	
	private int height(BSTreeNode<E> node) {
		
		if (node == null) return 0;
		
		return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
		
	}

	@Override
	public boolean contains(E entry) throws NullPointerException {
		if (entry == null) throw new NullPointerException();
		
		return search(entry) != null;
	}

	@Override
	public BSTreeNode<E> search(E entry) throws NullPointerException {
		if (entry == null) throw new NullPointerException();
		
		BSTreeNode<E> current = root;
		
		while (current != null) {
			int compare = entry.compareTo(current.getElement());
			
			if (compare == 0) return current;
			
			if (compare < 0) {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}
		}
		
		return null;
	}

	@Override
	public boolean add(E newEntry) throws NullPointerException {
		if (newEntry == null) throw new NullPointerException();
		
		BSTreeNode<E> newNode = new BSTreeNode<>(newEntry);
		
		if (root == null) {
			root = newNode;
			size++;
			return true;
		}
		
		BSTreeNode<E> parent = null;
		BSTreeNode<E> current = root;
		
		while (current != null) {
			parent = current;
			
			if (newEntry.compareTo(current.getElement()) < 0) {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}
		}
		
		if (newEntry.compareTo(parent.getElement()) < 0) {
			parent.setLeft(newNode);
		} else {
			parent.setRight(newNode);
		}
		
		size++;
		return true;
	}

	@Override
	public BSTreeNode<E> removeMin() {
		
		if (root == null) return null;
		
		BSTreeNode<E> parent = null;
		BSTreeNode<E> current = root;
		
		while (current.getLeft() != null) {
			parent = current;
			current = current.getLeft();
		}
		
		if (parent == null) {
			root = root.getRight();
		} else {
			parent.setLeft(current.getRight());
		}
		
		size--;
		
		return current;
	}

	@Override
	public BSTreeNode<E> removeMax() {
		
		if (root == null) return null;
		
		BSTreeNode<E> parent = null;
		BSTreeNode<E> current = root;
		
		while (current.getRight() != null) {
			parent = current;
			current = current.getRight();
		}
		
		if (parent == null) {
			root = root.getLeft();
		} else {
			parent.setRight(current.getLeft());
		}
		
		size--;
		
		return current;
	}

	@Override
	public Iterator<E> inorderIterator() {
		
		ArrayList<E> list = new ArrayList<>();
		inorder(root, list);
		
		return new TreeIterator(list);
	}

	@Override
	public Iterator<E> preorderIterator() {
		ArrayList<E> list = new ArrayList<>();
		preorder(root, list);
		
		return new TreeIterator(list);
	}

	@Override
	public Iterator<E> postorderIterator() {
		ArrayList<E> list = new ArrayList<>();
		postorder(root, list);
		
		return new TreeIterator(list);
	}
	
	private void inorder(BSTreeNode<E> node, ArrayList<E> list) {
		
		if (node == null) return;
		
		inorder(node.getLeft(), list);
		list.add(node.getElement());
		inorder(node.getRight(), list);
	}
	
	private void preorder(BSTreeNode<E> node, ArrayList<E> list) {
		
		if (node == null) return;
		
		
		list.add(node.getElement());
		preorder(node.getLeft(), list);
		preorder(node.getRight(), list);
	}

	private void postorder(BSTreeNode<E> node, ArrayList<E> list) {
	
		if (node == null) return;
		
		postorder(node.getLeft(), list);
		postorder(node.getRight(), list);
		list.add(node.getElement());
	}

	
	private class TreeIterator implements Iterator<E> {

		private ArrayList<E> list;
		private int current;
		
		public TreeIterator(ArrayList<E> list) {
			this.list = list;
			current = 0;
		}
		
		@Override
		public boolean hasNext() {
			return current < list.size();
		}

		@Override
		public E next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements.");
			}
			
			return list.get(current++);
		}
		
	}
}
