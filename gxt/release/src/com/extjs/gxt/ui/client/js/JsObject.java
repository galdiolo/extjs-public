/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.js;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wraps a native javascript object.
 */
public class JsObject implements JsWrapper {

  /**
   * The native javascript object.
   */
  protected JavaScriptObject jsObject;

  /**
   * Creates a new instance.
   */
  public JsObject() {
    jsObject = JsUtil.eval("[{}]");
  }

  /**
   * Creates a new object from the given JSON text.
   * 
   * @param data the JSON text
   */
  public JsObject(String data) {
    jsObject = JsUtil.eval("[{" + data + "}]");
  }

  /**
   * Returns a property value.
   * 
   * @param name the property name
   * @return the value
   */
  public native Object get(String name) /*-{
    var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
    return js[name];
   }-*/;

  public JavaScriptObject getJsObject() {
    return jsObject;
  }

  /**
   * Returns a property value.
   * 
   * @param name the property name
   * @return the value
   */
  public native int getNumber(String name) /*-{
     var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
     return js[name];
   }-*/;

  /**
   * Returns a property value.
   * 
   * @param name the property name
   * @return the value
   */
  public native String getString(String name) /*-{
     var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
     return js[name];
    }-*/;

  public native void set(String name, boolean value) /*-{
     var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
     js[name] = value;
    }-*/;

  public native void set(String name, int value) /*-{
     var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
     js[name] = value;
    }-*/;

  public native void set(String name, JavaScriptObject value) /*-{
      var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
      js[name] = value;

      }-*/;

  /**
   * Sets a property.
   * 
   * @param name the property name
   * @param wrapper the property value
   */
  public void set(String name, JsWrapper wrapper) {
    set(name, wrapper.getJsObject());
  }

  public native void set(String name, Object value) /*-{
     var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
     js[name] = value;
    }-*/;

  public native void set(String name, String value) /*-{
      var js = this.@com.extjs.gxt.ui.client.js.JsObject::jsObject;
      js[name] = value;
     }-*/;

  protected native JavaScriptObject create() /*-{
      return {};
      }-*/;

}
