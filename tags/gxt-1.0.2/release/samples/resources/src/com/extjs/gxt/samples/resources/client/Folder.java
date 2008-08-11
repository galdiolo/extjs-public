/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.TreeModel;

public class Folder extends BaseTreeModel<TreeModel> implements Serializable {

  public Folder() {

  }

  public Folder(String name) {
    set("name", name);
  }

  public Folder(String name, BaseTreeModel[] children) {
    this(name);
    for (int i = 0; i < children.length; i++) {
      add(children[i]);
    }
  }

  public String getName() {
    return (String) get("name");
  }

  public String toString() {
    return getName();
  }

}
