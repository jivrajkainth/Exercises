package uk.co.rapidware.structures;

import java.util.Arrays;

/**
 */
public class Heap {

    private final Heap.Type type_;
    private int[] elements_;
    private int capacity_;
    private int size_;

    public Heap(final Type type, final int capacity) {
        type_ = type;
        elements_ = new int[capacity];
        capacity_ = capacity;
        size_ = 0;
    }

    public Type getType() {
        return type_;
    }

    public int[] getElements() {
        return elements_;
    }

    public int getCapacity() {
        return capacity_;
    }

    public int getSize() {
        return size_;
    }

    private void setSize(final int size) {
        size_ = size;
    }

    private void incrementSize() {
        setSize(getSize() + 1);
    }

    private void decrementSize() {
        setSize(getSize() - 1);
    }

    public void push(final int element) {
        if (getSize() == getCapacity()) {
            throw new RuntimeException("Heap is at capacity");
        }
        getElements()[getSize()] = element;
        incrementSize();
        siftUp();
    }

    public int peek() {
        int peeked = -1;
        if (getSize() > 0) {
            peeked = getElements()[0];
        }
        return peeked;
    }

    public int pop() {
        if (getSize() == 0) {
            throw new RuntimeException("Heap is empty");
        }
        int poppedValue = getElements()[0];
        getElements()[0] = getElements()[getSize() - 1];
        decrementSize();
        siftDown();
        return poppedValue;
    }

    private void siftDown() {
        int index = 0;
        int leftChildIndex = leftChildIndex(index);
        int rightChildIndex = leftChildIndex + 1;

        boolean siftedDown = true;
        while (leftChildIndex < getSize() && siftedDown) {
            final int elementToSift = getElements()[index];
            int childIndex = chooseIndex(leftChildIndex, rightChildIndex);
            int childElement = getElements()[childIndex];
            if (shouldSiftDown(elementToSift, childElement)) {
                getElements()[index] = childElement;
                getElements()[childIndex] = elementToSift;
                index = childIndex;
                leftChildIndex = leftChildIndex(index);
                rightChildIndex = leftChildIndex + 1;
            } else {
                siftedDown = false;
            }

        }
    }

    private int chooseIndex(final int leftChildIndex, final int rightChildIndex) {
        if (rightChildIndex >= getSize()) {
            return leftChildIndex;
        }

        if (getElements()[leftChildIndex] > getElements()[rightChildIndex]) {
            return getType() == Type.MAX ? leftChildIndex : rightChildIndex;
        } else if (getElements()[rightChildIndex] > getElements()[leftChildIndex]) {
            return getType() == Type.MAX ? rightChildIndex : leftChildIndex;
        }
        return leftChildIndex;
    }

    private void siftUp() {

        // Child 2n+1, 2n+2
        // Parent (n-1) / 2, (n-2) / 2
        int index = getSize() - 1;
        int parentIndex = parentIndex(index);
        boolean siftedUp = true;
        while (parentIndex >= 0 && siftedUp) {
            final int elementToSift = getElements()[index];
            final int parentElement = getElements()[parentIndex];
            if (shouldSiftUp(elementToSift, parentElement)) {
                getElements()[parentIndex] = elementToSift;
                getElements()[index] = parentElement;
                index = parentIndex;
                parentIndex = parentIndex(index);
            } else {
                siftedUp = false;
            }
        }
    }

    private boolean shouldSiftUp(final int elementToSift, final int parentElement) {
        return getType() == Type.MAX
               ? elementToSift > parentElement
               : elementToSift < parentElement;
    }

    private boolean shouldSiftDown(final int elementToSift, final int childElement) {
        return getType() == Type.MAX
               ? elementToSift < childElement
               : elementToSift > childElement;
    }

    private int leftChildIndex(final int index) {
        return (index * 2) + 1;
    }

    private int rightChildIndex(final int index) {
        return (index * 2) + 2;
    }

    private int parentIndex(final int index) {
        return (index & 1) > 0
               ? (index - 1) / 2
               : (index - 2) / 2;
    }

    @Override
    public String toString() {
        return "Heap{" +
               "type_=" + type_ +
               ", elements_=" + Arrays.toString(elements_) +
               ", capacity_=" + capacity_ +
               ", size_=" + size_ +
               '}';
    }

    public enum Type {
        MAX,
        MIN
    }

}
