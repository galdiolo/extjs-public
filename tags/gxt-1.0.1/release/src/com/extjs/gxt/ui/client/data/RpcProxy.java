/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <code>DataProxy</code> implementation that retrieves data using GWT RPC.
 */
public abstract class RpcProxy<C, D> implements DataProxy<C, D> {

  public void load(final DataReader<C, D> reader, final C loadConfig,
      final AsyncCallback<D> callback) {
    load(loadConfig, new AsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess(Object result) {
        try {
          D data = null;
          if (reader != null) {
            data = reader.read(loadConfig, result);
          } else {
            data = (D) result;
          }
          callback.onSuccess(data);
        } catch (Exception e) {
          callback.onFailure(e);
        }
      }

    });
  }

  /**
   * Subclasses should make RPC call using the load configuration.
   * 
   * @param callback the callback to be used when making the rpc call.
   */
  protected abstract void load(C loadConfig, AsyncCallback<D> callback);

}
