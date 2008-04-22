/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.model;

import com.extjs.gxt.samples.explorer.client.pages.Page;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.widget.Container;

public class Category extends BaseTreeModel {

  protected Category() {
  }
  
  public Category(String name) {
    set("name", name);
  }
  
  public String getName() {
    return (String) get("name");
  }
  
  public String toString() {
    return getName();
  }

  void addEntry(String title, Container page) {
    add(new Entry(title, new Page(page)));
  }

}
