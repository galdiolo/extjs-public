/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.data.BaseLoadResult.FailedLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <code>DataProxy</code> implementation that retrieves data using GWT RPC.
 */
public abstract class RpcProxy<C extends LoadConfig, R extends LoadResult> implements DataProxy<C> {

  public void load(final DataReader reader, final C loadConfig,
      final DataCallback callback) {

    AsyncCallback<R> asyncCallback = new AsyncCallback<R>() {
      public void onFailure(Throwable caught) {
        callback.setResult(new FailedLoadResult(caught));
      }

      public void onSuccess(LoadResult result) {
        callback.setResult(result);
      }
    };
    load(reader, loadConfig, asyncCallback);
  }

  /**
   * Subclasses should make RPC call using the load configuration.
   * 
   * @param callback the callback to be used when making the rpc call.
   */
  protected abstract void load(final DataReader reader, final C loadConfig,
      AsyncCallback<R> callback);

}
