import java.util.Iterator;
import java.util.LinkedList;

public class ChainedHash<K,V>{
	
	@SuppressWarnings("hiding")
	private class HashElement<K,V> implements Comparable<HashElement<K,V>>{
		
		K key;
		@SuppressWarnings("unused")
		V value;
		
		@SuppressWarnings("unused")
		public HashElement(K key, V value) {
			// TODO Auto-generated constructor stub
			this.key = key;
			this.value = value;
		}

		@SuppressWarnings({ "unchecked" })
		public int compareTo(HashElement<K, V> o) {
			// TODO Auto-generated method stub
			return (((Comparable<K>)this.key).compareTo(o.key));
		}	
	}
	
	@SuppressWarnings("unused")
	private class IteratorHelper<E> implements Iterator<E>{
		E[] keys;
		int position;
		
		@SuppressWarnings("unchecked")
		public IteratorHelper() {
			// TODO Auto-generated constructor stub
			this.position = 0;
			this.keys = (E[]) new Object[numElements];
			int counter = 0 ;
			for (int i = 0; i < tableSize; i++) {
				LinkedList<HashElement<K,V>> list = hashArray[i];
				
				for(HashElement<K,V> hashE: list) {
					keys[counter++] = (E) hashE.key;
				}
			}		
		}

		@Override
		public boolean hasNext() {
			return position < keys.length;
		}

		@Override
		public E next() {
			if(!hasNext())
				return null;
			return keys[this.position];
		}	
	}
	
	private int tableSize,numElements;
	private double maxLoadFactor;
	private LinkedList<HashElement<K,V>> [] hashArray;
	
	@SuppressWarnings("unchecked")
	public ChainedHash(int tableSize) {
		// TODO Auto-generated constructor stub
		this.tableSize = tableSize;
		this.numElements = 0 ;
		this.maxLoadFactor = 0.75;
		this.hashArray = (LinkedList<HashElement<K,V>> []) new LinkedList[tableSize];
		
		for (int i = 0; i < hashArray.length; i++) {
			hashArray[i] = new LinkedList<HashElement<K,V>>();
		}			
	}
	
	
	public boolean add(K key, V value) {
		HashElement<K,V> element = new HashElement<K,V>(key,value);
		double loadFactor  = (1.0 * this.tableSize) / this.numElements;
		if(loadFactor > this.maxLoadFactor)
			rehash(2* this.tableSize);
		int hashValue = key.hashCode();
		hashValue = hashValue & 0x7FFFFFFF;
		hashValue %= this.tableSize;
		hashArray[hashValue].add(element);
		this.numElements++;
		return true;
	}
	
	public boolean remove(K key) {
		int hashValue = key.hashCode();
		hashValue = hashValue & 0x7FFFFFFF;
		hashValue %= this.tableSize;
		hashArray[hashValue].remove();
		this.numElements--;
		return true;	
	}
	
	@SuppressWarnings("unchecked")
	public V getValue(K key) {
		int hashValue = key.hashCode();
		hashValue = hashValue & 0x7FFFFFFF;
		hashValue %= this.tableSize;
		
		for(HashElement<K,V> h : hashArray[hashValue]) {
			if(((Comparable<K>)key).compareTo(h.key) == 0) {
				return h.value;
			}	
		}
		return null;			
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void rehash(int newSize) {
		LinkedList<HashElement<K,V>> [] newArray = (LinkedList <HashElement<K,V>> []) new LinkedList[newSize];
	
		for(int i = 0 ; i < newSize ; i++) {
			newArray[i] = new LinkedList<HashElement<K,V>>();
		}

	/*	for(K key : this) {
			V value = getValue(key);
			HashElement<K,V> hashE = new HashElement(key,value);
			int hashValue = (key.hashCode() & 0x7FFFFFFF) % this.tableSize;
			newArray[hashValue].add(hashE);
		}*/
		
		this.hashArray = newArray;
		this.tableSize = newSize;
		
		
	}
	
	
	
	
	

}
