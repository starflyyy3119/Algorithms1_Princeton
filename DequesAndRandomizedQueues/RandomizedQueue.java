import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] array;
    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        array = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == array.length) resize(array.length * 2);
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int rn = StdRandom.uniform(0, size);
        Item temp = array[rn];
        array[rn] = array[--size];
        array[size] = null;
        if (size > 0 && size == array.length / 4) resize(array.length / 2);
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int rn = StdRandom.uniform(0, size);
        return array[rn];
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {             // 这里的条件是小于size，因为在减半的时候如果还用array.length，newArray会越界
            newArray[i] = array[i];
        }
        array = newArray;   // *这样引用array 和 newArray指向同一个数组*，通过一个引用改变值另一个也会变
    }

    // return and independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ResizingRandomizedQueueIterator();
    }

    private class ResizingRandomizedQueueIterator implements Iterator<Item> {

        int sizeIterator = size;
        Item[] copy = (Item[]) new Object[array.length];  // 通过新建数组并如下方式赋值，才是创建出新的
        private ResizingRandomizedQueueIterator() {
            for (int i = 0; i < array.length; i++) {
                copy[i] = array[i];
            }
        }
        public boolean hasNext() {
            return sizeIterator > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
//            StdOut.println("sizeIterator = " + sizeIterator);
//            StdOut.println("array = " + array[0] + array[1] + array[2]);
//            StdOut.println("copy = "+ copy[0] + copy[1] + copy[2]);
            int rn = StdRandom.uniform(0, sizeIterator);
//            StdOut.println("rn = " + rn);
            Item temp = copy[rn];
            copy[rn] = copy[--sizeIterator];
            return temp;
        }
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(3);
        rq.enqueue(7);
        rq.dequeue();
        rq.dequeue();
        rq.enqueue(8);
        rq.enqueue(9);
        StdOut.println(rq.sample());
        for (Integer integer : rq) {
            StdOut.println(integer);
        }
        StdOut.println("next");
        // rq.enqueue(10);
        for (Integer integer : rq) {
            StdOut.println(integer);
        }


    }
}

// 数组实现对随机队列的困难： 因为剔除后会产生空隙，从而导致内存利用效率低。
// 解决方法： 随机抽取元素后将该元素与队尾元素交换，队尾元素出栈。

// 链表实现中遇到的困难： 一个随机的iterator怎么实现