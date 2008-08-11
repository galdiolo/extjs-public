/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.js.JsObject;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Various utility functions.
 */
public class Util {

  /**
   * Constrains the value by a min and max value.
   * 
   * @param value the value
   * @param min the minimum
   * @param max the maximum
   * @return the adjusted value
   */
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
   * Creates a new list and adds the element(s).
   * 
   * @param element the item to add to the list
   * @return the new list
   */
  public static List createList(Object... element) {
    List list = new ArrayList();
    fill(list, element);
    return list;
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

  /**
   * Returns the model's data as a javascript object.
   * 
   * @param model the model
   * @return the javascript object
   */
  public static JavaScriptObject getJsObject(ModelData model) {
    JsObject jsObj = new JsObject();
    for (String key : model.getPropertyNames()) {
      Object value = model.get(key);
      jsObj.set(key, value);
    }
    return jsObj.getJsObject();
  }

  /**
   * Returns the index of a object in an array.
   * 
   * @param elements the array
   * @param elem the elemnt
   * @return the index or -1 if elem not in the array
   */
  public static int indexOf(Object[] elements, Object elem) {
    for (int i = 0; i < elements.length; i++) {
      if (elements[i] == elem) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Returns true if the style ends with .jpg, .gif, or .png.
   * 
   * @param style the style
   * @return true for an image path
   */
  public static boolean isImagePath(String style) {
    return style.matches(".*(jpg$|gif$|png$)");
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
