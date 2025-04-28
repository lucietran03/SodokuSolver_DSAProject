package sudoku.Mycollection;

import java.util.Iterator;

public class MySet<E> implements Iterable<E> {
    private MyLinkedList<E>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MySet() {
        buckets = new MyLinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    private int getBucketIndex(E element) {
        return Math.abs(element.hashCode()) % buckets.length;
    }

    public void add(E element) {
        int index = getBucketIndex(element);
        if (buckets[index] == null) {
            buckets[index] = new MyLinkedList<>();
        }
        if (!buckets[index].contains(element)) {
            buckets[index].add(element);
            size++;
        }
    }

    public void remove(E element) {
        int index = getBucketIndex(element);
        if (buckets[index] != null) {
            if (buckets[index].remove(element)) {
                size--;
            }
        }
    }

    public boolean contains(E element) {
        int index = getBucketIndex(element);
        return buckets[index] != null && buckets[index].contains(element);
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
        return result;
    }

    public int size() {
        return size;
    }

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
                return currentIterator.next();
            }
        };
    }
}