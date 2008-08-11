/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;

/**
 * Default <code>ListLoadConfig</code> implementation.
 * 
 * @see ListLoadConfig
 */
public class BaseListLoadConfig extends BaseModelData implements ListLoadConfig, Serializable {

  protected SortInfo sortInfo = new SortInfo();

  public SortInfo getSortInfo() {
    return sortInfo;
  }

  public void setSortInfo(SortInfo sortInfo) {
    this.sortInfo = sortInfo;
  }

}
