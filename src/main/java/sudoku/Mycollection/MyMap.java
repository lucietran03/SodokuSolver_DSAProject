package sudoku.Mycollection;

import java.util.Iterator;

/**
 * A custom implementation of a Map (key-value pair) data structure.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class MyMap<K, V> {

    /**
     * Entry class represents a key-value pair.
     *
     * @param <K> the type of key
     * @param <V> the type of value
     */
    private static class Entry<K, V> {
        K key;
        V value;

        /**
         * Constructs a new key-value entry.
         *
         * @param key the key
         * @param value the value
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private MyLinkedList<Entry<K, V>>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    /**
     * Constructs a new empty map with the default initial capacity.
     */
    public MyMap() {
        buckets = new MyLinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the index of the bucket where the key-value pair will be stored.
     *
     * @param key the key to find the bucket index for
     * @return the bucket index
     */
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length; // O(1)
    }

    /**
     * Puts a key-value pair into the map. If the key already exists, the value is updated.
     *
     * @param key the key to put
     * @param value the value to associate with the key
     */
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new MyLinkedList<>();
        }
        for (Object obj : buckets[index].toArray()) {
            Entry<K, V> entry = (Entry<K, V>) obj;
            if (entry.key.equals(key)) {
                entry.value = value; // Update existing key (O(n) in worst case)
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value)); // Add new key-value pair (O(1) for add)
        size++;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or null if the key does not exist
     */
    public V get(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            for (Object obj : buckets[index].toArray()) {
                Entry<K, V> entry = (Entry<K, V>) obj;
                if (entry.key.equals(key)) {
                    return entry.value; // O(n) in worst case if the bucket is long
                }
            }
        }
        return null; // O(1) for empty bucket
    }

    /**
     * Checks if the map contains the specified key.
     *
     * @param key the key to check for
     * @return true if the map contains the key, false otherwise
     */
    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            for (Object obj : buckets[index].toArray()) {
                Entry<K, V> entry = (Entry<K, V>) obj;
                if (entry.key.equals(key)) {
                    return true; // O(n) in worst case if the bucket is long
                }
            }
        }
        return false; // O(1) for empty bucket
    }

    /**
     * Removes the entry associated with the specified key.
     *
     * @param key the key to remove
     */
    public void remove(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            MyLinkedList<Entry<K, V>> bucket = buckets[index];
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove(); // O(n) in worst case if bucket is long
                    size--; // Decrement size
                    return;
                }
            }
        }
    }

    /**
     * Checks if the map is empty.
     *
     * @return true if the map is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0; // O(1)
    }

    /**
     * Returns a set of all the keys in the map.
     *
     * @return a set of all the keys
     */
    public MySet<K> keySet() {
        MySet<K> keys = new MySet<>();
        for (MyLinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Object obj : bucket.toArray()) {
                    Entry<K, V> entry = (Entry<K, V>) obj;
                    keys.add(entry.key); // O(1) for adding each key
                }
            }
        }
        return keys; // O(n) where n is the number of keys in the map
    }
}
