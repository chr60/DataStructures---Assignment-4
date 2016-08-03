// Christen Reinbeck

import java.util.Iterator;
import StackAndQueuePackage.*; // Needed by tree iterators
import java.util.NoSuchElementException;

// used format and methods from BinaryTree and BinaryNode that we were given to complete this assignment
public class TernaryTree<T> implements TernaryTreeInterface<T>{
    private TernaryNode<T> root;

	
	// CONSTRUCTORS
	public TernaryTree(){
		root = null;
	}
	
	public TernaryTree(T rootData){
		root = new TernaryNode<>(rootData);
	}
	
	public TernaryTree(T rootData, TernaryTree<T> leftTree, TernaryTree<T> middleTree, TernaryTree<T> rightTree){
		privateSetTree(rootData, leftTree, middleTree, rightTree);
	}
	
	
	// SETTING THE TREE
	@Override
	public void setTree(T rootData) {
		 root = new TernaryNode<>(rootData);		
	}

	@Override
	public void setTree(T rootData, TernaryTreeInterface<T> leftTree, TernaryTreeInterface<T> middleTree, TernaryTreeInterface<T> rightTree) {
		privateSetTree(rootData, (TernaryTree<T>)leftTree, (TernaryTree<T>)middleTree, (TernaryTree<T>)rightTree);
	}
	
	private void privateSetTree(T rootData, TernaryTree<T> leftTree, TernaryTree<T> middleTree, TernaryTree<T> rightTree) {
		root = new TernaryNode<>(rootData);
		
		if ((leftTree != null) && !leftTree.isEmpty()) {
			root.setLeftChild(leftTree.root);
		}
		
		//added middle logic
		if ((middleTree != null) && !middleTree.isEmpty()) {
			if (middleTree != leftTree) {
				root.setMiddleChild(middleTree.root);
			} else {
				root.setMiddleChild(middleTree.root.copy());
			}
		}
		
		//added new right logic
		if ((rightTree != null) && !rightTree.isEmpty()) {
			if (rightTree != middleTree && rightTree != leftTree) {
				root.setRightChild(rightTree.root);
			} else {
				root.setRightChild(rightTree.root.copy());
			}
		}
		
		if ((leftTree != null) && (leftTree != this)) {
			leftTree.clear();
		}
		
		if ((rightTree != null) && (rightTree != this)) {
			rightTree.clear();
		}
		
		//added middle clear logic
		if((middleTree != null) && (middleTree != this)){
			middleTree.clear();
		}
	}

	
	@Override
	public T getRootData() {
		if (isEmpty()) {
            throw new EmptyTreeException();
        } else {
            return root.getData();
        }
	}

	@Override
	public int getHeight() {
        return root.getHeight();
	}

	@Override
	public int getNumberOfNodes() {
        return root.getNumberOfNodes();
	}

	@Override
	public boolean isEmpty() {
        return root == null;
	}

	@Override
	public void clear() {
        root = null;		
	}
	
    protected void setRootData(T rootData) {
        root.setData(rootData);
    }

    protected void setRootNode(TernaryNode<T> rootNode) {
        root = rootNode;
    }

    protected TernaryNode<T> getRootNode() {
        return root;
    }

	@Override
	public Iterator<T> getPreorderIterator() {
        return new PreorderIterator();
	}

	@Override
	public Iterator<T> getPostorderIterator() {
        return new PostorderIterator();
	}

	@Override
	// Ternary Tree does not support in order iteration because of the definition of in order traversal
	// because in order looks at the bottom of each node as it goes around the elements of the tree
	// with a middle child tree, the iterator will find the bottom of the root node twice - once before traversing the middle child and once after
	// you can't hit/iterate through a node more than once
	public Iterator<T> getInorderIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> getLevelOrderIterator() {
        return new LevelOrderIterator();
	}

// PRE ORDER ITERATOR	
    private class PreorderIterator implements Iterator<T> {
        private StackInterface<TernaryNode<T>> nodeStack;

        public PreorderIterator() {
            nodeStack = new LinkedStack<>();
            if (root != null) {
                nodeStack.push(root);
            }
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty();
        }

