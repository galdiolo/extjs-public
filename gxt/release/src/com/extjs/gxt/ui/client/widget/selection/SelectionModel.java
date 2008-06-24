/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.selection;

import java.util.List;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;

/**
 * Base interface for selection models. Defines the minimum methods required by
 * a selection model.
 * 
 * @param <C> the selection container type
 * @param <T> the item type
 */
public interface SelectionModel<C extends Container, T extends Component> {

  /**
   * Binds the selection model to the container.
   * 
   * @param container the container
   */
  public void bind(C container);

  /**
   * Deselects the item at the given index.
   * 
   * @param index the index of the item to be deselected
   */
  public void deselect(int index);

  /**
   * Deselects the range.
   * 
   * @param start the start index
   * @param end the end index
   */
  public void deselect(int start, int end);

  /**
   * Deselects the items.
   * 
   * @param items the item
   */
  public void deselect(List<T> items);

  /**
   * Deselects the items.
   * 
   * @param items the items to deselect
   */
  public void deselect(T... items);

  /**
   * Deselects the item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(T item);

  /**
   * Deselects all selections.
   */
  public void deselectAll();

  /**
   * Returns the selected item.
   */
  public T getSelectedItem();

  /**
   * Returns the selected items.
   */
  public List<T> getSelectedItems();

  /**
   * Returns true if the item is selcted.
   * 
   * @param item the item
   * @return true if selected
   */
  public boolean isSelected(T item);

  /**
   * Refreshes the current selections.
   */
  public void refresh();

  /**
   * Selects the item at the given index.
   * 
   * @param index the index of the item to be selected
   */
  public void select(int index);

  /**
   * Selects the range.
   * 
   * @param start the start index
   * @param end the end index
   */
  public void select(int start, int end);

  /**
   * Selects the items. Selects the first item for single-select.
   * 
   * @param items the items to select
   */
  public void select(List<T> items);

  /**
   * Selects the items. Selects the first item for single-select.
   * 
   * @param items the items
   */
  public void select(T... items);
  
  /**
   * Selects the item.
   * 
   * @param item the item
   */
  public void select(T item);

  /**
   * Selets all items.
   */
  public void selectAll();

}
