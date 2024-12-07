package cs2110;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.  By constructing strings with
    // `new`, more likely to catch inadvertent use of `==` instead of `.equals()`.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @Test
    void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @Test
    void testPrependSize() {
        // List creation helper functions use prepend.
        Seq<String> list;

        list = makeList1();
        assertEquals(1, list.size());

        list = makeList2();
        assertEquals(2, list.size());

        list = makeList3();
        assertEquals(3, list.size());
    }

    @Test
    void testToString() {
        Seq<String> list;

        list = makeList0();
        assertEquals("[]", list.toString());

        list = makeList1();
        assertEquals("[A]", list.toString());

        list = makeList2();
        assertEquals("[A, B]", list.toString());

        list = makeList3();
        assertEquals("[A, B, C]", list.toString());
    }


    @Test
    void testContains()
    {
        // Test 1: check for `elem` in a list that contains `elem` once.
        Seq<String> list;
        list = makeList3();
        assertEquals(true, list.contains("A"));

        // Test 2: check for `elem` in a list that contains `elem` more than once.
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("A"));
        ans.prepend(new String("A"));
        assertEquals(true, ans.contains("A"));

        // Test 3: check for `elem` in a list that does not contain `elem`.
        list = makeList0();
        assertEquals(false, list.contains("A"));

    }

    @Test
    void testGet()
    {
        Seq<String> list;
        list = makeList3();
        // Test 1: get 'elem' from index 1.
        assertEquals("B", list.get(1));
        // Test 2: get 'elem' from index 2.
        assertEquals("C", list.get(2));
        // Test 3: get 'elem' from index 3.
        assertEquals("A", list.get(0));

    }

    @Test
    void testAppend()
    {
        // Test 1: append 3 'elem's to list (size = 3)
        Seq<String> ans = new LinkedSeq<>();
        ans.append(new String("B"));
        ans.append(new String("A"));
        ans.append(new String("C"));
        assertEquals(3, ans.size());
        assertEquals("[B, A, C]", ans.toString());

        // Test 2: append 'elem' to an empty list
        Seq<String> ansOne = new LinkedSeq<>();
        ansOne.append(new String(""));
        assertEquals(1, ansOne.size());
        assertEquals("[]", ansOne.toString());

        // Test 3: append 5 'elem's to list (size = 5)
        Seq<String> ansTwo = new LinkedSeq<>();
        ansTwo.append(new String("H"));
        ansTwo.append(new String("e"));
        ansTwo.append(new String("l"));
        ansTwo.append(new String("l"));
        ansTwo.append(new String("o"));
        assertEquals(5, ansTwo.size());
        assertEquals("[H, e, l, l, o]", ansTwo.toString());
    }

    @Test
    void testInsertBefore()
    {

        Seq<String> list;
        list = makeList3();
        assertEquals("[A, B, C]", list.toString());

        // Test 1: insert 'elem' when successor is in the middle
        list.insertBefore("J", "B");
        assertEquals("[A, J, B, C]", list.toString());
        assertEquals(4, list.size());

        // Test 2: insert 'elem' when successor is the head
        list.insertBefore("H","A");
        assertEquals("[H, A, J, B, C]", list.toString());
        assertEquals(5, list.size());

        // Test 3: insert 'elem' when successor is the tail
        list.insertBefore("D", "C");
        assertEquals("[H, A, J, B, D, C]", list.toString());
        assertEquals(6, list.size());

    }

    @Test
    void testRemove() {
        Seq<String> ans = new LinkedSeq<>();
        ans.append(new String("AAA"));
        ans.append(new String("BBB"));
        ans.append(new String("CCC"));
        ans.append(new String("DDD"));
        ans.append(new String("EEE"));
        ans.append(new String("EEE"));

        // Test 1: Remove `elem` from a list that does not contain `elem`
        assertEquals(false, ans.remove("ABC"));
        assertEquals("[AAA, BBB, CCC, DDD, EEE, EEE]", ans.toString());

        // Test 2: Remove `elem` from a list that contains `elem` and is the head
        assertEquals(true, ans.remove("AAA"));
        assertEquals("[BBB, CCC, DDD, EEE, EEE]", ans.toString());

        // Test 3: Remove `elem` from a list that contains `elem` and is the head
        assertEquals(true, ans.remove("DDD"));
        assertEquals("[BBB, CCC, EEE, EEE]", ans.toString());

        // Test 4: Remove `elem` from a list that contains `elem` twice
        assertEquals(true, ans.remove("EEE"));
        assertEquals("[BBB, CCC, EEE]", ans.toString());

        // Test 5: Remove `elem` from a list that contains `elem` and 'elem' is the tail
        assertEquals(true, ans.remove("EEE"));
        assertEquals("[BBB, CCC]", ans.toString());
    }

    @Test
    void testEquals() {
        Seq<String> ans = new LinkedSeq<>();
        ans.append(new String("AAA"));
        ans.append(new String("BBB"));
        ans.append(new String("CCC"));

        Seq<String> ans1 = new LinkedSeq<>();
        ans1.append(new String("AAA"));
        ans1.append(new String("BBB"));
        ans1.append(new String("CCC"));

        Seq<String> ans2 = new LinkedSeq<>();
        ans2.append(new String("AAA"));
        ans2.append(new String("BBB"));

        Seq<String> ans3 = new LinkedSeq<>();
        ans3.append(new String("AAA"));
        ans3.append(new String("BBB"));
        ans3.append(new String("XYZ"));

        Seq<String> ans4 = new LinkedSeq<>();
        ans4.append(new String("AAA"));
        ans4.append(new String("BBB"));
        ans4.append(new String("CCC"));

        // Test 1: Equal
        assertEquals(true, ans.equals(ans1));

        // Test 2: Size different, Not Equal
        assertEquals(false, ans.equals(ans2));

        // Test 3: Object different, Not Equal
        String str = "AAA BBB";
        assertEquals(false, ans.equals(str));

        // Test 4: Same object, same size, different nodes, Not Equal
        assertEquals(false, ans.equals(ans3));

        // Test 5: Same object, same size, same nodes, Equal
        assertEquals(true, ans.equals(ans4));
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered `hashCode()` or `assertThrows()` in class.
     */

    @Test
    void testHashCode() {
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @Test
    void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}
