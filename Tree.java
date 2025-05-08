// Tree.java -- Red-black search tree package

package myTree;


// Red-black search tree class
//
public class Tree
{
    // Color constants
    //
    private final static boolean RED   = true;
    private final static boolean BLACK = false;

    // Pool of free nodes
    //
    private static Node pool = null;
    
    // Tree node
    //
    private static class Node
    {
	int     key;
	Object  item;
        Node    left;
	Node    right;
	boolean color;

	final static int BLOCK_SIZE = 32; // Number of nodes in a block

	// Node constructor
	//
	public Node ()
	{
	    key = 0;
	    item = null;
	    left = null;
	    right = null;
	    color = false;
	}
	public Node (int k, Object x, Node l, Node r, boolean c)
	{
	    key   = k;
	    item  = x;
	    left  = l;
	    right = r;
	    color = c;
	}

        // allocate -- Allocate a node by taking one from the pool
        //
        public static Node allocate ()
        {
            // If the pool is empty, allocate a block of nodes and put them
            // into the pool
            //
            if (pool == null)
		{
		    // Allocate a block of nodes
		    //
		    Node[] block = new Node[BLOCK_SIZE];
		    for (int i = 0; i < BLOCK_SIZE; i++)
			block[i] = new Node();

		    // Link the nodes in the block into a pool (reusing the
		    // field "right" to form a singly linked list)
		    //
		    pool = block[0];
		    for (int i = 0; i < BLOCK_SIZE - 1; i++)
			block[i].right = block[i + 1];
		    block[BLOCK_SIZE - 1].right = null;
		}

            // Take a node off the pool
            //
            Node v = pool;
            pool = v.right;

            return v;
        }
	public static Node allocate
	    (int k, Object x, Node l, Node r, boolean c)
        {
            Node v = allocate();

            // Initialize the node fields
            //
	    v.key   = k;
            v.item  = x;
            v.left  = l;
            v.right = r;
	    v.color = c;

            return v;
        }

        // free -- Free a node by putting it into the pool
        //
        public void free ()
        {
            this.right = pool;
            pool = this;
        }

	// Scaffolding
	//
	public boolean isBlack () { return color == BLACK; }
	public boolean isRed   () { return color == RED; }
    }

    // Null node
    //
    private static Node nil = new Node (0, null, null, null, BLACK);

    // Root of the tree
    //
    private Node root;

    // Tree constructor
    //
    public Tree () { root = nil; }

    // find -- Find the item associated with a key
    //
    // Returns the associated item if the key is in the tree, or null
    // otherwise.
    //

    public Object find (int k) {
		return findHelper(k, this.root); // call findHelper for the subtree rooted at the root
	} 

	// findHelper -- Helper function for the find method
	//
	// Returns the item associated with key k in the subtree rooted at
	// node v if it exists in the tree, or null otherwise. Runs in O(logn)
	// time
	//

	public Object findHelper(int k, Node v) {
		if (v == nil) // if the subtree is empty or k is not found, return null
			return null;
		else if (k == v.key) { // if the root of the subtree has key k, return its item
			return v.item;
		} else if (k < v.key) { // if k is less than the key of the subtrees root, search its left child
			return findHelper(k, v.left);
		} else { // if k is greater than the key of the subtrees root, search its right child
			return findHelper(k, v.right);
		}
	}

    // min -- Find the minimum key in a tree
    //
    // Returns the minimum key if the tree is not empty, or null otherwise.
    //

    public Integer min () {
		return minHelper(this.root); // call minHelper for subtree rooted at the root
	} 

	// minHelper -- Helper function for the min method
	//
	// Returns the minimum key in the subtree rooted at v if the subtree is not 
	// empty, or null otherwise. Runs in O(logn) time
	//

	public Integer minHelper(Node v) {
		if (v == nil) { // if the tree is empty, return null
			return null;
		} else  if (v.left == nil) { // if the subtree root has no left child, it's the minimum—return its key.
			return v.key;
		} else { // otherwise, search the left child for the minimum
			return minHelper(v.left);
		}
	}
    
    // max -- Find the maximum key in a tree
    //
    // Returns the maximum key if the tree is not empty, or null otherwise.
    //

    public Integer max () {
		return maxHelper(this.root); // call maxHelper for the subtree rooted at the root
	} 

