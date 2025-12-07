package com.davidhagar.util;

import com.david_hagar.util.IndexStorage;
import com.david_hagar.util.UpdatablePriorityQueue;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


public class TestUpdatablePriorityQueue {

    static class Container implements IndexStorage {

        int index;
        int value;

        public Container(int value) {
            this.value = value;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Container{" +
                    "index=" + index +
                    ", value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Container)) return false;
            Container container = (Container) o;
            return value == container.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    @Test
    public void testHeap() {

        int n = 1000;
        ArrayList<Container> list = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            list.add(new Container(i));
        }

        Collections.shuffle(list);

        final Comparator<Container> comparator = new Comparator<>() {
            @Override
            public int compare(Container o1, Container o2) {
                return Integer.compare(o1.value, o2.value);
            }
        };
        UpdatablePriorityQueue<Container> q = new UpdatablePriorityQueue<>(comparator);
        for (Container c : list) {
            q.add(c);
        }

        Random r = new Random();

        for (Container c : list) {
            c.value += r.nextInt(n*2) -n;
            q.update(c);
        }
        for (int i = 0; i < n/4; i++) {
            final Container container = new Container(i);
            list.add(container);
            q.add(container);
        }

        list.sort(comparator);

        for (Container c : list) {
            assertEquals(c, q.poll());
        }
        assertTrue(q.isEmpty());

//todo: add remove test


    }


}
