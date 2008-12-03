/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.XDOM;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ScriptTagProxy<C, D> implements DataProxy<C, D> {

  private static int ID = 0;
  private AsyncCallback<D> callback;
  private DataReader<C, D> reader;
  private C config;
  private String url;
  private Element head = XDOM.getHead();

  public ScriptTagProxy(String url) {
    this.url = url;
  }

  public void load(DataReader<C, D> reader, C loadConfig, AsyncCallback<D> callback) {
    this.callback = callback;
    this.reader = reader;
    this.config = loadConfig;

    String transId = "transId" + ID++;
    String prepend = url.indexOf("?") != -1 ? "&" : "?";
    String u = url + prepend + "callback=" + transId + generateUrl(loadConfig);

    createCallback(this, transId);

    Element script = DOM.createElement("script");
    script.setAttribute("src", u);
    script.setAttribute("id", transId);
    script.setAttribute("type", "text/javascript");
    script.setAttribute("language", "JavaScript");

    head.appendChild(script);
  }

  /**
   * Sets the proxies url.
   * 
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  protected void destroyTrans(String id) {
    head.removeChild(DOM.getElementById(id));
  }

  protected String generateUrl(C loadConfig) {
    StringBuffer sb = new StringBuffer();
    if (loadConfig instanceof ListLoadConfig) {
      ListLoadConfig cfg = (ListLoadConfig) loadConfig;
      sb.append("&sortField=" + cfg.getSortInfo().getSortField());
      sb.append("&sortDir=" + cfg.getSortInfo().getSortDir());
    }
    if (loadConfig instanceof PagingLoadConfig) {
      PagingLoadConfig cfg = (PagingLoadConfig) loadConfig;
      sb.append("&start=" + cfg.getOffset());
      sb.append("&limit=" + cfg.getLimit());
    }

    if (loadConfig instanceof BasePagingLoadConfig) {
      BasePagingLoadConfig cfg = (BasePagingLoadConfig) loadConfig;
      for (String s : cfg.getParams().keySet()) {
        sb.append("&" + s + "=" + cfg.getParams().get(s));
      }
    }
    return sb.toString();
  }

  protected void onReceivedData(String transId, JavaScriptObject jso) {
    try {
      D data = null;
      if (reader != null) {
        data = reader.read(config, jso);
      } else {
        data = (D) jso;
      }
      callback.onSuccess(data);
    } catch (Exception e) {
      callback.onFailure(e);
    }
    destroyTrans(transId);
  }

  private native void createCallback(ScriptTagProxy proxy, String transId) /*-{
    cb = function( j ){
    proxy.@com.extjs.gxt.ui.client.data.ScriptTagProxy::onReceivedData(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(transId, j);
    };
    $wnd[transId]=cb;
    }-*/;

}
