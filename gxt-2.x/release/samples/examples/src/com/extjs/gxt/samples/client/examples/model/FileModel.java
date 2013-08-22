/*
 * Sencha GXT 2.3.0 - Sencha for GWT
 * Copyright(c) 2007-2013, Sencha, Inc.
 * licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
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

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof FileModel) {
      FileModel mobj = (FileModel) obj;
      return getName().equals(mobj.getName()) && getPath().equals(mobj.getPath());
    }
    return super.equals(obj);
  }

}
