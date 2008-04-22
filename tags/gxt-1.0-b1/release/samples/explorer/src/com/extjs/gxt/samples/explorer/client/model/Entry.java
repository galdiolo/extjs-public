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

public class Entry extends BaseTreeModel {

  private String name;

  protected Entry() {

  }

  public Entry(String name, Page page) {
    this.name = name;
    set("name", name);
    set("page", page);
    set("path", "images/thumbs/" + getId() + ".gif");
  }

  public String getName() {
    return (String) get("name");
  }

  public String getId() {
    if (name.equals("% Columns")) {
      return "percentcolumns";
    }
    return name.replaceAll(" ", "").toLowerCase();
  }

  public Page getPage() {
    return (Page) get("page");
  }

  public String toString() {
    return getName();
  }

}
