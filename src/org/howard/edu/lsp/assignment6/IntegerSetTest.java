package org.howard.edu.lsp.assignment6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for IntegerSet.
 * Each public method in IntegerSet has at least one test (typical & edge cases).
 */
public class IntegerSetTest {

  // ---------- basic construction / add / contains / length ----------
  @Test
  public void testAddContainsNoDuplicates() {
    IntegerSet s = new IntegerSet();
    assertTrue(s.isEmpty());

    s.add(3);
    s.add(3); // duplicate ignored
    s.add(1);

    assertEquals(2, s.length());
    assertTrue(s.contains(3));
    assertTrue(s.contains(1));
    assertFalse(s.contains(7));
  }

  @Test
  public void testClearAndIsEmpty() {
    IntegerSet s = new IntegerSet();
    s.add(10);
    s.add(20);
    assertFalse(s.isEmpty());

    s.clear();
    assertTrue(s.isEmpty());
    assertEquals(0, s.length());
  }

  // ---------- largest / smallest ----------
  @Test
  public void testLargestSmallestTypical() {
    IntegerSet s = new IntegerSet();
    s.add(4);
    s.add(-2);
    s.add(15);
    s.add(7);

    assertEquals(15, s.largest());
    assertEquals(-2, s.smallest());
  }

  @Test
  public void testLargestThrowsOnEmpty() {
    IntegerSet s = new IntegerSet();
    assertThrows(IllegalStateException.class, s::largest);
  }

  @Test
  public void testSmallestThrowsOnEmpty() {
    IntegerSet s = new IntegerSet();
    assertThrows(IllegalStateException.class, s::smallest);
  }

  // ---------- equals ----------
  @Test
  public void testEqualsOrderInsensitive() {
    IntegerSet a = new IntegerSet();
    a.add(1); a.add(2); a.add(3);

    IntegerSet b = new IntegerSet();
    b.add(3); b.add(2); b.add(1);

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    b.remove(1);
    assertFalse(a.equals(b));
  }

  // ---------- remove ----------
  @Test
  public void testRemove() {
    IntegerSet s = new IntegerSet();
    s.add(5); s.add(6);
    s.remove(6);
    s.remove(99); // not present: no effect
    assertEquals(1, s.length());
    assertFalse(s.contains(6));
    assertTrue(s.contains(5));
  }

  // ---------- union / intersect / diff / complement ----------
  @Test
  public void testUnion() {
    IntegerSet s1 = new IntegerSet();
    s1.add(1); s1.add(2);

    IntegerSet s2 = new IntegerSet();
    s2.add(2); s2.add(3);

    s1.union(s2); // modifies s1
    assertTrue(s1.contains(1));
    assertTrue(s1.contains(2));
    assertTrue(s1.contains(3));
    assertEquals(3, s1.length());
  }

  @Test
  public void testIntersect() {
    IntegerSet s1 = new IntegerSet();
    s1.add(1); s1.add(2); s1.add(3);

    IntegerSet s2 = new IntegerSet();
    s2.add(2); s2.add(4);

    s1.intersect(s2); // modifies s1 to keep only common
    assertEquals(1, s1.length());
    assertTrue(s1.contains(2));
    assertFalse(s1.contains(1));
    assertFalse(s1.contains(3));
  }

  @Test
  public void testDiff() {
    IntegerSet s1 = new IntegerSet();
    s1.add(1); s1.add(2); s1.add(3);

    IntegerSet s2 = new IntegerSet();
    s2.add(2); s2.add(5);

    s1.diff(s2); // remove anything found in s2
    assertEquals(2, s1.length());
    assertTrue(s1.contains(1));
    assertTrue(s1.contains(3));
    assertFalse(s1.contains(2));
  }

  @Test
  public void testComplement() {
    IntegerSet s1 = new IntegerSet();
    s1.add(1); s1.add(2);

    IntegerSet s2 = new IntegerSet();
    s2.add(1); s2.add(3); s2.add(4);

    s1.complement(s2); // s1 becomes (s2 \ s1) = {3,4}
    assertEquals(2, s1.length());
    assertTrue(s1.contains(3));
    assertTrue(s1.contains(4));
    assertFalse(s1.contains(1));
    assertFalse(s1.contains(2));
  }

  // ---------- toString ----------
  @Test
  public void testToStringSortedFormat() {
    IntegerSet s = new IntegerSet();
    s.add(3); s.add(1); s.add(2);
    assertEquals("[1, 2, 3]", s.toString());
  }
}
