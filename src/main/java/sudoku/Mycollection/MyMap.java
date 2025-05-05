package sudoku.Mycollection;

/**
 * A simple implementation of a hash map (key-value store) with basic
 * operations.
 * Supports resizing when the load factor exceeds a certain threshold.
 *
 * Big-O (Worst Case) Analysis:
 * - put(): O(n), n is the number of entries in the table due to collisions
 * (chaining).
 * - get(): O(n) in the worst case when all entries hash to the same index
 * (chaining).
 * - remove(): O(n) in the worst case due to collisions (chaining).
 * - containsKey(): O(n) in the worst case due to chaining.
 * - clear(): O(n), as it needs to clear all entries in the table.
 */
public class MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private float loadFactor;

    /**
     * Entry class to hold key-value pairs.
     */
    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Default constructor with default capacity and load factor.
     */
    public MyMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructor with a specified initial capacity.
     */
    public MyMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructor with specified initial capacity and load factor.
     *
     * @param initialCapacity the initial capacity of the map.
     * @param loadFactor      the load factor, determining when to resize.
     * @throws IllegalArgumentException if initialCapacity or loadFactor is invalid.
     */
    @SuppressWarnings("unchecked")
    public MyMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }

        this.table = (Entry<K, V>[]) new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    /**
     * Puts a key-value pair into the map.
     * 
     * @param key   the key.
     * @param value the value.
     * @return the previous value associated with the key, or null if no previous
     *         value.
     */
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        // Check if key already exists
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        // Add new entry
        addEntry(key, value, index);
        return null;
    }

    /**
     * Gets the value associated with a key.
     * 
     * @param key the key.
     * @return the value associated with the key, or null if not found.
     */
    public V get(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }

        return null;
    }

    /**
     * Removes the key-value pair associated with the key.
     * 
     * @param key the key.
     * @return the value associated with the key, or null if the key was not found.
     */
    public V remove(K key) {
        if (key == null) {
            return removeForNullKey();
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Entry<K, V> prev = table[index];
        Entry<K, V> e = prev;

        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.key.equals(key)) {
                if (prev == e) {
                    table[index] = next;
                } else {
                    prev.next = next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    /**
     * Checks if the map contains a key.
     * 
     * @param key the key.
     * @return true if the map contains the key, false otherwise.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the number of key-value pairs in the map.
     * 
     * @return the size of the map.
     */
    public int size() {
        return size;
    }

    /**
     * Returns whether the map is empty.
     * 
     * @return true if the map is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all entries from the map.
     */
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Helper methods

    private V putForNullKey(V value) {
        // Special handling for null key (usually stored at index 0)
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addEntry(null, value, 0);
        return null;
    }

    private V getForNullKey() {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    private V removeForNullKey() {
        Entry<K, V> prev = table[0];
        Entry<K, V> e = prev;

        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.key == null) {
                if (prev == e) {
                    table[0] = next;
                } else {
                    prev.next = next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> e = table[index];
        table[index] = new Entry<>(key, value, e);

        // Check if we need to resize
        if (size++ >= table.length * loadFactor) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Entry<K, V>[] newTable) {
        for (Entry<K, V> e : table) {
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(hash(e.key), newTable.length);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
    }

    private int hash(K key) {
        // Simple hash function - in real implementation you'd want something better
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }
}