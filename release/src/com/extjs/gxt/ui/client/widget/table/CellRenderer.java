/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

/**
 * Allows cell values to be modified before being displayed.
 */
public interface CellRenderer {

  /**
   * Returns the formatted cell value.
   * 
   * @param property the property being modified
   * @param value the property value
   * @return the new value as HTML
   */
  public String render(String property, Object value);

}
