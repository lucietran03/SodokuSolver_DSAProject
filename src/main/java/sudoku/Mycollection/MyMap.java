package sudoku.Mycollection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyMap<K, V> implements Iterable<MyMap.Entry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;

    public MyMap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MyMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.table = (Entry<K, V>[]) new Entry[initialCapacity];
        this.size = 0;
    }

    public static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        addEntry(key, value, index);
        return null;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
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

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
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

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public MySet<K> keySet() {
        MySet<K> set = new MySet<>();
        for (Entry<K, V> entry : this) {
            set.add(entry.key);
        }
        return set;
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> e = table[index];
        table[index] = new Entry<>(key, value, e);
        if (size++ >= table.length * LOAD_FACTOR) {
            resize(2 * table.length);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
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
        return key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            private int currentIndex = 0;
            private Entry<K, V> currentEntry = null;
            private Entry<K, V> nextEntry = null;

            {
                findNext();
            }

            private void findNext() {
                while (currentIndex < table.length && (nextEntry = table[currentIndex]) == null) {
                    currentIndex++;
                }
                if (nextEntry == null) {
                    while (currentIndex < table.length) {
                        if (table[currentIndex] != null) {
                            nextEntry = table[currentIndex];
                            break;
                        }
                        currentIndex++;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return nextEntry != null;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                currentEntry = nextEntry;
                nextEntry = nextEntry.next;

                if (nextEntry == null) {
                    currentIndex++;
                    findNext();
                }

                return currentEntry;
            }

            @Override
            public void remove() {
                if (currentEntry == null) {
                    throw new IllegalStateException();
                }

                K key = currentEntry.key;
                MyMap.this.remove(key);
                currentEntry = null;
            }
        };
    }
}