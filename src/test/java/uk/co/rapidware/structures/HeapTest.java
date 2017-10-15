package uk.co.rapidware.structures;

import junit.framework.TestCase;
import org.junit.Test;

/**
 */
public class HeapTest {

    @Test
    public void testInsertsMaxHeap() throws Exception {

        final Heap integerHeap = new Heap(Heap.Type.MAX, 10);

        integerHeap.push(1);
        System.out.println(integerHeap);
        TestCase.assertEquals(1, integerHeap.peek());

        integerHeap.push(2);
        System.out.println(integerHeap);
        TestCase.assertEquals(2, integerHeap.peek());

        integerHeap.push(3);
        System.out.println(integerHeap);
        TestCase.assertEquals(3, integerHeap.peek());

        integerHeap.push(4);
        System.out.println(integerHeap);
        TestCase.assertEquals(4, integerHeap.peek());

        integerHeap.push(5);
        System.out.println(integerHeap);
        TestCase.assertEquals(5, integerHeap.peek());

        integerHeap.push(6);
        System.out.println(integerHeap);
        TestCase.assertEquals(6, integerHeap.peek());

        integerHeap.push(10);
        System.out.println(integerHeap);
        TestCase.assertEquals(10, integerHeap.peek());
    }

    @Test
    public void testInsertsMinHeap() throws Exception {

        final Heap integerHeap = new Heap(Heap.Type.MIN, 10);

        integerHeap.push(10);
        System.out.println(integerHeap);
        TestCase.assertEquals(10, integerHeap.peek());

        integerHeap.push(6);
        System.out.println(integerHeap);
        TestCase.assertEquals(6, integerHeap.peek());

        integerHeap.push(3);
        System.out.println(integerHeap);
        TestCase.assertEquals(3, integerHeap.peek());

        integerHeap.push(4);
        System.out.println(integerHeap);
        TestCase.assertEquals(3, integerHeap.peek());

        integerHeap.push(5);
        System.out.println(integerHeap);
        TestCase.assertEquals(3, integerHeap.peek());

        integerHeap.push(2);
        System.out.println(integerHeap);
        TestCase.assertEquals(2, integerHeap.peek());

        integerHeap.push(1);
        System.out.println(integerHeap);
        TestCase.assertEquals(1, integerHeap.peek());
    }

    @Test
    public void testPopMaxHeap() {
        final Heap integerHeap = new Heap(Heap.Type.MAX, 10);
        integerHeap.push(1);
        integerHeap.push(2);
        integerHeap.push(3);
        integerHeap.push(4);
        integerHeap.push(5);
        integerHeap.push(6);

        System.out.println(integerHeap);

        TestCase.assertEquals(6, integerHeap.pop());
        System.out.println(integerHeap);

        TestCase.assertEquals(5, integerHeap.pop());
        System.out.println(integerHeap);

        TestCase.assertEquals(4, integerHeap.pop());
        System.out.println(integerHeap);

        TestCase.assertEquals(3, integerHeap.pop());
        System.out.println(integerHeap);

        TestCase.assertEquals(2, integerHeap.pop());
        System.out.println(integerHeap);

        TestCase.assertEquals(1, integerHeap.pop());
        System.out.println(integerHeap);
    }
}