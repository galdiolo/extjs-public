/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.mvc;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.legacy.client.mvc.AfterAppEvent.AfterAppEventHandler;

public class AfterAppEvent extends GwtEvent<AfterAppEventHandler> {
  private static GwtEvent.Type<AfterAppEventHandler> TYPE;

  public static GwtEvent.Type<AfterAppEventHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<AfterAppEventHandler>();
    }
    return TYPE;
  }

  private final AppEvent appEvent;

  public AfterAppEvent(AppEvent appEvent) {
    this.appEvent = appEvent;
  }

  public AppEvent getAppEvent() {
    return appEvent;
  }

  @Override
  public GwtEvent.Type<AfterAppEventHandler> getAssociatedType() {
    return getType();
  }

  @Override
  protected void dispatch(AfterAppEventHandler handler) {
    handler.afterDispatch(this);
  }

  public interface AfterAppEventHandler extends EventHandler {
    void afterDispatch(AfterAppEvent event);
  }

}