        public T next() {
            TernaryNode<T> nextNode;

            if (hasNext()) {
                nextNode = nodeStack.pop();
                TernaryNode<T> leftChild = nextNode.getLeftChild();
                TernaryNode<T> middleChild = nextNode.getMiddleChild();
                TernaryNode<T> rightChild = nextNode.getRightChild();

                // Push into stack in reverse order of recursive calls
                if (rightChild != null) {
                    nodeStack.push(rightChild);
                }
                
                if (middleChild != null) {
                	nodeStack.push(middleChild);
                }

                if (leftChild != null) {
                    nodeStack.push(leftChild);
                }
            } else {
                throw new NoSuchElementException();
            }

            return nextNode.getData();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void iterativePreorderTraverse() {
        StackInterface<TernaryNode<T>> nodeStack = new LinkedStack<>();
        if (root != null) {
            nodeStack.push(root);
        }
        TernaryNode<T> nextNode;
        while (!nodeStack.isEmpty()) {
            nextNode = nodeStack.pop();
            TernaryNode<T> leftChild = nextNode.getLeftChild();
            TernaryNode<T> middleChild = nextNode.getMiddleChild();
            TernaryNode<T> rightChild = nextNode.getRightChild();

            // Push into stack in reverse order of recursive calls
            if (rightChild != null) {
                nodeStack.push(rightChild);
            }
            
            if (middleChild != null) {
            	nodeStack.push(middleChild);
            }

            if (leftChild != null) {
                nodeStack.push(leftChild);
            }

            System.out.print(nextNode.getData() + " ");
        }
    }
    
// POST ORDER ITERATOR
    private class PostorderIterator implements Iterator<T> {
        private StackInterface<TernaryNode<T>> nodeStack;
        private TernaryNode<T> currentNode;
        private TernaryNode<T> newNode;

        public PostorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty() || (currentNode != null);
        }
        public T next() {
            boolean foundNext = false;
            TernaryNode<T> leftChild, middleChild, rightChild, nextNode = null;

            // Find leftmost leaf
            while (currentNode != null) {
                nodeStack.push(currentNode);
                leftChild = currentNode.getLeftChild();
                if (leftChild == null) {
                    newNode = currentNode.getMiddleChild();
                    if(newNode == null){
                    	currentNode = currentNode.getRightChild();
                    }else{
                    	currentNode = newNode;
                    }
                } else {
                    currentNode = leftChild;
                }
            }
            

            // Stack is not empty either because we just pushed a node, or
            // it wasn't empty to begin with since hasNext() is true.
            // But Iterator specifies an exception for next() in case
            // hasNext() is false.

            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                // nextNode != null since stack was not empty before pop

                TernaryNode<T> parent = null;
                TernaryNode<T> middleNode = parent.getMiddleChild();
                TernaryNode<T> rightNode = parent.getRightChild();
                if (!nodeStack.isEmpty()) {
                    parent = nodeStack.peek();
                    if (nextNode == parent.getLeftChild()) {
                    	if(middleNode == null){
                    		currentNode = rightNode;
                    	}else{
                    		// since middleNode is null, currentNode would be null
                    		currentNode = middleNode;
                    	}
                    } else if(nextNode == middleNode){
                    	currentNode = rightNode;
                    }else {
                        currentNode = null;
                    }
                } else {
                    currentNode = null;
                }
            } else {
                throw new NoSuchElementException();
            }

            return nextNode.getData();
        }


        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
// LEVEL ORDER ITERATOR
    private class LevelOrderIterator implements Iterator<T> {
        private QueueInterface<TernaryNode<T>> nodeQueue;

        public LevelOrderIterator() {
            nodeQueue = new LinkedQueue<>();
            if (root != null) {
                nodeQueue.enqueue(root);
            }
        }

        public boolean hasNext() {
            return !nodeQueue.isEmpty();
        }

        public T next() {
            TernaryNode<T> nextNode;

            if (hasNext()) {
                nextNode = nodeQueue.dequeue();
                TernaryNode<T> leftChild = nextNode.getLeftChild();
                TernaryNode<T> middleChild = nextNode.getMiddleChild();
                TernaryNode<T> rightChild = nextNode.getRightChild();

                // Add to queue in order of recursive calls
                if (leftChild != null) {
                    nodeQueue.enqueue(leftChild);
                }
                
                if(middleChild != null) {
                	nodeQueue.enqueue(middleChild);
                }

                if (rightChild != null) {
                    nodeQueue.enqueue(rightChild);
                }
            } else {
                throw new NoSuchElementException();
            }

            return nextNode.getData();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
	
}
