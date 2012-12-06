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
import com.sencha.gxt.legacy.client.mvc.BeforeAppEvent.BeforeAppEventHandler;

/**
 * Fired before an app event is dispatched. The event can be canceled by calling
 * {@link BeforeAppEvent#setCanceled(boolean)}.
 * 
 */
public class BeforeAppEvent extends GwtEvent<BeforeAppEventHandler> {
  private static GwtEvent.Type<BeforeAppEventHandler> TYPE;

  public static GwtEvent.Type<BeforeAppEventHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<BeforeAppEventHandler>();
    }
    return TYPE;
  }

  private final AppEvent appEvent;
  private boolean canceled;

  public BeforeAppEvent(AppEvent appEvent) {
    this.appEvent = appEvent;
  }

  public AppEvent getAppEvent() {
    return appEvent;
  }

  @Override
  public GwtEvent.Type<BeforeAppEventHandler> getAssociatedType() {
    return getType();
  }

  @Override
  protected void dispatch(BeforeAppEventHandler handler) {
    handler.beforeDispatch(this);
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  public interface BeforeAppEventHandler extends EventHandler {
    void beforeDispatch(BeforeAppEvent event);
  }

}
