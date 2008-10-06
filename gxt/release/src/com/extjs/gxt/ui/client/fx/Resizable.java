/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.fx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ResizeEvent;
import com.extjs.gxt.ui.client.event.ResizeListener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Applies drag handles to a widget to make it resizable. The drag handles are
 * inserted into the widget and positioned absolute.
 * <p>
 * Here is the list of valid resize handles:
 * </p>
 * 
 * <pre>
 * Value   Description
 * ------  -------------------
 * 'n'     north
 * 's'     south
 * 'e'     east
 * 'w'     west
 * 'nw'    northwest
 * 'sw'    southwest
 * 'se'    southeast
 * 'ne'    northeast
 * 'all'   all
 * </pre>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>ResizeStart</b> : (source, widget, event) <br>
 * Fires before a resize operation start. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.
 * <ul>
 * <li>source : this</li>
 * <li>component : resize widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ResizeEnd</b> : (source, widget, event)<br>
 * Fires after a resize.
 * <ul>
 * <li>source : this</li>
 * <li>widget : resize widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Resizable extends BaseObservable {

  protected enum Dir {
    N, NE, E, SE, S, SW, W, NW;
  }

  private class ResizeHandle extends Component {

    public Dir dir;

    public void onBrowserEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          DOM.eventCancelBubble(event, true);
          DOM.eventPreventDefault(event);
          handleMouseDown(event, this);
          break;
      }
    }

    @Override
    protected void onRender(Element target, int index) {
      super.onRender(target, index);
      setElement(DOM.createDiv(), target, index);
      sinkEvents(Event.MOUSEEVENTS);
    }
  }

  /**
   * The minumum width for the widget (defaults to 50).
   */
  public int minWidth = 50;

  /**
   * The maximum width for the widget (defaults to 2000).
   */
  public int maxWidth = 2000;

  /**
   * The minimum height for the widget (defaults to 50).
   */
  public int minHeight = 50;

  /**
   * The maximum height for the widget (defaults 2000).
   */
  public int maxHeight = 2000;

  /**
   * <code>true</code> to preserve the original ratio between height and width
   * during resize. Default value is <code>false</code>.
   */
  public boolean preserveRatio = false;

  /**
   * The style name used for proxy drags (defaults to 'x-resizable-proxy').
   */
  public String proxyStyle = "x-resizable-proxy";

  /**
   * True to resize the widget directly instead of using a proxy (defaults to
   * false).
   */
  public boolean dynamic;
  private BoxComponent resize;
  private List<ResizeHandle> handleList;
  private boolean enabled = true;
  private boolean dragging;
  private El proxyEl, dragEl;
  private Dir dir;
  private EventPreview preview;
  private Listener listener;
  private String handles;
  private Rectangle startBox;
  private Point startPoint;

  /**
   * Creates a new resizable instance with 8-way resizing.
   * 
   * @param resize the resize widget
   */
  public Resizable(BoxComponent resize) {
    this(resize, "all");
  }

  /**
   * Creates a new resizable instance.
   * 
   * @param resize the resize widget
   * @param handles the resize handle locations seperated by spaces
   */
  public Resizable(final BoxComponent resize, String handles) {
    this.resize = resize;
    this.handles = handles;

    listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        switch (ce.type) {
          case Events.Render:
            init();
            break;
          case Events.Attach:
            onAttach();
            break;
          case Events.Detach:
            onDetach();
            break;
        }
      }
    };

    resize.addListener(Events.Render, listener);
    resize.addListener(Events.Attach, listener);
    resize.addListener(Events.Detach, listener);

    if (resize.isRendered()) {
      init();
    }

    if (resize.isAttached()) {
      onAttach();
    }

  }

  /**
   * Adds a resize listener.
   * 
   * @param listener the listener
   */
  public void addResizeListener(ResizeListener listener) {
    addListener(Events.ResizeStart, listener);
    addListener(Events.ResizeEnd, listener);
  }

  /**
   * Returns <code>true</code> if if resizing.
   * 
   * @return the resize state
   */
  public boolean isResizing() {
    return dragging;
  }

  /**
   * Removes the drag handles.
   */
  public void release() {
    onDetach();

    resize.removeListener(Events.Attach, listener);
    resize.removeListener(Events.Detach, listener);

    Iterator<ResizeHandle> iter = handleList.iterator();
    while (iter.hasNext()) {
      ResizeHandle handle = iter.next();
      iter.remove();
      DOM.removeChild(resize.getElement(), handle.getElement());
    }
  }
  
  /**
   * Removes a resize listener.
   * 
   * @param listener the listener
   */
  public void removeResizeListener(ResizeListener listener) {
    removeListener(Events.ResizeStart, listener);
    removeListener(Events.ResizeEnd, listener);
  }
  
  /**
   * Enables or disables the drag handles.
   * 
   * @param enable <code>true</code> to enable
   */
  public void setEnabled(boolean enable) {
    for (ResizeHandle handle : handleList) {
      El.fly(handle.getElement()).setVisibility(enable);
    }
  }

  protected Element createProxy() {
    Element elem = DOM.createDiv();
    El.fly(elem).setStyleName(proxyStyle, true);
    El.fly(elem).disableTextSelection(true);
    return elem;
  }

  protected void init() {
    resize.el().makePositionable();
    if (handleList == null) {
      handleList = new ArrayList<ResizeHandle>();
      if (handles.equals("all")) {
        handles = "n s e w ne nw se sw";
      }
      String[] temp = handles.split(" ");
      for (int i = 0; i < temp.length; i++) {
        if ("n".equals(temp[i])) {
          create(Dir.N, "north");
        } else if ("nw".equals(temp[i])) {
          create(Dir.NW, "northwest");
        } else if ("e".equals(temp[i])) {
          create(Dir.E, "east");
        } else if ("w".equals(temp[i])) {
          create(Dir.W, "west");
        } else if ("se".equals(temp[i])) {
          create(Dir.SE, "southeast");
        } else if ("s".equals(temp[i])) {
          create(Dir.S, "south");
        } else if ("ne".equals(temp[i])) {
          create(Dir.NE, "northeast");
        } else if ("sw".equals(temp[i])) {
          create(Dir.SW, "southwest");
        }
      }

      preview = new EventPreview() {
        public boolean onEventPreview(Event event) {
          switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEMOVE:
              int x = DOM.eventGetClientX(event);
              int y = DOM.eventGetClientY(event);
              handleMouseMove(x, y);
              break;
            case Event.ONMOUSEUP:
              handleMouseUp(event);
              break;
          }
          return false;
        }
      };

    }

  }

  protected void onAttach() {
    for (ResizeHandle handle : handleList) {
      ComponentHelper.doAttach(handle);
    }
  }

  protected void onDetach() {
    for (ResizeHandle handle : handleList) {
      ComponentHelper.doDetach(handle);
    }
  }

  private int constrain(int v, int diff, int m, int mx) {
    if (v - diff < m) {
      diff = v - m;
    } else if (v - diff > mx) {
      diff = mx - v;
    }
    return diff;
  }

  private ResizeHandle create(Dir dir, String cls) {
    ResizeHandle rh = new ResizeHandle();
    rh.setStyleName("x-resizable-handle " + "x-resizable-handle-" + cls);
    rh.dir = dir;
    rh.render(resize.getElement());
    handleList.add(rh);
    return rh;
  }

  private void handleMouseDown(Event event, ResizeHandle handle) {
    if (!enabled) {
      return;
    }

    if (!fireEvent(Events.ResizeStart, new ResizeEvent(this, resize, event))) {
      return;
    }

    dir = handle.dir;

    startBox = resize.getBounds(false);
    int x = DOM.eventGetClientX(event);
    int y = DOM.eventGetClientY(event);
    startPoint = new Point(x, y);

    dragging = true;

    if (!dynamic) {
      if (proxyEl == null) {
        proxyEl = new El(DOM.createDiv());
        proxyEl.setStyleName(proxyStyle, true);
        proxyEl.disableTextSelection(true);
        Element body = RootPanel.getBodyElement();
        DOM.appendChild(body, proxyEl.dom);
      }

      proxyEl.makePositionable(true);
      proxyEl.setLeft(startBox.x).setTop(startBox.y);
      proxyEl.setSize(startBox.width, startBox.height);
      proxyEl.setVisible(true);
      dragEl = proxyEl;
    } else {
      dragEl = new El(resize.getElement());
    }
    DOM.addEventPreview(preview);
  }

  private void handleMouseMove(int xin, int yin) {
    if (dragging) {
      int x = startBox.x;
      int y = startBox.y;
      float w = startBox.width;
      float h = startBox.height;
      float ow = w, oh = h;
      int mw = minWidth;
      int mh = minHeight;
      int mxw = maxWidth;
      int mxh = maxHeight;

      Point eventXY = new Point(xin, yin);

      int diffX = -(startPoint.x - Math.max(2, eventXY.x));
      int diffY = -(startPoint.y - Math.max(2, eventXY.y));

      switch (dir) {
        case E:
          w += diffX;
          w = Math.min(Math.max(mw, w), mxw);
          break;
        case S:
          h += diffY;
          h = Math.min(Math.max(mh, h), mxh);
          break;
        case SE:
          w += diffX;
          h += diffY;
          w = Math.min(Math.max(mw, w), mxw);
          h = Math.min(Math.max(mh, h), mxh);
          break;
        case N:
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          break;
        case W:
          diffX = constrain((int) w, diffX, mw, mxw);
          x += diffX;
          w -= diffX;
          break;
        case NE:
          w += diffX;
          w = Math.min(Math.max(mw, w), mxw);
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          break;
        case NW:
          diffX = constrain((int) w, diffX, mw, mxw);
          diffY = constrain((int) h, diffY, mh, mxh);
          y += diffY;
          h -= diffY;
          x += diffX;
          w -= diffX;
          break;
        case SW:
          diffX = constrain((int) w, diffX, mw, mxw);
          h += diffY;
          h = Math.min(Math.max(mh, h), mxh);
          x += diffX;
          w -= diffX;
          break;
      }
      if (preserveRatio) {
        switch (dir) {
          case SE:
          case E:
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            w = ow * (h / oh);
            break;
          case S:
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            break;
          case NE:
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            break;
          case N: {
            float tw = w;
            w = ow * (h / oh);
            w = Math.min(Math.max(mw, w), mxw);
            h = oh * (w / ow);
            x += (tw - w) / 2;
          }
            break;
          case SW: {
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            float tw = w;
            w = ow * (h / oh);
            x += tw - w;
            break;
          }
          case W: {
            float th = h;
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            y += (th - h) / 2;
            float tw = w;
            w = ow * (h / oh);
            x += tw - w;
            break;
          }
          case NW: {
            float tw = w;
            float th = h;
            h = oh * (w / ow);
            h = Math.min(Math.max(mh, h), mxh);
            w = ow * (h / oh);
            y += th - h;
            x += tw - w;
            break;
          }
        }
      }
      dragEl.setBounds(x, y, (int) w, (int) h);
    }
  }

  private void handleMouseUp(Event event) {
    dragging = false;
    DOM.removeEventPreview(preview);
    Rectangle rect = dragEl.getBounds();

    rect.width = Math.min(rect.width, maxWidth);
    rect.height = Math.min(rect.height, maxHeight);

    if (proxyEl != null) {
      proxyEl.disableTextSelection(false);
    }

    resize.setBounds(rect);

    dragEl.setVisible(false);
    proxyEl.setVisible(false);

    ResizeEvent ce = new ResizeEvent(this);
    ce.component = resize;
    ce.event = event;
    fireEvent(Events.ResizeEnd, ce);
  }

}
