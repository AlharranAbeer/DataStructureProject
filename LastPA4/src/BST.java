class BSTNode <K extends Comparable <K>, T > {
	
	public K key ;
	public T data;
	public BSTNode<K, T> left , right;
	public BSTNode(K k, T val) {
		key = k;
		data = val;
		left = right = null;
		}
		public BSTNode(K k, T val, BSTNode<K , T> l, BSTNode<K , T> r) {
		key = k;
		data = val;
		left = l;
		right = r;}
		}

public class BST <K extends Comparable <K>, T> implements Map<K, T> {
	BSTNode<K,T> current ,root;
	
	public BST() {
		current = root = null;
	}
	
	public boolean empty() {
		return root == null;
	}

	public boolean full() {
		return false;
	}

	public void clear() {
	  current =  root = null ;}

	public T retrieve() {
		return current.data;
	}

	public void update(T e) {
		current.data = e;
		
	}
	 @Override
	public boolean find(K key) {
		BSTNode<K,T> temp1 = root, temp2 = current;	
		if (empty())
			return false;
		boolean flag = false;
		while (temp1 != null) {
			if (temp1.key.compareTo(key) == 0) {
				current = temp1 ;
				flag = true;
				break;
			}
				if (key.compareTo(temp1.key) < 0 )
					temp1 = temp1.left;
				else if (key.compareTo(temp1.key) > 0 ) 
					temp1 = temp1.right;
		}
		
		if (flag )
			return true;
		else {
		current =temp2;
		return false;}
	}

	// Return the number of keys one needs to compare to in order to find key.break ;
	public int nbKeyComp(K key) {
		BSTNode<K,T> temp1 = root;	
		int count = 0;
		if (!empty()) {
		   while (temp1 != null ) {
			if (temp1.key == key) {
				count++;
				break;
			}
			else
				if (key.compareTo(temp1.key)<0) { 
					temp1 = temp1.left;
					count++;  
					}
				else {
					temp1 = temp1.right;
		           count++;
		           }
		   }
		}
	return count;}
	// Insert a new element if does not exist and return true.
	//The current points to the new element. If the element already exists, 
	//current does not change and false is returned. This method must be O(log(n)) in average.
	@Override
	public boolean insert(K key,T data) {
		BSTNode<K,T> p, q = current;
		if (find(key)) { //if already in there 
			current = q ;
		return false;
		}
		BSTNode<K,T> temp1 = root;    // start looking for a parent 	
		   while (temp1 != null ) {
				if (key.compareTo(temp1.key) < 0) { 
					if (temp1.left != null)
						temp1 = temp1.left;
					else 
						break;}
				else if (key.compareTo(temp1.key) > 0) {
					 if (temp1.right != null)
						temp1 = temp1.right;
					else 
						break;}
		           }
		   current = temp1;  // found parent 
		p = new BSTNode<K,T>(key , data); // new node
		if (empty()) {// no nodes 
			root = p;}
		else {
			if (key.compareTo(current.key) < 0) // Lefty
				current.left = p;
			else 
				if (key.compareTo(current.key) > 0)// Righty
				current.right = p;}
		current = p;
		return true;
	}

	@Override
	public boolean remove(K k) {
		 // Search for k
		 K k1 = k;
		 BSTNode<K,T> p = root;
		 BSTNode<K,T> q = null; // Parent of p
		 while (p != null) {
		 if (k1.compareTo( p.key) < 0){
		 q =p;
		 p = p.left;
		 } else if (k1.compareTo(p.key)>0) {
		 q = p;
		 p = p.right;
		 } 
		 else { // Found the key
			 // Check the three cases
			 if ((p.left != null) && (p.right != null)) {
			// Case 3: two children
			 // Search for the min in the right subtree
			 BSTNode<K,T> min = p.right;
			 q = p;
			 while (min.left != null) {
			 q = min;
			min = min.left;
			 }
			 p.key = min.key;
			 p.data = min.data;
			 k1 = min.key;
			 p = min;
			 // Now fall back to either case 1 or 2
			 }
			// The subtree rooted at p will change here
			 if (p.left != null) { // One child
			 p = p.left;
			 } else { // One or no children
			 p = p.right;
			 }
			 if (q == null) { // No parent for p, root must change
			 root = p;
			 } else {
			 if (k1.compareTo(q.key) < 0 ) {
			 q.left = p;
			 } else {
			 q.right = p;
			 }
			 }
			 current = root;
			 return true;
			 }
			 }
			 return false; // Not found
			 }
			
