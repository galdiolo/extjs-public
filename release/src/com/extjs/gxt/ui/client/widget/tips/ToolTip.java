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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
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
public class ToolTip extends Tip {

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true). If {@link #closable} = true a close tool button will be rendered into
   * the tooltip header.
   */
  public boolean autoHide = true;

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   */
  public int showDelay = 500;

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   */
  public int hideDelay = 200;

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   */
  public int dismissDelay = 5000;

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [10,10]).
   */
  public int[] mouseOffset = new int[] {10, 10};

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   */
  public boolean trackMouse;

  private Date lastActive;
  private Timer dismissTimer;
  private Timer showTimer;
  private Timer hideTimer;
  private Component target;
  private Point targetXY;
  private ToolTipConfig config;

  /**
   * Creates a new tool tip.
   */
  public ToolTip() {
    hidden = true;
    lastActive = new Date();
  }

  /**
   * Creates a new tool tip for the given target.
   * 
   * @param target the target widget
   */
  public ToolTip(Component target, ToolTipConfig config) {
    this();
    this.config = config;
    initTarget(target);
  }
  
  public void hide() {
    clearTimer("dismiss");
    lastActive = new Date();
    super.hide();
  }

  public void show() {
    super.show();
    showAt(getTargetXY());
  }

  public void showAt(int x, int y) {
    lastActive = new Date();
    clearTimers();
    super.showAt(x, y);
    if (dismissDelay != -1 && autoHide) {
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
   * @return this
   */
  public ToolTip update(ToolTipConfig config) {
    this.config = config;
    if (!hidden) {
      updateContent();
    }
    return this;
  }

  protected void updateContent() {
    getHeader().setText(config.title == null ? "" : config.title);
    if (config.text != null) {
      fly(getElement("body")).update(config.text);
    }
  }

  private void clearTimer(String timer) {
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

  private void clearTimers() {
    clearTimer("show");
    clearTimer("dismiss");
    clearTimer("hide");
  }

  private void delayHide() {
    if (!hidden && hideTimer == null) {
      hideTimer = new Timer() {
        public void run() {
          hide();
        }
      };
      hideTimer.schedule(hideDelay);
    }
  }

  private void delayShow() {
    if (hidden && showTimer == null) {
      if ((new Date().getTime() - lastActive.getTime()) < quickShowInterval) {
        show();
      } else {
        showTimer = new Timer() {
          public void run() {
            show();
          }
        };
        showTimer.schedule(showDelay);
      }

    } else if (!hidden && !autoHide) {
      show();
    }
  }

  private Point getTargetXY() {
    int x = targetXY.x + mouseOffset[0];
    int y = targetXY.y + mouseOffset[1];
    return new Point(x, y);
  }

  private void initTarget(final Component target) {
    this.target = target;
    Listener<ComponentEvent> l = new Listener<ComponentEvent>() {

      public void handleEvent(ComponentEvent ce) {
        Element source = target.getElement();
        switch (ce.getEventType()) {
          case Event.ONMOUSEOVER:
            Element from = DOM.eventGetFromElement(ce.event);
            if (!DOM.isOrHasChild(source, from)) {
              onTargetOver(ce);
            }
            break;
          case Event.ONMOUSEOUT:
            Element to = DOM.eventGetToElement(ce.event);
            if (!DOM.isOrHasChild(source, to)) {
              onTargetOut(ce);
            }
            break;
          case Events.MouseMove:
            onMouseMove(ce);
            break;
        }
      }

    };
    target.addListener(Event.ONMOUSEOVER, l);
    target.addListener(Event.ONMOUSEOUT, l);
    target.addListener(Event.ONMOUSEMOVE, l);
  }

  private void onMouseMove(ComponentEvent ce) {
    targetXY = ce.getXY();
    if (!hidden && trackMouse) {
      setPagePosition(getTargetXY());
    }
  }

  private void onTargetOut(ComponentEvent ce) {
    if (disabled) {
      return;
    }
    clearTimer("show");
    if (autoHide) {
      delayHide();
    }
  }

  private void onTargetOver(ComponentEvent ce) {
    if (disabled || !ce.within(target.getElement())) {
      return;
    }
    clearTimer("hide");
    targetXY = ce.getXY();
    delayShow();
  }

}
