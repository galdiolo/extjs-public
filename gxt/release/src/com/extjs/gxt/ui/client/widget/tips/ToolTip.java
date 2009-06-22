/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;

import java.util.Date;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

/**
 * A standard tooltip implementation for providing additional information when
 * hovering over a target element.
 */
public class ToolTip extends Tip implements Listener<ComponentEvent> {

  protected Component target;
  protected Point targetXY;

  private int showDelay = 500;
  private int[] mouseOffset = new int[] {10, 10};
  private boolean trackMouse;
  private boolean autoHide = true;
  private int hideDelay = 200;
  private int dismissDelay = 5000;
  private Date lastActive;
  private Timer dismissTimer;
  private Timer showTimer;
  private Timer hideTimer;
  private Template template;
  private Params params;
  protected String title, text;

  /**
   * Creates a new tool tip.
   */
  public ToolTip() {
    hidden = true;
    lastActive = new Date();
  }

  /**
   * Creates a new tool tip.
   * 
   * @param target the target widget
   */
  public ToolTip(Component target) {
    this();
    initTarget(target);
  }

  /**
   * Creates a new tool tip for the given target.
   * 
   * @param target the target widget
   */
  public ToolTip(Component target, ToolTipConfig config) {
    this();
    updateConfig(config);
    initTarget(target);
  }

  /**
   * Returns the dismiss delay.
   * 
   * @return the dismiss delay
   */
  public int getDismissDelay() {
    return dismissDelay;
  }

  /**
   * Returns the hide delay.
   * 
   * @return the hide delay
   */
  public int getHideDelay() {
    return hideDelay;
  }

  /**
   * Returns the mouse offsets.
   * 
   * @return the mouse offsets
   */
  public int[] getMouseOffset() {
    return mouseOffset;
  }

  /**
   * Returns the show delay.
   * 
   * @return the show delay
   */
  public int getShowDelay() {
    return showDelay;
  }

  public void handleEvent(ComponentEvent ce) {
    Element source = target.getElement();
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        Element from = DOM.eventGetFromElement(ce.event);
        if (from != null && !DOM.isOrHasChild(source, from)) {
          onTargetOver(ce);
        }
        break;
      case Event.ONMOUSEOUT:
        Element to = DOM.eventGetToElement(ce.event);
        if (to != null && !DOM.isOrHasChild(source, to)) {
          onTargetOut(ce);
        }
        break;
      case Event.ONMOUSEMOVE:
        onMouseMove(ce);
        break;
      case Events.Hide:
      case Events.Detach:
        hide();
        break;
    }
  }

  @Override
  public void hide() {
    clearTimers();
    lastActive = new Date();
    super.hide();
  }

  /**
   * Returns true if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * Returns true if mouse tracking is enabled.
   * 
   * @return the track mouse state
   */
  public boolean isTrackMouse() {
    return trackMouse;
  }

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true). If {@link #closable} = true a close tool button will be rendered
   * into the tooltip header.
   * 
   * @param autoHide the auto hide state
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   * 
   * @param dismissDelay the dismiss delay in milliseconds
   */
  public void setDismissDelay(int dismissDelay) {
    this.dismissDelay = dismissDelay;
  }

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   * 
   * @param hideDelay the hide delay
   */
  public void setHideDelay(int hideDelay) {
    this.hideDelay = hideDelay;
  }

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [10,10]).
   * 
   * @param mouseOffset the mouse offsets
   */
  public void setMouseOffset(int[] mouseOffset) {
    this.mouseOffset = mouseOffset;
  }

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   * 
   * @param showDelay the show delay in milliseconds
   */
  public void setShowDelay(int showDelay) {
    this.showDelay = showDelay;
  }

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   * 
   * @param trackMouse the track mouse state
   */
  public void setTrackMouse(boolean trackMouse) {
    this.trackMouse = trackMouse;
  }

  @Override
  public void show() {
    super.show();
    showAt(getTargetXY());
  }

  @Override
  public void showAt(int x, int y) {
    lastActive = new Date();
    clearTimers();
    super.showAt(x, y);
    if (dismissDelay > 0 && autoHide) {
      dismissTimer = new Timer() {
        public void run() {
          hide();
        }
      };
      dismissTimer.schedule(dismissDelay);
    }
  }

  /**
   * Updates the tool tip with the given config.
   * 
   * @param config the tool tip config
   */
  public void update(ToolTipConfig config) {
    updateConfig(config);
    if (!hidden) {
      updateContent();
    }
  }

  protected void clearTimer(String timer) {
    if (timer.equals("hide")) {
      if (hideTimer != null) {
        hideTimer.cancel();
        hideTimer = null;
      }
    } else if (timer.equals("dismiss")) {
      if (dismissTimer != null) {
        dismissTimer.cancel();
        dismissTimer = null;
      }
    } else if (timer.equals("show")) {
      if (showTimer != null) {
        showTimer.cancel();
        showTimer = null;
      }
    }
  }

  protected void clearTimers() {
    clearTimer("show");
    clearTimer("dismiss");
    clearTimer("hide");
  }

  protected void delayShow() {
    if (hidden && showTimer == null) {
      if ((new Date().getTime() - lastActive.getTime()) < quickShowInterval) {
        show();
      } else {
        showTimer = new Timer() {
          public void run() {
            show();
          }
        };
        showTimer.schedule(getShowDelay());
      }

    } else if (!hidden && autoHide) {
      show();
    }
  }

  protected void onMouseMove(ComponentEvent ce) {
    targetXY = ce.getXY();
    if (!hidden && trackMouse) {
      setPagePosition(getTargetXY());
    }
  }

  protected void onTargetOut(ComponentEvent ce) {
    if (disabled) {
      return;
    }
    clearTimer("show");
    if (autoHide) {
      delayHide();
    }
  }

  protected void onTargetOver(ComponentEvent ce) {
    if (disabled || !ce.within(target.getElement())) {
      return;
    }

    clearTimer("hide");
    targetXY = ce.getXY();
    delayShow();
  }

  protected void updateContent() {
    if (template != null) {
      template.overwrite(getBody().dom, params);
    } else {
      String title = this.title;
      String text = this.text;
      getHeader().setText(title == null ? "" : title);
      if (text != null) {
        getBody().update(text);
      }
    }
  }

  private void delayHide() {
    if (!hidden && hideTimer == null) {
      if(hideDelay == 0) {
        hide();
        return;
      }
      hideTimer = new Timer() {
        public void run() {
          hide();
        }
      };
      hideTimer.schedule(hideDelay);
    }
  }

  private Point getTargetXY() {
    int x = targetXY.x + getMouseOffset()[0];
    int y = targetXY.y + getMouseOffset()[1];
    return new Point(x, y);
  }

  private void initTarget(final Component target) {
    this.target = target;
    target.addListener(Event.ONMOUSEOVER, this);
    target.addListener(Event.ONMOUSEOUT, this);
    target.addListener(Event.ONMOUSEMOVE, this);
    target.addListener(Events.Hide, this);
    target.addListener(Events.Detach, this);
    target.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
  }

  private void updateConfig(ToolTipConfig config) {
    if (!config.isEnabled()) {
      clearTimers();
      hide();
    }

    template = config.getTemplate();
    params = config.getParams();
    text = config.getText();
    title = config.getTitle();
    trackMouse = config.isTrackMouse();
    autoHide = config.isAutoHide();
    showDelay = config.getShowDelay();
    hideDelay = config.getHideDelay();
    dismissDelay = config.getDismissDelay();
  }

}
