/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;
import java.util.List;

public class BaseLiveTreeGridLoadConfig extends BasePagingLoadConfig implements LiveTreeGridLoadConfig, Serializable {

  protected transient List<String> expanded;

  public List<String> getExpandedNodes() {
    return expanded;
  }

  public void setExpandedNodes(List<String> nodes) {
    this.expanded = nodes;
  }

}
