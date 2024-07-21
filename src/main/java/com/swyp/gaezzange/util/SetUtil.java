package com.swyp.gaezzange.util;

import java.util.Set;

public class SetUtil {

  public static <T> boolean hasIntersection(Set<T> set1, Set<T> set2) {
    Set<T> smallerSet = set1.size() < set2.size() ? set1 : set2;
    Set<T> largerSet = set1.size() >= set2.size() ? set1 : set2;

    for (T element : smallerSet) {
      if (largerSet.contains(element)) {
        return true;
      }
    }

    return false;
  }
}