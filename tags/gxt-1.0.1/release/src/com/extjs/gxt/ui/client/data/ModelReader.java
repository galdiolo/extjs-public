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

/**
 * A <code>DataReader</code> implementation for <code>Model</code>
 * instances.
 */
public class ModelReader<C> implements DataReader<C, ListLoadResult<ModelData>> {

  public ListLoadResult read(C loadConfig, Object data) {
    if (data instanceof ModelData) {
      List list = new ArrayList();
      list.add(data);
      return new BaseListLoadResult(list);
    } else if (data instanceof List) {
      return new BaseListLoadResult((List) data);
    } else if (data instanceof ListLoadResult) {
      return (ListLoadResult)data;
    } else {
      throw new RuntimeException("Error converting data");
    }
  }

}
