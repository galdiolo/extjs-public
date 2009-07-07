/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;

public class FastSet extends AbstractSet<String> {
  private HashSet<String> javaSet;
  private FastMap<String> map;
  private static final String PRESENT = "";

  public FastSet() {
    if (GWT.isScript()) {
      map = new FastMap<String>();
    } else {
      javaSet = new HashSet<String>();
    }
  }

  @Override
  public boolean add(String s) {
    if (GWT.isScript()) {
      return map.put(s, PRESENT) == null;
    } else {
      return javaSet.add(s);
    }
  }

  @Override
  public void clear() {
    if (GWT.isScript()) {
      map.clear();
    } else {
      javaSet.clear();
    }
  }

  @Override
  public boolean contains(Object o) {
    if (GWT.isScript()) {
      return map.containsKey(o);
    } else {
      return javaSet.contains(o);
    }
  }

  @Override
  public boolean isEmpty() {
    if (GWT.isScript()) {
      return map.isEmpty();
    } else {
      return javaSet.isEmpty();
    }
  }

  @Override
  public Iterator<String> iterator() {
    if (GWT.isScript()) {
      return map.keySet().iterator();
    } else {
      return javaSet.iterator();
    }
  }

  @Override
  public boolean remove(Object o) {
    if (GWT.isScript()) {
      return map.remove(o) == PRESENT;
    } else {
      return javaSet.remove(o);
    }
  }

  @Override
  public int size() {
    if (GWT.isScript()) {
      return map.size();
    } else {
      return javaSet.size();
    }
  }

}
