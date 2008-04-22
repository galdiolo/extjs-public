/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import com.extjs.gxt.ui.client.Style.SortDir;

public class SortInfo {

  /**
   * The sort field.
   */
  public String sortField;
  
  /**
   * The sort direction.
   */
  public SortDir sortDir;
  
  public SortInfo(String field, SortDir sortDir) {
    this.sortField = field;
    this.sortDir = sortDir;
  }
  
}
