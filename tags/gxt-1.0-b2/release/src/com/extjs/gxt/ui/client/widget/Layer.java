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
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Shadow.ShadowPosition;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * An extended {@link El} object that supports a shadow and shim, constrain to
 * viewport and automatic maintaining of shadow/shim positions.
 */
public class Layer extends El {

  private static Stack<El> shims = new Stack<El>();

  private Shadow shadow = new Shadow(ShadowPosition.DROP);
  private El shim;
  private boolean shadowDisabled = true;

  /**
   * Creates a new layer instance.
   */
  public Layer() {
    super(DOM.createDiv());
  }

  /**
   * Creates a new layer instance wrapping the specified element.
   * 
   * @param element the element
   */
  public Layer(Element element) {
    super(element);
    makePositionable();
  }

  @Override
  public El alignTo(Element align, String pos, int[] offsets) {
    super.alignTo(align, pos, offsets);
    sync(true);
    return this;
  }

  /**
   * Creates an iframe shim for this element to keep selects and other windowed
   * objects from showing through.
   * 
   * @return the new shim element
   */
  public El createShim() {
    El el = new El(DOM.createIFrame());
    el.setElementAttribute("frameborder", "no");
    el.setStyleName("ext-shim");
    getParent().insertBefore(el.dom, dom);
    return el;
  }

  /**
   * Disables the shadow.
   */
  public void disableShadow() {
    if (shadow != null) {
      shadowDisabled = true;
      Shadow.push(shadow);
    }
  }

  /**
   * Enables the shadow.
   * 
   * @param show true to show
   */
  public void enableShadow(boolean show) {
    if (shadow != null) {
      shadowDisabled = !show;
      if (show) {
        sync(true);
      } else {
        hideShadow();
      }
    } else {
      shadow = Shadow.pop();
    }
  }

  /**
   * Enables the shim.
   */
  public void enableShim() {
    shim = getShim();
  }

  /**
   * Returns the layer's shim.
   * 
   * @return the shim
   */
  public El getShim() {
    shim = shims.size() > 0 ? shims.pop() : null;
    if (shim == null) {
      shim = createShim();
      shim.setVisible(false);
    }
    El pn = getParent();
    El p = shim.getParent();
    if (pn.dom != p.dom) {
      pn.insertBefore(shim.dom, dom);
    }
    shim.setStyleAttribute("zIndex", XDOM.getTopZIndex() - 2);
    return shim;
  }

  /**
   * Hides the layer's shadow.
   */
  public void hideShadow() {
    if (shadow != null) {
      shadow.hide();
    }
  }

  /**
   * Hides the shim.
   */
  public void hideShim() {
    if (shim != null) {
      shim.setVisible(false);
      shims.push(shim);
      shim = null;
    }
  }

  @Override
  public El setHeight(int height) {
    super.setHeight(height);
    sync(true);
    return this;
  }

  @Override
  public El setHeight(int height, boolean adjust) {
    super.setHeight(height, adjust);
    sync(false);
    return this;
  }

  @Override
  public El setHeight(String height) {
    super.setHeight(height);
    sync(true);
    return this;
  }

  @Override
  public El setLeft(int left) {
    super.setLeft(left);
    sync(false);
    return this;
  }

  @Override
  public El setLeftTop(int left, int top) {
    super.setLeftTop(left, top);
    sync(true);
    return this;
  }

  @Override
  public El setSize(int width, int height) {
    super.setSize(width, height);
    sync(false);
    return this;
  }

  @Override
  public El setSize(Size size) {
    super.setSize(size);
    sync(false);
    return this;
  }

  @Override
  public El setVisibility(boolean visible) {
    super.setVisibility(visible);
    sync(visible);
    if (!visible) {
      hideUnders(true);
    }
    return this;
  }

  @Override
  public El setVisible(boolean visible) {
    super.setVisible(visible);
    sync(visible);
    if (!visible) {
      hideUnders(true);
    }
    return this;
  }

  @Override
  public El setWidth(int width) {
    super.setWidth(width);
    sync(false);
    return this;
  }

  @Override
  public El setWidth(int width, boolean adjust) {
    super.setWidth(width, adjust);
    sync(false);
    return this;
  }

  @Override
  public El setWidth(String width) {
    super.setWidth(width);
    sync(false);
    return this;
  }

  @Override
  public El setX(int x) {
    super.setX(x);
    sync(false);
    return this;
  }

  @Override
  public El setXY(int x, int y) {
    super.setXY(x, y);
    sync(false);
    return this;
  }

  @Override
  public El setY(int y) {
    super.setY(y);
    sync(false);
    return this;
  }

  /**
   * Syncs the shadow and shim.
   * 
   * @param show true to show
   */
  public void sync(boolean show) {
    if (isVisible() && (shadow != null || shim != null)) {
      int w = getWidth();
      int h = getHeight();
      int l = getLeft();
      int t = getTop();
      if (shadow != null && !shadowDisabled) {
        if (show && !shadow.isVisible()) {
          shadow.show(dom);
        } else {
          shadow.sync(l, t, w, h);
        }
        if (shim != null) {
          if (show) {
            shim.setVisible(true);
          }
          Rectangle a = shadow.adjusts;
          if (a == null) a = new Rectangle(0, 0, 0, 0);
          if (a.x < 0 || a.y < 0) return;

          shim.setLeft(Math.min(l, l + a.x));
          shim.setTop(Math.min(t, t + a.y));
          shim.setWidth(w + a.width);
          shim.setHeight(w + a.height);
        }
      } else if (shim != null) {
        if (show) {
          shim.setVisible(true);
        }
        shim.setSize(w, h);
        shim.setLeftTop(l, t);
      }
    }
  }
  
  public void destroy() {
    hideUnders(true);
    if (shadow != null) {
      shadow.destroy();
    }
  }

  private void hideUnders(boolean hide) {
    hideShadow();
    hideShim();
  }

}
