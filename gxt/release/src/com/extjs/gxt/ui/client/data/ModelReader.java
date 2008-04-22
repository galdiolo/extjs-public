/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.Collection;

import com.extjs.gxt.ui.client.data.BaseLoadResult.FailedLoadResult;
import com.extjs.gxt.ui.client.data.BaseLoadResult.ModelCollectionLoadResult;

/**
 * A <code>DataReader</code> implementation for <code>Model</code>
 * instances.
 */
public class ModelReader implements DataReader {

  public LoadResult read(LoadConfig loadConfig, Object data) {
    if (data instanceof Model) {
      return new BaseLoadResult<Model>((Model) data);
    } else if (data instanceof Collection) {
      return new ModelCollectionLoadResult<Model>((Collection) data);
    } else {
      return new FailedLoadResult();
    }
  }

}
