package sudoku.Mycollection;

import java.util.Iterator;

/**
 * A custom implementation of a singly linked list.
 *
 * @param <E> the type of elements in this list
 */
public class MyLinkedList<E> implements Iterable<E> {

    /**
     * Node class represents an element in the linked list.
     *
     * @param <E> the type of element stored in the node
     */
    private static class Node<E> {
        E data;
        Node<E> next;

        /**
         * Constructor for creating a new node.
         *
         * @param data the element to store in the node
         */
        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<E> head;
    private int size;

    /**
     * Constructs an empty linked list.
     */
    public MyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param element the element to add
     */
    public void add(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = newNode; // O(1) for empty list
        } else {
            Node<E> current = head;
            // Traverse the list to find the last node (O(n) for the worst case)
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode; // O(1)
        }
        size++;
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     *
     * @param element the element to remove
     * @return true if the element was removed, false if it was not found
     */
    public boolean remove(E element) {
        if (head == null) return false; // O(1) for empty list

        // If the element is at the head of the list (O(1))
        if (head.data.equals(element)) {
            head = head.next;
            size--;
            return true;
        }

        Node<E> current = head;
        // Traverse the list to find the element (O(n) in the worst case)
        while (current.next != null && !current.next.data.equals(element)) {
            current = current.next;
        }

        // If element is found (O(1))
        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }

        return false; // O(1) when the element is not found
    }

    /**
     * Checks if the list contains the specified element.
     *
     * @param element the element to check for
     * @return true if the list contains the element, false otherwise
     */
    public boolean contains(E element) {
        Node<E> current = head;
        // Traverse the list to find the element (O(n) in the worst case)
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false; // O(n) for worst case
    }

    /**
     * Returns the size of the list.
     *
     * @return the number of elements in the list
     */
    public int size() {
        return size; // O(1)
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0; // O(1)
    }

    /**
     * Clears the list, removing all elements.
     */
    public void clear() {
        head = null; // O(1)
        size = 0;    // O(1)
    }

    /**
     * Converts the list to an array.
     *
     * @return an array containing all elements in the list
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = head;
        int index = 0;
        // Traverse the list and add elements to the array (O(n) in worst case)
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array; // O(n)
    }

    /**
     * Returns an iterator for the list.
     *
     * @return an iterator for the list
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            /**
             * Returns true if there are more elements to iterate over.
             *
             * @return true if there are more elements
             */
            @Override
            public boolean hasNext() {
                return current != null; // O(1)
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element
             */
            @Override
            public E next() {
                E data = current.data;
                current = current.next; // O(1)
                return data;
            }
        };
    }
}
