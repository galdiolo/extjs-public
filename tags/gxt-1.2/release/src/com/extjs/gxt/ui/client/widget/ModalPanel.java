/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.Stack;

import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.Size;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A panel that grays out the view port and displays a widget above it.
 */
public class ModalPanel extends BoxComponent {

  private static Stack modalStack = new Stack();
  private static ModalPanel last;

  /**
   * Returns a ModalPanel from the stack.
   * 
   * @return the panel
   */
  public static ModalPanel pop() {
    ModalPanel panel = modalStack.size() > 0 ? (ModalPanel) modalStack.pop() : null;
    if (panel == null) {
      panel = new ModalPanel();
    }
    last = panel;
    return panel;
  }

  /**
   * Pushes the last popped panel onto the stack.
   */
  public static void push() {
    if (last != null) {
      push(last);
    }
  }

  /**
   * Pushes a panel back onto the stack.
   * 
   * @param panel the panel
   */
  public static void push(ModalPanel panel) {
    panel.hide();
    last = null;
    modalStack.push(panel);
  }

  private boolean blink;
  private Component component;
  private boolean blinking;
  private BaseEventPreview eventPreview;

  /**
   * Creates a new model panel.
   */
  public ModalPanel() {
    baseStyle = "x-modal";
    shim = true;
    setShadow(false);
  }

  /**
   * Returns the panel's event preview.
   * 
   * @return the event preview
   */
  public BaseEventPreview getEventPreview() {
    return eventPreview;
  }

  /**
   * Hides the panel.
   */
  public void hide() {
    super.hide();
    el().setZIndex(-1);
    component = null;
    eventPreview.getIgnoreList().removeAll();
    eventPreview.remove();
    if (layer != null) {
      layer.disableShadow();
      layer.hideShim();
    }
    RootPanel.get().remove(this);
  }

  /**
   * Returns true if blinking is enabled.
   * 
   * @return the blink state
   */
  public boolean isBlink() {
    return blink;
  }

  /**
   * Fowards a event to the underlying event preview instance.
   * 
   * @param event the event
   */
  public void relayEvent(Event event) {
    eventPreview.onEventPreview(event);
  }

  /**
   * True to blink the widget being displayed when the use clicks outside of the
   * widgets bounds (defaults to false).
   * 
   * @param blink true to blink
   */
  public void setBlink(boolean blink) {
    this.blink = blink;
  }

  /**
   * Displays the panel.
   */
  public void show(Component component) {
    this.component = component;
    RootPanel.get().add(this);

    el().makePositionable(true);
    el().updateZIndex(0);
    component.el().updateZIndex(0);

    super.show();

    eventPreview.getIgnoreList().removeAll();
    eventPreview.getIgnoreList().add(component.getElement());

    eventPreview.add();
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(component);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(component);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);

    El body = XDOM.getBodyEl();
    int width = body.dom.getScrollWidth();
    int height = body.dom.getScrollHeight();
    Size vp = XDOM.getViewportSize();

    String w = "100%";
    String h = "100%";
    if (width > vp.width) {
      w = width + "px";
    }
    if (height > vp.height) {
      h = height + "px";
    }
    el().setSize(w, h);

    eventPreview = new BaseEventPreview() {

      @Override
      protected boolean onAutoHide(PreviewEvent pe) {
        if (blink && !blinking) {
          blinking = true;
          component.el().blink(new FxConfig(new Listener<FxEvent>() {
            public void handleEvent(FxEvent fe) {
              blinking = false;
            }
          }));
        }
        return false;
      }

    };
  }

}
