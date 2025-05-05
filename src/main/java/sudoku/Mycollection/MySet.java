package sudoku.Mycollection;

public class MySet<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MySet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MySet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // Basic operations

    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (safeEquals(o, elements[i])) {
                removeAtIndex(i);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (safeEquals(o, elements[i])) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Helper methods

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    private void removeAtIndex(int index) {
        // Shift all elements after the index left by one
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // Clear last element and decrement size
    }

    private boolean safeEquals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }
}