	// maxHelper -- Helper function for the max method
	//
	// Returns the maximum key in the subtree rooted at v if the subtree is not 
	// empty, or null otherwise. Runs in O(logn) time
	//

	public Integer maxHelper(Node v) {
		if (v == nil) { // if the tree is empty, return null
			return null;
		} else if (v.right == nil) { // if the subtree root has no right child, its the maximum-return its key
			return v.key;
		} else { // otherwise, search the right child for the maximum
			return maxHelper(v.right);
		}
	}

    // next -- Find the next greater key
    //
    // Returns the next greater key in the tree if it exists, or null
    // otherwise.
    //

    public Integer next (int k) {
		return nextHelper(k, this.root, nil); // call nextHelper for the subtree rooted at the root
	} 

	// nextHelper -- Helper function for the next method
	// 
	// Returns the next greater key (successor) in the subtree rooted at v
	// if it exists, or null otherwise. Runs in O(logn) time
	//
	
	public Integer nextHelper(int k, Node v, Node p) {
		if (v == nil) { // encountered a nil leaf searching for a k which isn't in the tree
			if (p != nil && k < p.key) return p.key; // if the potential successor exists and k is less than its key, 
													 // return its key as the successor.	
			else return null;	// otherwise there is no successor, return null
		}
		if (k < v.key) { // if k is less than the current node's key, search in the left subtree and mark the current node as a potential successor
			return nextHelper(k, v.left, v);
		} else if (k > v.key) { // if k is greater than v's key, search in the right subtree and keep same potential successor
			return nextHelper(k, v.right, p); 
		} else if (v.right != nil) { // if k is found at v and it has a right subtree, successor is the minimum key in v's right subtree
			return minHelper(v.right); 
		} else if (p != nil) { // if k is found and there's no right subtree, the successor is the closest left ancestor (the marked potential successor)
			return p.key; 
		} else return null; // otherwise we have found the key, there is no right subtree, and no marked potential successor (must be max key), return null
	}



    // prev -- Find the next smaller key
    //
    // Returns the next smaller key in the tree if it exists, or null
    // otherwise.
    //

    public Integer prev (int k) {
		return prevHelper(k, this.root, nil); // call prevHelper for the subtree rooted at the root
	} 

	// prevHelper -- Recursive helper function for the prev method
	// 
	// Returns the next smaller key (predecessor) in the subtree rooted at v 
	// if it exists, or null otherwise. Runs in O(logn) time
	//

	public Integer prevHelper(int k, Node v, Node p) {
		if (v == nil) { // encountered a nil leaf searching for a k which isn't in the tree
			if (p != nil && k > p.key) return p.key; // if the potential predecessor exists and k is greater than its 
													 // key, return its key as the predecessor
			else return null; // otherwise there is no predecessor, return null
		}
		if (k < v.key) { // if k is less than the v's key, search in the left subtree and keep the same potential predecessor
			return prevHelper(k, v.left, p);
		} else if (k > v.key) { // if k is greater than v's key, search in the right subtree and mark v as a potential predecessor
			return prevHelper(k, v.right, v);
		} else if (v.left != nil) { // if k is found at v and it has a left subtree, predecessor is the max key in the left subtree
			return maxHelper(v.left); 
		} else if (p != nil) { // if k is found at v and there is no left subtree, the predecessor is the closest right ancestor (the marked potential predecessor)
			return p.key;
		} else return null; // otherwise we have found the key, there is no left subtree, and no marked potential predecessor (must be min key), return null

	}

    // insert -- Insert a key-item pair into a tree
    //
    // If the key is already in the tree, its associated item is updated.
    //

    public void insert (int k, Object x)
    {
	root = insert(root, k, x); // call insert at the root
	root.color = BLACK;
    }

	// insert -- Inserts a key-item pair at in a subtree rooted at node v
	//
	// If the key is already in the tree, its associated item is updated. 
	// Returns the neew subtree root agter insertion and rebalancing
	// Runs in O(logn) time. 
	// 

