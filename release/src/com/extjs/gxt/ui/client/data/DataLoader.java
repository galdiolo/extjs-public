/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * A <code>BaseLoader</code> used to retrieve and convert raw data. Data is
 * retrieved using a <code>DataProxy</code> and optionally read and parsed
 * using a <code>DataReader</code>.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeLoad</b> : LoadEvent(loader, config)<br>
 * <div>Fires before a load operation. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Load</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>LoadException</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see DataProxy
 * @see DataReader
 */
public class DataLoader<C extends BaseLoadConfig> extends BaseLoader<C> {

  private DataProxy<C> proxy;
  private DataReader reader;

  /**
   * Creates a new loader instance with the given proxy. A reader is not
   * specified and will not be passed to the proxy at load time.
   * 
   * @param proxy the data proxy
   */
  public DataLoader(DataProxy<C> proxy) {
    this.proxy = proxy;
  }

  /**
   * Creates a new loader with the given proxy and reader.
   * 
   * @param proxy the data proxy
   * @param reader an optional data reader, if null, null will be passed to
   *            proxy.load(Reader, Loadconfig, Datacallback)
   */
  public DataLoader(DataProxy<C> proxy, DataReader reader) {
    this.proxy = proxy;
    this.reader = reader;
  }

  protected void loadData(final C config, DataCallback callback) {
    proxy.load(reader, config, new DataCallback() {
      public void setResult(LoadResult result) {
        onLoadResult(config, result);
      }
    });
  }

}
