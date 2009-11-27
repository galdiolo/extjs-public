/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.List;

public interface LiveTreeGridLoadConfig extends PagingLoadConfig {

  /**
   * Returns the id's of the expanded nodes as returned by the ModelKeyProvider.
   * 
   * @return the list of id's
   */
  public List<String> getExpandedNodes();

  /**
   * Sets the expanded node id's.
   * 
   * @param nodes the expanded node id's.
   */
  public void setExpandedNodes(List<String> nodes);

}
