/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class FileModel extends BaseModelData {

  protected FileModel() {

  }

  public FileModel(String name, String path) {
    setName(name);
    setPath(path);
  }

  public void setName(String name) {
    set("name", name);
  }

  public void setPath(String path) {
    set("path", path);
  }

  public String getPath() {
    return get("path");
  }

  public String getName() {
    return get("name");
  }

}
