/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.EventListener;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;

/**
 * Provides a mapping between typed and untyped listeners that GXT supports.
 */
public class TypedListener extends BaseTypedListener {

  public TypedListener(EventListener eventListener) {
    super(eventListener);
  }

  public void handleEvent(BaseEvent be) {
    ComponentEvent ce = be instanceof ComponentEvent ? (ComponentEvent) be : null;
    switch (be.type) {
      case Events.EffectStart:
        ((EffectListener) eventListener).effectStart(ce);
        break;
      case Events.EffectCancel:
        ((EffectListener) eventListener).effectCancel(ce);
        break;
      case Events.EffectComplete:
        ((EffectListener) eventListener).effectComplete(ce);
        break;
      case Events.Attach:
        ((WidgetListener) eventListener).widgetAttached(ce);
        break;
      case Events.Detach:
        ((WidgetListener) eventListener).widgetDetached(ce);
        break;
      case Events.Resize:
        ((WidgetListener) eventListener).widgetResized(ce);
        break;
      case Events.Select:
        ((SelectionListener) eventListener).componentSelected(ce);
        break;
      case Events.DragStart:
        ((DragListener) eventListener).dragStart((DragEvent) be);
        break;
      case Events.DragMove:
        ((DragListener) eventListener).dragMove((DragEvent) be);
        break;
      case Events.DragCancel:
        ((DragListener) eventListener).dragCancel((DragEvent) be);
        break;
      case Events.DragEnd:
        ((DragListener) eventListener).dragEnd((DragEvent) be);
        break;
      case Events.Scroll:
        ((ScrollListener) eventListener).widgetScrolled(ce);
        break;
      case Store.Add:
        ((StoreListener) eventListener).add((StoreEvent) be);
        break;
    }
  }

}
