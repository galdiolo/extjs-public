/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.data.BaseLoadResult.FailedLoadResult;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

/**
 * A concrete <code>DataProxy</code> that retrieves data using
 * <code>RequestBulder</code> instances.
 * 
 * @see RequestBuilder
 */
public class HttpProxy<C extends LoadConfig> implements DataProxy<C> {

  private RequestBuilder builder;

  public HttpProxy(RequestBuilder builder) {
    this.builder = builder;
  }

  public void load(final DataReader reader, final C loadConfig,
      final DataCallback callback) {
    try {
      builder.sendRequest(generateUrl(loadConfig), new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          callback.setResult(new FailedLoadResult(exception));
        }

        public void onResponseReceived(Request request, Response response) {
          String text = response.getText();
          LoadResult result = reader.read(loadConfig, text);
          // TODO implement this
          // result.setCursor(config.getStart());
          callback.setResult(result);
        }

      });
    } catch (Exception e) {
      callback.setResult(new FailedLoadResult(e));
    }
  }

  protected String generateUrl(LoadConfig loadConfig) {
    StringBuffer sb = new StringBuffer();
    sb.append("start=" + loadConfig.getOffset());
    sb.append("&limit=" + loadConfig.getLimit());
    sb.append("&sortField=" + loadConfig.getSortField());
    sb.append("&sortDir=" + loadConfig.getSortDir());
    return sb.toString();
  }
}