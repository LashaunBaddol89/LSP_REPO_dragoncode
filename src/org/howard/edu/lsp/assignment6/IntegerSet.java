package org.howard.edu.lsp.assignment6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A mutable mathematical set of integers backed by an ArrayList.
 * <p>
 * No duplicates are stored. All mutators modify this instance (not returning new objects).
 * Methods {@code equals(Object)} and {@code toString()} override {@link Object}.
 */
public class IntegerSet  {
  /** Internal storage (duplicates are prevented via logic in add). */
  private List<Integer> set = new ArrayList<>();

  /** Clears the internal representation of the set. */
  public void clear() {
    set.clear();
  }

  /** @return the number of elements in the set. */
  public int length() {
    return set.size();
  }

  /**
   * Two IntegerSets are equal if they contain the same values in any order.
   * Order is ignored; contents are compared.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IntegerSet)) return false;
    IntegerSet other = (IntegerSet) o;
    // same size and mutual containment
    return this.set.size() == other.set.size()
        && this.set.containsAll(other.set)
        && other.set.containsAll(this.set);
  }

  /** @return true if the set contains value, false otherwise. */
  public boolean contains(int value) {
    return set.contains(value);
  }

  /**
   * @return largest element in the set
   * @throws IllegalStateException if the set is empty
   */
  public int largest()  {
    if (set.isEmpty()) throw new IllegalStateException("largest() on empty set");
    int max = set.get(0);
    for (int v : set) if (v > max) max = v;
    return max;
  }

  /**
   * @return smallest element in the set
   * @throws IllegalStateException if the set is empty
   */
  public int smallest()  {
    if (set.isEmpty()) throw new IllegalStateException("smallest() on empty set");
    int min = set.get(0);
    for (int v : set) if (v < min) min = v;
    return min;
  }

  /** Adds an item to the set or does nothing if already present. */
  public void add(int item) {
    if (!set.contains(item)) {
      set.add(item);
    }
  }

  /** Removes an item from the set or does nothing if not present. */
  public void remove(int item) {
    // remove(Object) removes the first occurrence; we never store duplicates
    set.remove(Integer.valueOf(item));
  }

  /**
   * Set union: modifies this set to contain all unique elements in either set.
   * @param other another IntegerSet
   */
  public void union(IntegerSet other) {
    for (int v : other.set) {
      if (!this.set.contains(v)) {
        this.set.add(v);
      }
    }
  }

  /**
   * Set intersection: keeps only elements present in both sets (modifies this).
   * @param other another IntegerSet
   */
  public void intersect(IntegerSet other) {
    // retainAll will keep only elements that are also in other.set
    this.set.retainAll(other.set);
  }

  /**
   * Set difference (this \ other): remove all elements that are found in other (modifies this).
   * @param other another IntegerSet
   */
  public void diff(IntegerSet other) {
    this.set.removeAll(other.set);
  }

  /**
   * Set complement: modifies this to become (other \ this).
   * @param other another IntegerSet
   */
  public void complement(IntegerSet other) {
    List<Integer> result = new ArrayList<>(other.set);
    result.removeAll(this.set);
    this.set = result;
  }

  /** @return true if the set has no elements. */
  public boolean isEmpty() {
    return set.isEmpty();
  }

  /**
   * Returns a stable string representation such as "[1, 2, 3]".
   * Elements are shown in ascending order for readability.
   */
  @Override
  public String toString() {
    List<Integer> copy = new ArrayList<>(set);
    Collections.sort(copy);
    return copy.toString();
  }
}
