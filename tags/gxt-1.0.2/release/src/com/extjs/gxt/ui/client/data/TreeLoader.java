/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * A <code>Loader</code> for trees.
 */
public interface TreeLoader<M extends ModelData> extends Loader<M> {

  /**
   * Returns whether the given model has children.
   * 
   * @param parent the parent model
   * @return true if the given parent has children, and false if it has no
   *         children
   */
  public boolean hasChildren(M parent);

  /**
   * Initiates a load request for the parent's children.
   * 
   * @param parent the parent
   * @return true if the load was requested
   */
  public boolean loadChildren(M parent);

}