    private static Node insert (Node v, int k, Object x) {
		if (v == nil) { // if the tree is empty at this position, create a new red node
			return Node.allocate(k, x, nil, nil, RED);
		} else if (k < v.key) { // if key is less than current node, go left
			v.left = insert(v.left, k, x);
		} else if (k > v.key) { // if key is greater than current node, go right
			v.right = insert(v.right, k, x);
		} else { // key already exists in the tree, update the item
			v.item = x;
			return v;
		}

		// Restore red-black tree properties potentially violated by the insertion
		v = insertRestore(v, k);
		return v; // return the subtree root
	} 
        // This method recursively inserts the key-item pair (k, x) into the
	// subtree rooted at node v, and returns the new subtree rooted at v

    private static Node insertRestore (Node v, int k)
	// Restore the color invariants at node v after an insertion of key k
    {
	if (v != nil && v.isBlack())
	    if (k < v.key)
	    {
		Node w = v.left;
		if (w.isRed())
		    if (v.right.isRed())
		    {
			if ((k < w.key && w.left.isRed())
			    || (k > w.key && w.right.isRed())) // Cases 3 and 4
			    flipBoth(v);
		    }
		    else if (k < w.key && w.left.isRed()) // Case 5
		    {
			flipLeft(v);
			v = rotateRight(v);
		    }
		    else if (k > w.key && w.right.isRed()) // Case 6
		    {
			v.left = rotateLeft(v.left);
			flipLeft(v);
			v = rotateRight(v);
		    }
	    }
	    else
	    {
		Node w = v.right;
		if (w.isRed())
		    if (v.left.isRed())
		    {
			if ((k > w.key && w.right.isRed())
			    || (k < w.key && w.left.isRed())) // Cases 7 and 8
			    flipBoth(v);
		    }
		    else if (k > w.key && w.right.isRed()) // Case 9
		    {
			flipRight(v);
			v = rotateLeft(v);
		    }
		    else if (k < w.key && w.left.isRed()) // Case 10
		    {
			v.right = rotateRight(v.right);
			flipRight(v);
			v = rotateLeft(v);
		    }
	    }
	return v;
    }

    private static boolean defect; // true iff the tree needs an extra black
        // field "defect" is only used by method "delete"
    
    // delete -- Delete a key-item pair from a tree
    //
    // If the key is not in the tree, nothing is changed.
    //
    public void delete (int k)
    {
	defect = false;
	root = delete(root, k);
	root.color = BLACK;
    }
    private static Node delete (Node v, int k) // Helper function
    {
	if (v == nil)
	    return nil;
	else if (k < v.key)
	{
	    v.left = delete(v.left, k);
	    return deleteRestore(v, v.left);
	}
	else if (k > v.key)
	{
	    v.right = delete(v.right, k);
	    return deleteRestore(v, v.right);
	}
	else if (v.left == nil)
	    return deleteBasis(v, v.right);
	else if (v.right == nil)
	    return deleteBasis(v, v.left);
	else
	{
	    v.right = deleteGeneral(v.right, v);
	    return deleteRestore(v, v.right);
	}
    }
    private static Node deleteGeneral (Node w, Node p)
	// General case for deleting a node p with two children
    {
	if (w.left != nil) // Find the minimum node in the subtree rooted at w
	{
	    w.left = deleteGeneral(w.left, p);
	    return deleteRestore(w, w.left);
	}
	else // Copy the minimum node's contents into the ancestral node p
	{
	    p.key = w.key;
	    p.item = w.item;
	    return deleteBasis(w, w.right);
	}
    }
    private static Node deleteBasis (Node v, Node w)
	// Base case of splicing out a node v with at most one child w
    {
	if (v.isRed())
	    defect = false;
	else if (w.isBlack())
	    defect = true;
	else
	{
	    flip(w);
	    defect = false;
	}
	v.free();
	return w;
    }
    private static Node deleteRestore (Node v, Node w)
	// Restore the color invariants at a node w after a deletion
    {
	if (defect)
	{
	    if (w.isRed()) // Case 0
	    {
		flip(w);
		defect = false;
	    }
	    else if (w == v.left)
	    {
		Node u = v.right;
		if (u.isRed()) // Case 1
		{
		    flipRight(v);
		    v = rotateLeft(v);
		    v.left = deleteRestore(v.left, v.left.left);
		        // This call will encounter Cases 2-4 at v.left
		    if (defect) // Case 2 occurred at v.left
		    {
			flip(v.left);
			defect = false;
		    }
		}
		else if (u.left.isBlack() && u.right.isBlack()) // Case 2
		    flip(u);
		else // Cases 3 and 4
		{
		    if (u.left.isRed()) // Case 3
		    {
			v.right = rotateRight(v.right);
			flipRight(v.right);
		    }
		    v = rotateLeft(v);
		    flipLeft(v);
		    flip(v.right);
		    defect = false;
		}
	    }
	    else
	    {
		Node u = v.left;
		if (u.isRed()) // Case 5
		{
		    flipLeft(v);
		    v = rotateRight(v);
		    v.right = deleteRestore(v.right, v.right.right);
		        // This call will encounter Cases 6-8 at v.right
		    if (defect) // Case 6 occurred at v.right
		    {
			flip(v.right);
			defect = false;
		    }
		}
		else if (u.right.isBlack() && u.left.isBlack()) // Case 6
		    flip(u);
		else // Cases 7 and 8
		{
		    if (u.right.isRed()) // Case 7
		    {
			v.left = rotateLeft(v.left);
			flipLeft(v.left);
		    }
		    v = rotateRight(v);
		    flipRight(v);
		    flip(v.left);
		    defect = false;
		}
	    }
	}
	return v;
    }
   
