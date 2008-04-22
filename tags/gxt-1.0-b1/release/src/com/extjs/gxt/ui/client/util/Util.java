/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.List;

import com.extjs.gxt.ui.client.widget.Component;


/**
 * Various utility funcitons.
 */
public class Util {
  
  public static int constrain(int value, int min, int max) {
    value = Math.max(value, min);
    value = Math.min(value, max);
    return value;
  }

  /**
   * Creates a Component[] from a list of Component's.
   * 
   * @param list the list
   * @return the array
   */
  public static Component[] createArray(List list) {
    int size = list.size();
    Component[] components = new Component[size];
    for (int i = 0; i < components.length; i++) {
      components[i] = (Component) list.get(i);
    }
    return components;
  }

  /**
   * Populates a list with an array of elements.
   * 
   * @param list the list
   * @param elements the elements to be added to the list
   */
  public static void fill(List list, Object[] elements) {
    for (int i = 0; i < elements.length; i++) {
      list.add(elements[i]);
    }
  }
  
  public static int indexOf(Object[] elements, Object elem) {
    for (int i = 0; i < elements.length; i++) {
      if (elements[i] == elem) {
        return i;
      }
    }
    
    return -1;
  }

  /**
   * Tests if the value is an integer.
   * 
   * @param value the value to test
   * @return the integer state
   */
  public static boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
