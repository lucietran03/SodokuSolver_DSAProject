package sudoku.Mycollection;

public class MySet<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    /**
     * Constructs an empty set with the default capacity.
     * 
     * Time Complexity: O(1)
     */
    public MySet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Constructs an empty set with the specified initial capacity.
     * 
     * @param initialCapacity The initial capacity of the set.
     * @throws IllegalArgumentException If the initial capacity is non-positive.
     * 
     * Time Complexity: O(1)
     */
    public MySet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Adds the specified element to the set if it is not already present.
     * 
     * @param e The element to add.
     * @return {@code true} if the element was added, {@code false} if it was
     *         already present.
     * 
     * Time Complexity: O(n) in the worst case (due to the contains() check).
     */
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    /**
     * Removes the specified element from the set.
     * 
     * @param o The element to remove.
     * @return {@code true} if the element was removed, {@code false} if it was not
     *         found.
     * 
     * Time Complexity: O(n) in the worst case (due to linear search and shifting elements).
     */
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (safeEquals(o, elements[i])) {
                removeAtIndex(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the set contains the specified element.
     * 
     * @param o The element to check for.
     * @return {@code true} if the element is present, {@code false} otherwise.
     * 
     * Time Complexity: O(n) in the worst case (due to linear search).
     */
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (safeEquals(o, elements[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of elements in the set.
     * 
     * @return The size of the set.
     * 
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the set is empty.
     * 
     * @return {@code true} if the set is empty, {@code false} otherwise.
     * 
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the set.
     * 
     * Time Complexity: O(n) in the worst case (due to clearing all elements).
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Ensures that the set has enough capacity to store additional elements.
     * If the set is full, it doubles the capacity.
     * 
     * Time Complexity: O(n) in the worst case (due to copying elements to a new array).
     */
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

    /**
     * Removes the element at the specified index and shifts remaining elements
     * left.
     * 
     * @param index The index of the element to remove.
     * 
     * Time Complexity: O(n) in the worst case (due to shifting elements).
     */
    private void removeAtIndex(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    /**
     * Safely compares two objects for equality, accounting for null values.
     * 
     * @param a The first object.
     * @param b The second object.
     * @return {@code true} if the objects are equal or both null, {@code false}
     *         otherwise.
     * 
     * Time Complexity: O(1)
     */
    private boolean safeEquals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }
}