    // empty -- Returns true iff the tree is empty
    //
    public boolean empty ()
    {
	return root == nil;
    }

    // size -- Returns the number of nodes in the tree
    //
    public int size ()
    {
	return size(root);
    }
    private static int size (Node v) // Helper function
    {
	return (v != nil) ? 1 + size(v.left) + size(v.right) : 0;
    }

    // clear -- Frees all nodes in the tree
    //
    public void clear ()
    {
	clear(root);
	root = nil;
    }
    private static void clear (Node v) // Helper function
    {
	if (v != nil)
	{
	    clear(v.left);
	    clear(v.right);
	    v.free();
	}
    }

    // copy -- Returns a shallow copy of the tree
    //
    public Tree copy ()
    {
	Tree t = new Tree();
	t.root = copy(root);
	return t;
    }
    private static Node copy (Node v) // Helper function
    {
       return (v != nil)
	   ? Node.allocate(v.key, v.item, copy(v.left), copy(v.right), v.color)
	   : nil;
    }

    // Scaffolding
    //
    private static void flip (Node v) // Flip the color at a node
    {
	v.color = v.isBlack() ? RED : BLACK;
    }
    private static void flipLeft (Node v) // Exchange colors with left child
    {
	boolean c = v.color;
	v.color = v.left.color;
	v.left.color = c;
    }
    private static void flipRight (Node v) // Exchange colors with right child
    {
	boolean c = v.color;
	v.color = v.right.color;
	v.right.color = c;
    }
    private static void flipBoth (Node v) // Exchange colors with both children
    {
	boolean c = v.color;
	v.color = v.left.color;
	v.left.color = c;
	v.right.color = c;
    }
    private static Node rotateRight (Node v) // Right rotation
    {
	Node w = v.left;
	v.left = w.right;
	w.right = v;
	return w;
    }
    private static Node rotateLeft (Node v) // Left rotation
    {
	Node w = v.right;
	v.right = w.left;
	w.left = v;
	return w;
    }
    private boolean valid () // Is this a valid red-black tree? 
    {
	// Count the number of black nodes on the left chain
	//
	int b = 0;
	for (Node v = root; v != nil; v = v.left)
	    b += (v.isBlack() ? 1 : 0);
	
	return root.isBlack() && valid(root, 0, b);
    }
    private static boolean valid (Node v, int i, int b) // Helper function
    {
	if (v != nil)
	    return (v.isBlack()
		    || (v.isRed() && v.left.isBlack() && v.right.isBlack()))
		&& valid(v.left, i + (v.isBlack() ? 1 : 0), b)
		&& valid(v.right, i + (v.isBlack() ? 1 : 0), b);
	else
	    return i == b;
    }
    private void dump () // Prints out a red-black tree in preorder
    {
        dump(root, 0);
    }
    private static void dump (Node v, int d) // Helper function
    {
        if (v != nil)
	{
	    for (int i = 1; i <= d; i++)
		System.out.print(" ");
	    System.out.print(v.key + " " + v.item);
	    System.out.println(v.isBlack() ? " black" : " red");
	    dump(v.left, d+1);
	    dump(v.right, d+1);
	}
    }
}