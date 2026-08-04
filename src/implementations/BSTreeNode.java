package implementations;

import java.io.Serializable;

/**
 * Represents a node in a binary search tree.
 * Each node stores an element along with references to its left and
 * right child nodes.
 *
 * @param <E> the type of element stored in the node
 * @author Habin Park
 */
public class BSTreeNode<E> implements Serializable {

	/**
	 * Serialization version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The element stored in this node.
	 */
	private E element;

	/**
	 * Reference to the left child node.
	 */
	private BSTreeNode<E> left;

	/**
	 * Reference to the right child node.
	 */
	private BSTreeNode<E> right;

	/**
	 * Creates a new binary search tree node containing the specified element.
	 *
	 * @param element the element to store in this node
	 */
	public BSTreeNode(E element) {
		this.element = element;
		left = null;
		right = null;
	}

	/**
	 * Returns the element stored in this node.
	 *
	 * @return the stored element
	 */
	public E getElement() {
		return element;
	}

	/**
	 * Replaces the element stored in this node.
	 *
	 * @param element the new element to store
	 */
	public void setElement(E element) {
		this.element = element;
	}

	/**
	 * Returns the left child of this node.
	 *
	 * @return the left child node, or {@code null} if no left child exists
	 */
	public BSTreeNode<E> getLeft() {
		return left;
	}

	/**
	 * Sets the left child of this node.
	 *
	 * @param left the node to assign as the left child
	 */
	public void setLeft(BSTreeNode<E> left) {
		this.left = left;
	}

	/**
	 * Returns the right child of this node.
	 *
	 * @return the right child node, or {@code null} if no right child exists
	 */
	public BSTreeNode<E> getRight() {
		return right;
	}

	/**
	 * Sets the right child of this node.
	 *
	 * @param right the node to assign as the right child
	 */
	public void setRight(BSTreeNode<E> right) {
		this.right = right;
	}

}