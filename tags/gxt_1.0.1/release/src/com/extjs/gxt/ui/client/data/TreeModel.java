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
 * A Model that supports parent and children.
 *
 * @param <T> the tree model type
 */
public interface TreeModel<T extends TreeModel> extends Model {

  /**
   * Adds a child to the model.
   * 
   * @param child the model to add
   */
  public void add(T child);

  /**
   * Returns the child at the given index.
   * 
   * @param index the index
   * @return the child
   */
  public T getChild(int index);

  /**
   * Returns the child count.
   * 
   * @return the child count
   */
  public int getChildCount();

  /**
   * Returns the model's children.
   * 
   * @return the children
   */
  public List<T> getChildren();

  /**
   * Returns the model's parent.
   * 
   * @return the parent
   */
  public T getParent();

  /**
   * Returns the index of the child.
   * 
   * @param child the child
   * @return the index
   */
  public int indexOf(T child);

  /**
   * Inserts a child.
   * 
   * @param child the child to add
   * @param index the insert location
   */
  public void insert(T child, int index);

  /**
   * Returns true if the model is a leaf and has children. The method provides
   * the ability to mark a model as having children before the children have
   * been added.
   * 
   * @return true for leaf
   */
  public boolean isLeaf();

  /**
   * Removes a child.
   * 
   * @param child the child to remove
   */
  public void remove(T child);

  /**
   * Removes all the children.
   */
  public void removeAll();

  /**
   * Sets the model's parent.
   * 
   * @param parent the new parent
   */
  public void setParent(T parent);

}