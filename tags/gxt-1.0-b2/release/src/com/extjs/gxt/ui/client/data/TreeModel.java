/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.List;

/**
 * A Model that supports children.
 */
public interface TreeModel extends Model {

  /**
   * Adds a child to the model.
   * 
   * @param child the model to add
   */
  public abstract void add(TreeModel child);

  /**
   * Returns the child at the given index.
   * 
   * @param index the index
   * @return the child
   */
  public abstract TreeModel getChild(int index);

  /**
   * Returns the child count.
   * 
   * @return the child count
   */
  public abstract int getChildCount();

  /**
   * Returns the model's children.
   * 
   * @return the children
   */
  public abstract List<TreeModel> getChildren();

  /**
   * Returns the model's parent.
   * 
   * @return the parent
   */
  public abstract TreeModel getParent();

  /**
   * Inserts a child.
   * 
   * @param child the child to add
   * @param index the insert location
   */
  public abstract void insert(TreeModel child, int index);

  /**
   * Removes a child.
   * 
   * @param child the child to remove
   */
  public abstract void remove(TreeModel child);

  /**
   * Sets the model's parent.
   * 
   * @param parent the new parent
   */
  public abstract void setParent(TreeModel parent);

}