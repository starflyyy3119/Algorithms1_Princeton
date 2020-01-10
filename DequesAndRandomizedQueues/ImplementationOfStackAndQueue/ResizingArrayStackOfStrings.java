import java.util.Iterator;

public class ResizingArrayStackOfStrings {

    private String[] s;
    private int N = 0;

    public ResizingArrayStackOfStrings() {
        s = new String[1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void push(String item) {
        if (N == s.length) resize(2 * s.length);
        s[N++] = item;
    }

    public String pop() {
        String item = s[--N];
        s[N] = null;
        // why halve when array is one-quarter full?
        // consider the push-pop-push-pop sequence, will double and halve the array continuously, too expensive
        if(N > 0 && N == s.length / 4) resize(s.length / 2);
        return item;
    }

    public void resize(int capacity) {
        String[] copy = new String[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }
    
    public Iterator<Item> iterator() { return new ReverseArrayIterator(); }
    
    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;
        
        public boolean hasNext() { return i > 0;   }
        public void remove()     {/*not supported*/}
        public Item next()       {return s[--i];   }
    }
}
