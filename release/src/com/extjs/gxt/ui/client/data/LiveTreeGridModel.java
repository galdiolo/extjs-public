package com.extjs.gxt.ui.client.data;

public class LiveTreeGridModel {

  protected ModelData model;
  protected boolean leaf;
  protected int depth;
  
  public ModelData getModel() {
    return model;
  }
  
  public boolean isLeaf() {
    return leaf;
  }
  
  public int depth() {
    return depth;
  }
  
}
