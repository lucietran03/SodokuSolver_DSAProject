package sudoku.Mycollection;

import java.util.Iterator;

/**
 * A custom implementation of a Set data structure using hash buckets.
 *
 * @param <E> the type of elements maintained by this set
 */
public class MySet<E> implements Iterable<E> {

    private MyLinkedList<E>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    /**
     * Constructs a new empty set with the default initial capacity.
     */
    public MySet() {
        buckets = new MyLinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the index of the bucket where the element will be stored.
     *
     * @param element the element to find the bucket index for
     * @return the bucket index
     */
    private int getBucketIndex(E element) {
        return Math.abs(element.hashCode()) % buckets.length; // O(1)
    }

    /**
     * Adds the specified element to the set if it is not already present.
     *
     * @param element the element to add
     */
    public void add(E element) {
        int index = getBucketIndex(element);
        if (buckets[index] == null) {
            buckets[index] = new MyLinkedList<>();
        }
        if (!buckets[index].contains(element)) {
            buckets[index].add(element); // O(n) in worst case for checking and adding element
            size++;
        }
    }

    /**
     * Removes the specified element from the set.
     *
     * @param element the element to remove
     */
    public void remove(E element) {
        int index = getBucketIndex(element);
        if (buckets[index] != null) {
            if (buckets[index].remove(element)) { // O(n) in worst case for removing element
                size--;
            }
        }
    }

    /**
     * Checks if the set contains the specified element.
     *
     * @param element the element to check
     * @return true if the set contains the element, false otherwise
     */
    public boolean contains(E element) {
        int index = getBucketIndex(element);
        return buckets[index] != null && buckets[index].contains(element); // O(n) in worst case for checking element
    }

    /**
     * Checks if the set is empty.
     *
     * @return true if the set is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0; // O(1)
    }

    /**
     * Converts the set into an array.
     *
     * @return an array containing all elements of the set
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (MyLinkedList<E> bucket : buckets) {
            if (bucket != null) {
                for (Object element : bucket.toArray()) {
                    result[index++] = element;
                }
            }
        }
        return result; // O(n) where n is the total number of elements in the set
    }

    /**
     * Returns the size of the set.
     *
     * @return the number of elements in the set
     */
    public int size() {
        return size; // O(1)
    }

    /**
     * Returns an iterator over the elements in the set.
     *
     * @return an iterator over the elements in the set
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Iterator<E> currentIterator = null;

            @Override
            public boolean hasNext() {
                while (bucketIndex < buckets.length) {
                    if (currentIterator == null || !currentIterator.hasNext()) {
                        if (buckets[bucketIndex] != null) {
                            currentIterator = buckets[bucketIndex].iterator();
                        }
                        bucketIndex++;
                    } else {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public E next() {
                return currentIterator.next(); // O(1) per next call
            }
        };
    }
}
