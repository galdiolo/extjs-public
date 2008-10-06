/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Default <code>ListLoadConfig</code> implementation.
 * 
 * @see ListLoadConfig
 */
public class BaseListLoadConfig extends BaseModelData implements ListLoadConfig, Serializable {

  protected Map<String, String> params = new HashMap<String, String>();
  protected SortInfo sortInfo = new SortInfo();

  public SortInfo getSortInfo() {
    return sortInfo;
  }

  public void setSortInfo(SortInfo sortInfo) {
    this.sortInfo = sortInfo;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

}
