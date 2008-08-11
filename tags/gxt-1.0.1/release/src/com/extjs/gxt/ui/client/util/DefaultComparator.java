/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.Comparator;
import java.util.Date;

/**
 * Default <code>Comparator</code> implementation.
 */
public class DefaultComparator<X extends Object> implements Comparator<X> {

  public final static DefaultComparator<?> INSTANCE = new DefaultComparator<Object>();
  
  public int compare(Object o1, Object o2) {
    if (o1 instanceof Date) {
      return compareDates((Date) o1, (Date) o2);
    } else if (o1 instanceof Float) {
      return ((Float) o1).compareTo(((Float) o2));
    } else if (o1 instanceof Double) {
      return ((Double) o1).compareTo(((Double) o2));
    } else if (o1 instanceof Short) {
      return ((Short) o1).compareTo(((Short) o2));
    } else if (o1 instanceof Integer) {
      return ((Integer) o1).compareTo(((Integer) o2));
    } else if (o1 instanceof Long) {
      return ((Long) o1).compareTo(((Long) o2));
    } else {
      return compareStrings(o1.toString(), o2.toString());
    }
  }

  protected int compareDates(Date d1, Date d2) {
    return d1.before(d2) ? -1 : 1;
  }

  protected int compareStrings(String s1, String s2) {
    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }

}
