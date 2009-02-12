/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

public class Padding {

  public String left;

  public String top;

  public String right;

  public String bottom;

  public Padding() {

  }

  public Padding(String padding) {
    if (padding.indexOf(" ") != -1) {
      String[] tokens = padding.split(" ");
      for (int i = 0; i < tokens.length; i++) {
        String v = tokens[i];
        switch (i) {
          case 0:
            top = v;
            break;
          case 1:
            right = v;
            break;
          case 2:
            bottom = v;
            break;
          case 3:
            left = v;
            break;
        }
      }
    } else {
      left = padding;
      top = padding;
      right = padding;
      bottom = padding;
    }

  }

}
