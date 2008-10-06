/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A concrete <code>DataProxy</code> that retrieves data using a
 * <code>RequestBulder</code> instances.
 * 
 * @see RequestBuilder
 */
public class HttpProxy<C, D> implements DataProxy<C, D> {

  protected RequestBuilder builder;

  /**
   * Creates a new HttpProxy.
   * 
   * @param builder the request builder
   */
  public HttpProxy(RequestBuilder builder) {
    this.builder = builder;
  }
  
  public void load(final DataReader<C, D> reader, final C loadConfig,
      final AsyncCallback<D> callback) {
    try {
      String data = null;
      if (builder.getHTTPMethod().equals("POST")) {
        data = generateUrl(loadConfig);
      } else {
        String url = builder.getUrl();
        url = url + (url.indexOf("?") == -1 ? "?" : "&");
        String params = generateUrl(loadConfig);
        url += params;
        setUrl(builder, url);
      }

      builder.sendRequest(data, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          callback.onFailure(exception);
        }

        public void onResponseReceived(Request request, Response response) {
          String text = response.getText();
          try {
            D data = null;
            if (reader != null) {
              data = reader.read(loadConfig, text);
            } else {
              data = (D) text;
            }
            callback.onSuccess(data);
          } catch (Exception e) {
            callback.onFailure(e);
          }
        }
      });
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  protected String generateUrl(C loadConfig) {
    StringBuffer sb = new StringBuffer();
    if (loadConfig instanceof ListLoadConfig) {
      ListLoadConfig cfg = (ListLoadConfig) loadConfig;
      String field = cfg.getSortInfo().getSortField();
      if (field != null) {
        sb.append("&sortField=" + cfg.getSortInfo().getSortField());
      }
      sb.append("&sortDir=" + cfg.getSortInfo().getSortDir());
    }
    if (loadConfig instanceof PagingLoadConfig) {
      PagingLoadConfig cfg = (PagingLoadConfig) loadConfig;
      sb.append("&start=" + cfg.getOffset());
      sb.append("&limit=" + cfg.getLimit());
    }
    if (sb.length() > 0) {
      return sb.substring(1, sb.length());
    }
    return sb.toString();
  }

  private native void setUrl(RequestBuilder rb, String url) /*-{
    rb.@com.google.gwt.http.client.RequestBuilder::url = url;
  }-*/;
}