	@Override
	public List<Pair<K, T>> getAll() {
		// Return all data in the map in increasing order of the keys.
		LinkedList<Pair<K,T>> l = new LinkedList<Pair<K,T>>();
		BSTNode<K,T> cur = root;
		 recGetAll( l , cur);
		return l;
	}
	private void recGetAll(List<Pair<K, T>> l , BSTNode<K,T> cur) {
		if (cur == null)
		 return;
		if (cur.left !=  null) // goes left
		recGetAll( l , cur.left);
		l.insert( new Pair<K,T>(cur.key, cur.data));
		if (cur.right !=  null) // goes right
			recGetAll( l , cur.right);
		}

	// Return all elements of the map with key k such that k1 <= k <= k2 in increasing order of the keys
	@Override
	public	List<Pair<K, T>> getRange(K k1, K k2) {
		List<Pair<K,T>> l = new LinkedList<Pair<K,T>>();
		BSTNode<K,T> cur = root;
		recGetRange(l,k1,k2 , cur);
		return l;
	}
    private void recGetRange(List<Pair<K, T>> l , K k1 , K k2 , BSTNode<K,T> cur) {
    	if (cur == null)
   		 return;
   		if (cur.left !=  null) // goes left
   		recGetRange( l ,k1,k2 ,cur.left);
   		
   		if (k1.compareTo(cur.key) <=  0 && k2.compareTo(cur.key) >=  0 ) // check if in range
   		l.insert( new Pair<K,T>(cur.key, cur.data));
   		
   		if (cur.right !=  null) // goes right
   			recGetRange( l ,k1,k2 ,cur.right);
    }
	
    // Return the number of keys one needs to compare to in order to find all keys in the range [k1, k2].
	@Override
	public int nbKeyComp(K k1, K k2) {
	if (empty())
		return 0;
	if (k1.compareTo(k2) > 0)
		return 1;
	return  nbKeyCompRec(root ,  k1  ,k2 );
		
	}
/*
	private int nbKeyCompRec(BSTNode<K,T> cur , K k1 , K k2 ) {
		int x =0, x2=0 , x3= 0;

		if (cur == null)
			return x+x2+x3;
		
		if (k1.compareTo(cur.key) <  0) // goes left
		x += nbKeyCompRec( cur.left , k1, k2 );
		
		
		
	   	if (k2.compareTo(cur.key) >  0 ) // goes right
	   		x2 += nbKeyCompRec( cur.right,k1,k2);
	   	x3++;
	   	
     return x+x2+x3;}*/
	
	private int nbKeyCompRec(BSTNode<K,T> cur , K k1 , K k2 ) {
	int count=0;
	if (cur == null)
		return 0;
	
	boolean flag1 = k1.compareTo(cur.key)<0;
	if(flag1) {
		count += nbKeyCompRec(cur.left , k1 , k2 );}
	 
	boolean flag2 = k2.compareTo(cur.key)>0;
	if(flag2) {
		 count+= nbKeyCompRec(cur.right , k1 , k2);
	}
	count= count+1;
	
	return count;}
	
public static void main (String [] args) {
	BST <Integer,Integer> b = new BST <Integer,Integer>();
	boolean flag [] = new boolean[11];
	flag [0] = b.insert(35, -1);
	flag [1] = b.insert(14, -1);
	flag [2] = b.insert(53, -1);
	flag [3] = b.insert(33, -1);
	flag [4] = b.insert(05, -1);
	flag [5] = b.insert(50, -1);
	flag [6] = b.insert(58, -1);
	flag [7] = b.insert(44, -1);
	flag [8] = b.insert(40, -1);
	flag [9] = b.insert(55, -1);
	flag [10] = b.insert(56, -1);
	System.out.println( b.nbKeyComp(33,53)); 
	System.out.println( b.nbKeyComp(100));
	for (int i  = 0 ; i <flag.length ; i++)
		System.out.print(flag[i]+" ");
	
	
	
}
}
