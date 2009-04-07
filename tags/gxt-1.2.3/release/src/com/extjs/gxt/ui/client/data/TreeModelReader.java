/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>DataReader</code> that simply returns the children of the passed
 * TreeModel.
 */
public class TreeModelReader implements DataReader<ModelData, List<? extends ModelData>> {

  protected String childrenProperty;

  /**
   * Creates a tree model reader.
   */
  public TreeModelReader() {

  }

  /**
   * Creates a new tree model reader.
   * 
   * @param childrenProperty the children property name
   */
  public TreeModelReader(String childrenProperty) {
    this.childrenProperty = childrenProperty;
  }

  public List<? extends ModelData> read(ModelData loadConfig, Object data) {
    return getChildren((ModelData) data);
  }

  /**
   * Returns the children for the given parent. The default implementation first
   * checks the childrenProperty. If null, and the parent is a TreeModel
   * instance, its children are returned. Finally, an empty list is returned.
   * 
   * @param parent the parent model
   * @return the parents children
   */
  protected List<? extends ModelData> getChildren(ModelData parent) {
    if (childrenProperty != null) {
      return parent.get(childrenProperty);
    } else if (parent instanceof TreeModel) {
      return ((TreeModel) parent).getChildren();
    }
    return new ArrayList<ModelData>();
  }

  /**
   * Returns the property name that contains the model's children.
   * 
   * @return the children property name
   */
  public String getChildrenProperty() {
    return childrenProperty;
  }

  /**
   * Sets the property name that contains the children for the parent model.
   * 
   * @param childrenProperty the property name
   */
  public void setChildrenProperty(String childrenProperty) {
    this.childrenProperty = childrenProperty;
  }

}
