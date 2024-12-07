package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Assignment metadata
 */

/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;

            // Check that the number of linked nodes is equal to this list's size and that
            // the last linked node is the same object as `tail`.
            int count = 0;
            Node<T> temp = head;
            while (temp != null) {
                count++;
                temp = temp.next();
            }
            assert (count == size) && (temp == null || temp.equals(tail));
        }
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by ", ".
     * <p>
     * Example: a list containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a list containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        String str = "[";

        // Unit tests have already been provided.
        Node<T> temp = head;
        while (temp != null) {
            if (temp.next() == null) {
                str += temp.data().toString();
            }
            else {
                str += temp.data().toString() + ", ";
            }
            temp = temp.next();
        }
        str += "]";
        return str;
    }

    @Override
    /**
     * Return true if the list contains 'elem' at least once, return false if the list doesn't contain 'elem'.
     */
    public boolean contains(T elem) {
        assertInv();
        assert elem != null;

        Node<T> temp = head;
        while (temp != null) {
            if (temp.data().equals(elem)) {
                assertInv();
                return true;
            }
            temp = temp.next();
        }
        assertInv();
        return false;
    }

    @Override
    /**
     * Return the 'elem' at the given 'index'.
     */
    public T get(int index) {
        assert (index >= 0) && (index < size);
        Node<T> temp = head;
        int count = 0;
        while (temp != null) {
            if (count == index) {
                break;
            }

            temp = temp.next();
            count++;
        }
        assertInv();
        return temp.data();
    }

    @Override
    /**
     * Append the 'elem' at the end of the list.
     */
    public void append(T elem) {
        assert elem != null;
        Node<T> newNode = new Node<>(elem, null);
        if (size == 0) {
            head = newNode;
            tail = newNode;
            size++;
            assertInv();
        } else {
            tail.setNext(newNode);
            tail = newNode;
            size++;
            assertInv();
        }
    }

    /**
     * Insert element `elem` into the list just before the first occurrence of element `successor`.
     * Requires that `successor` is contained in the list and that `elem` and `successor` are not
     * null.
     * <p>
     * Example: If the list is [3, 8, 2], then insertBefore(1, 8) would change the list to [3, 1, 8,
     * 2].
     */
    @Override
    public void insertBefore(T elem, T successor) {
        // Tip: Since there is a precondition that `successor` is in the list, you don't have to
        // handle the case of the empty list.  Asserting this precondition is optional.
        assertInv();
        assert elem != null;
        Node<T> newNode = new Node<>(elem, null);

        // If head is successor, insert before head
        if (head.data().equals(successor)) {
            newNode.setNext(head);
            head = newNode;
            size++;
        }
        else {
            Node<T> temp = head;
            while (temp.next() != null) {
                if (temp.next().data().equals(successor)) {
                    newNode.setNext(temp.next());
                    temp.setNext(newNode);
                    size++;
                    assertInv();
                    return;
                }
                else {
                    temp = temp.next();
                }
            }
        }
    }
    /**
     * Remove the first occurrence of element `elem` (if any) from this list.  Return whether the
     * list changed.  Requires `elem` is not null.
     */
    @Override
    public boolean remove(T elem) {
        assertInv();
        assert elem != null;

        //list does not contain 'elem'
        if (!this.contains(elem))
            return false;
            //if the head is 'elem'
        else if (head.data().equals(elem)) {
            head = head.next();
            size--;
            return true;
        }
        else {
            Node<T> previous = head;

            while (previous.next() != null) {
                if (previous.next().data().equals(elem)) {
                    previous.setNext(previous.next().next());
                    size--;
                    return true;
                }
                else
                    previous = previous.next();
            }
        }
        assertInv();
        return false;
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `LinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        // Note: In the `instanceof` check, we write `LinkedSeq` instead of `LinkedSeq<T>` because
        // of a limitation inherent in Java generics: it is not possible to check at run-time
        // what the specific type `T` is.  So instead we check a weaker property, namely,
        // that `other` is some (unknown) instantiation of `LinkedSeq`.  As a result, the static
        // type returned by `currNodeOther.data()` is `Object`.
        if (!(other instanceof LinkedSeq)) {
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node currNodeOther = otherSeq.head;

        if (this.size != otherSeq.size) {
            return false;
        }

        while (currNodeThis != null) {
            if (currNodeThis.data().equals(currNodeOther.data())) {
                currNodeThis = currNodeThis.next();
                currNodeOther = currNodeOther.next();
            }
            else {
                return false;
            }
        }

        return true;
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered the implementation of these concepts in class.
     */

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.  Requires that the list not be mutated while the iterator is in use.
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();

        // Return an instance of an anonymous inner class implementing the Iterator interface.
        // For convenience, this uses Java features that have not eyt been introduced in the course.
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
