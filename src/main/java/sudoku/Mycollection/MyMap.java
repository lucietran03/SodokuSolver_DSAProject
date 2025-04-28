package sudoku.Mycollection;

import java.util.Iterator;

public class MyMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private MyLinkedList<Entry<K, V>>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyMap() {
        buckets = new MyLinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new MyLinkedList<>();
        }
        for (Object obj : buckets[index].toArray()) {
            Entry<K, V> entry = (Entry<K, V>) obj;
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            for (Object obj : buckets[index].toArray()) {
                Entry<K, V> entry = (Entry<K, V>) obj;
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            for (Object obj : buckets[index].toArray()) {
                Entry<K, V> entry = (Entry<K, V>) obj;
                if (entry.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void remove(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
            MyLinkedList<Entry<K, V>> bucket = buckets[index];
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove(); // Remove the entry using the iterator
                    size--; // Decrement the size
                    return;
                }
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // New keySet method
    public MySet<K> keySet() {
        MySet<K> keys = new MySet<>();
        for (MyLinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Object obj : bucket.toArray()) {
                    Entry<K, V> entry = (Entry<K, V>) obj;
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }
}