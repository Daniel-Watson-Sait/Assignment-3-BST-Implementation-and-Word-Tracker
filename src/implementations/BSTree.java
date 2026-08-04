package implementations;

import utilities.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Implements a generic Binary Search Tree (BST) using the BSTreeADT
 * interface. The tree supports insertion, searching, removal of minimum
 * and maximum elements, and inorder, preorder, and postorder traversal.
 *
 * @param <E> the type of comparable element stored in the tree
 * @author Habin Park
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {

	/**
	 * Serialization version identifier.
	 */
	private static final long serialVersionUID = 1L;

	private BSTreeNode<E> root;
	private int size;

	/**
	 * Creates an empty binary search tree.
	 */
	public BSTree() {
		size = 0;
		root = null;
	}

	/**
	 * Creates a binary search tree with the specified root element.
	 *
	 * @param entry the element to store as the root
	 * @throws NullPointerException if the entry is {@code null}
	 */
	public BSTree(E entry) {

		if (entry == null)
			throw new NullPointerException();

		root = new BSTreeNode<>(entry);
		size = 1;
	}

	/**
	 * Returns the root node of the tree.
	 *
	 * @return the root node
	 * @throws NullPointerException if the tree is empty
	 */
	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException {

		if (root == null)
			throw new NullPointerException();

		return root;
	}

	/**
	 * Returns the height of the tree.
	 *
	 * @return the height of the tree
	 */
	@Override
	public int getHeight() {
		return height(root);
	}

	/**
	 * Recursively calculates the height of the specified subtree.
	 *
	 * @param node the subtree root
	 * @return the height of the subtree
	 */
	private int height(BSTreeNode<E> node) {

		if (node == null)
			return 0;

		return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
	}

	/**
	 * Returns the number of elements in the tree.
	 *
	 * @return the number of elements
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Determines whether the tree is empty.
	 *
	 * @return {@code true} if the tree contains no elements;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes all elements from the tree.
	 */
	@Override
	public void clear() {
		root = null;
		size = 0;

	}

	/**
	 * Determines whether the specified element exists in the tree.
	 *
	 * @param entry the element to locate
	 * @return {@code true} if the element exists;
	 *         {@code false} otherwise
	 * @throws NullPointerException if the entry is {@code null}
	 */
	@Override
	public boolean contains(E entry) throws NullPointerException {
		if (entry == null)
			throw new NullPointerException();

		return search(entry) != null;
	}

	/**
	 * Searches the tree for the specified element.
	 *
	 * @param entry the element to locate
	 * @return the node containing the element, or {@code null} if not found
	 * @throws NullPointerException if the entry is {@code null}
	 */
	@Override
	public BSTreeNode<E> search(E entry) throws NullPointerException {
		if (entry == null)
			throw new NullPointerException();

		BSTreeNode<E> current = root;

		while (current != null) {
			int compare = entry.compareTo(current.getElement());

			if (compare == 0)
				return current;

			if (compare < 0) {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}
		}

		return null;
	}

	/**
	 * Inserts a new element into the binary search tree.
	 *
	 * @param newEntry the element to insert
	 * @return {@code true} if the element is successfully added
	 * @throws NullPointerException if the entry is {@code null}
	 */
	@Override
	public boolean add(E newEntry) throws NullPointerException {
		if (newEntry == null)
			throw new NullPointerException();

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

	/**
	 * Removes and returns the node containing the minimum element.
	 *
	 * @return the removed minimum node, or {@code null} if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMin() {

		if (root == null)
			return null;

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

	/**
	 * Removes and returns the node containing the maximum element.
	 *
	 * @return the removed maximum node, or {@code null} if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMax() {

		if (root == null)
			return null;

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

	/**
	 * Returns an iterator that traverses the tree in inorder.
	 *
	 * @return an inorder iterator
	 */
	@Override
	public Iterator<E> inorderIterator() {

		ArrayList<E> list = new ArrayList<>();
		inorder(root, list);

		return new TreeIterator(list);
	}

	/**
	 * Returns an iterator that traverses the tree in preorder.
	 *
	 * @return a preorder iterator
	 */
	@Override
	public Iterator<E> preorderIterator() {
		ArrayList<E> list = new ArrayList<>();
		preorder(root, list);

		return new TreeIterator(list);
	}

	/**
	 * Returns an iterator that traverses the tree in postorder.
	 *
	 * @return a postorder iterator
	 */
	@Override
	public Iterator<E> postorderIterator() {
		ArrayList<E> list = new ArrayList<>();
		postorder(root, list);

		return new TreeIterator(list);
	}

	/**
	 * Performs an inorder traversal of the specified subtree.
	 *
	 * @param node the subtree root
	 * @param list the list that stores the traversal result
	 */
	private void inorder(BSTreeNode<E> node, ArrayList<E> list) {

		if (node == null)
			return;

		inorder(node.getLeft(), list);
		list.add(node.getElement());
		inorder(node.getRight(), list);
	}

	/**
	 * Performs a preorder traversal of the specified subtree.
	 *
	 * @param node the subtree root
	 * @param list the list that stores the traversal result
	 */
	private void preorder(BSTreeNode<E> node, ArrayList<E> list) {

		if (node == null)
			return;

		list.add(node.getElement());
		preorder(node.getLeft(), list);
		preorder(node.getRight(), list);
	}

	/**
	 * Performs a postorder traversal of the specified subtree.
	 *
	 * @param node the subtree root
	 * @param list the list that stores the traversal result
	 */
	private void postorder(BSTreeNode<E> node, ArrayList<E> list) {

		if (node == null)
			return;

		postorder(node.getLeft(), list);
		postorder(node.getRight(), list);
		list.add(node.getElement());
	}

	/**
	 * An iterator implementation backed by an ArrayList containing
	 * the traversal order of the tree.
	 */
	private class TreeIterator implements Iterator<E> {

		private ArrayList<E> list;
		private int current;

		/**
		 * Creates an iterator for the specified traversal list.
		 *
		 * @param list the traversal result to iterate over
		 */
		public TreeIterator(ArrayList<E> list) {
			this.list = list;
			current = 0;
		}

		/**
		 * Determines whether another element exists in the iteration.
		 *
		 * @return {@code true} if another element is available;
		 *         {@code false} otherwise
		 */
		@Override
		public boolean hasNext() {
			return current < list.size();
		}

		/**
		 * Returns the next element in the iteration.
		 *
		 * @return the next element
		 * @throws NoSuchElementException if no more elements are available
		 */
		@Override
		public E next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements.");
			}

			return list.get(current++);
		}

	}
}