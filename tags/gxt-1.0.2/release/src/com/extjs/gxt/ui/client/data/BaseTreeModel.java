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
import java.util.Map;

/**
 * Default implementation of the <code>TreeModel</code> interface.
 */
public class BaseTreeModel<T extends TreeModel> extends BaseModel implements TreeModel<T> {

  /**
   * The model's parent.
   */
  protected T parent;

  /**
   * The model's children.
   */
  protected List<T> children;

  /**
   * Creates a new model instance.
   */
  public BaseTreeModel() {
    children = new ArrayList<T>();
  }

  /**
   * Creates a new model instance with the specified properties.
   * 
   * @param properties the initial properties
   */
  public BaseTreeModel(Map<String, Object> properties) {
    super(properties);
    children = new ArrayList<T>();
  }

  /**
   * Creates a new model instance.
   * 
   * @param parent the parent
   */
  public BaseTreeModel(T parent) {
    this();
    parent.add(this);
  }

  /**
   * Adds a child to the model and fires an {@link ChangeEventSource#Add} event.
   * 
   * @param child the child to be added
   */
  public void add(T child) {
    insert(child, getChildCount());
  }

  /**
   * Returns the child at the given index or <code>null</code> if the index is
   * out of range.
   * 
   * @param index the index to be retrieved
   * @return the model at the index
   */
  public T getChild(int index) {
    if ((index < 0) || (index >= children.size())) return null;
    return children.get(index);
  }

  /**
   * Returns the number of children.
   * 
   * @return the number of children
   */
  public int getChildCount() {
    return children.size();
  }

  /**
   * Returns the model's children.
   * 
   * @return the children
   */
  public List<T> getChildren() {
    return children;
  }

  /**
   * Returns the model's parent or <code>null</code> if no parent.
   * 
   * @return the parent
   */
  public T getParent() {
    return parent;
  }

  public int indexOf(T child) {
    return children.indexOf(child);
  }

  /**
   * Inserts a child to the model and fires an
   * {@link ChangeEventSource#Add} event.
   * 
   * @param child the child to be inserted
   * @param index the location to insert the child
   */
  public void insert(T child, int index) {
    adopt(child);
    children.add(index, child);
    if (index == getChildCount() - 1) {
      fireEvent(Add, child);
    } else {
      ChangeEvent evt = new ChangeEvent(Add, this);
      evt.item = child;
      evt.index = index;
      notify(evt);
    }
  }

  public boolean isLeaf() {
    return children.size() == 0;
  }

  @Override
  public void notify(ChangeEvent evt) {
    super.notify(evt);
    if (parent != null && parent instanceof ChangeEventSource) {
      ((ChangeEventSource) parent).notify(evt);
    }
  }

  /**
   * Removes the child at the given index.
   * 
   * @param index the child index
   */
  public void remove(int index) {
    if (index >= 0 && index < getChildCount()) {
      remove(getChild(index));
    }
  }

  /**
   * Removes the child from the model and fires a
   * {@link ChangeEventSource#Remove} event.
   * 
   * @param child the child to be removed
   */
  public void remove(T child) {
    child.setParent(null);
    children.remove(child);
    fireEvent(Remove, child);
  }

  public void removeAll() {
    for (int i = children.size() - 1; i >= 0; i--) {
      remove(getChild(i));
    }
  }

  /**
   * Sets the model's children. All existing children are first removed.
   * 
   * @param children the children to be set
   */
  public void setChildren(List<T> children) {
    removeAll();
    if (children != null) {
      for (T child : children) {
        add(child);
      }
    }
  }

  public void setParent(T parent) {
    this.parent = parent;
  }

  private void adopt(TreeModel child) {
    if (child.getParent() != null && child.getParent() != this) {
      child.getParent().remove(child);
    }
    child.setParent(this);
  }
}
