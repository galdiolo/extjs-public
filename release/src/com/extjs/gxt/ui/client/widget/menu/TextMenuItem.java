/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

/**
 * Menu item with text and a icon style.
 */
public class TextMenuItem extends Item {

  /**
   * Creates a new text menu item.
   */
  public TextMenuItem() {
    canActivate = true;
  }

  /**
   * Creates a new text menu item.
   * 
   * @param text the item's text
   */
  public TextMenuItem(String text) {
    this();
    setText(text);
  }

}
