/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.selection;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * Base interface for selection models. Defines the minimum methods required by
 * a selection model.
 * 
 * @param <C> the selection source type
 */
public interface SelectionModel<C extends Component> {

  /**
   * Binds the selection model to the component.
   * 
   * @param component the component
   */
  public void bind(C component);

  /**
   * Clears all selections.
   */
  public void deselectAll();

  /**
   * Selects all items.
   */
  public void selectAll();

  /**
   * Refreshes the current selections.
   */
  public void refresh();


}
