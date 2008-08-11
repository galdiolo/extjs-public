/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;
import com.google.gwt.user.client.Event;

/**
 * Base class for any visual {@link Component} that uses a box container.
 * {@link BoxComponent} provides automatic box model adjustments for sizing and
 * positioning and will work correctly within the {@link Component} rendering
 * model.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Resize</b> : BoxComponentEvent(boxComponent, width, height)<br>
 * <div>Fires after the component is resized.</div>
 * <ul>
 * <li>boxComponent : this</li>
 * <li>width : the widget width</li>
 * <li>height : the widget height</li>
 * </ul>
 * 
 * <dd><b>Move</b> : BoxComponentEvent(boxComponent, x, y)<br>
 * <div>Fires after the component is moved.</div>
 * <ul>
 * <li>boxComponent : this</li>
 * <li>x : the new x position</li>
 * <li>y : the new y position</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class BoxComponent extends Component {

  /**
   * True to adjust sizes for box model issues to ensure actual size matches set
   * size.
   */
  protected boolean adjustSize = true;

  /**
   * True to enable a shim which uses a transparent iframe to stop content from
   * bleeding through.
   */
  protected boolean shim = false;

  /**
   * A specialzed El that provides support for a shadow and shim. Will be
   * created if either {@link #shadow} or {@link #shim} is set to true.
   */
  protected Layer layer;

  protected Size attachSize = new Size(-1, -1);
  protected Size lastSize;

  private boolean shadow = false;
  private boolean deferHeight;
  private boolean autoHeight;
  private boolean autoWidth;
  private String width, height;
  private int left = Style.DEFAULT, top = Style.DEFAULT;
  private int pageX = Style.DEFAULT, pageY = Style.DEFAULT;
  private boolean boxReady;

  /**
   * Gets the current box measurements of the component's underlying element.
   * The component must be attached to return page coordinates.
   * 
   * @param local if true the element's left and top are returned instead of
   *          page coordinates
   * @return the component's bounds
   */
  public Rectangle getBounds(boolean local) {
    return el().getBounds(local);
  }

  /**
   * Returns the component's offset height.
   * 
   * @return the height
   */
  public int getHeight() {
    return el().getHeight();
  }

  /**
   * Return the component's height.
   * 
   * @param content true to get the height minus borders and padding
   * @return the height
   */
  public int getHeight(boolean content) {
    return el().getHeight(content);
  }

  /**
   * Returns the component's current position. The component must be attached to
   * return page coordinates.
   * 
   * @param local true to return the element's left and top rather than page
   *          coordinates
   * @return the position
   */
  public Point getPosition(boolean local) {
    if (local) {
      return new Point(el().getLeft(true), el().getTop(true));
    }
    return el().getXY();
  }

  /**
   * Returns true if the shadow is enabled.
   * 
   * @return the shadow the shadow state
   */
  public boolean getShadow() {
    return shadow;
  }

  /**
   * Returns the component's size.
   * 
   * @return the size
   */
  public Size getSize() {
    return el().getSize();
  }

  /**
   * Returns the component's width.
   * 
   * @return the width
   */
  public int getWidth() {
    return getOffsetWidth();
  }

  /**
   * Returns the component's width.
   * 
   * @param content true to get width minus borders and padding
   * @return the width
   */
  public int getWidth(boolean content) {
    return el().getWidth(content);
  }

  /**
   * @return the autoHeight
   */
  public boolean isAutoHeight() {
    return autoHeight;
  }

  /**
   * @return the autoWidth
   */
  public boolean isAutoWidth() {
    return autoWidth;
  }

  /**
   * Returns true if the height is being deferred
   * 
   * @return the defer heigh state
   */
  public boolean isDeferHeight() {
    return deferHeight;
  }

  /**
   * Returns true if shimming is enabled.
   * 
   * @return the shim state
   */
  public boolean isShim() {
    return shim;
  }

  /**
   * Sets the component's auto height value (defaults to false).
   * 
   * @param autoHeight true to enable auto height
   */
  public void setAutoHeight(boolean autoHeight) {
    this.autoHeight = autoHeight;
  }

  /**
   * True to use width:'auto', false to use fixed width (defaults to false).
   * 
   * @param autoWidth the auto width state
   */
  public void setAutoWidth(boolean autoWidth) {
    this.autoWidth = autoWidth;
  }

  /**
   * Sets the component's size. This method fires the <i>Move</i> and <i>Resize</i>
   * events. element.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   */
  public void setBounds(int x, int y, int width, int height) {
    setPagePosition(x, y);
    setSize(width, height);
  }

  /**
   * Sets the component's size. This method fires the <i>Move</i> and <i>Resize</i>
   * events. element.
   * 
   * @param bounds the update box
   */
  public void setBounds(Rectangle bounds) {
    setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
  }

  /**
   * True to defer height calculations to an external component, false to allow
   * this component to set its own height (defaults to false).
   * 
   * @param deferHeight true to defer height
   */
  public void setDeferHeight(boolean deferHeight) {
    this.deferHeight = deferHeight;
  }

  /**
   * Sets the component's height. This method fires the <i>Resize</i> event.
   * element.
   * 
   * @param height the new height
   */
  public void setHeight(int height) {
    setSize(-1, height);
  }

  /**
   * Sets the height of the component. This method fires the <i>Resize</i>
   * event. element.
   * 
   * @param height the new height to set
   */
  public void setHeight(String height) {
    setSize(Style.UNDEFINED, height);
  }

  /**
   * Sets the page XY position of the component. To set the left and top
   * instead, use {@link #setPosition}. This method fires the <i>Move</i>
   * event.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void setPagePosition(int x, int y) {
    if (x != Style.DEFAULT) {
      pageX = x;
    }
    if (y != Style.DEFAULT) {
      pageY = y;
    }
    if (!boxReady) {
      return;
    }
    el().setXY(pageX, pageY);
    onPosition(pageX, pageY);
    if (hasListeners) {
      BoxComponentEvent ce = new BoxComponentEvent(this);
      ce.x = pageX;
      ce.y = pageY;
      fireEvent(Events.Move, ce);
    }
  }

  /**
   * Sets the page XY position of the component. To set the left and top
   * instead, use {@link #setPosition}. This method fires the <i>Move</i>
   * event.
   * 
   * @param point the new location
   */
  public void setPagePosition(Point point) {
    setPagePosition(point.x, point.y);
  }

  /**
   * Sets the width and height of the component. This method fires the resize
   * event.
   * 
   * @param width the new width to set
   * @param height the new height to set
   */
  public void setPixelSize(int width, int height) {
    setSize(width, height);
  }

  /**
   * Sets the left and top of the component. To set the page XY position
   * instead, use {@link #setPagePosition}. This method fires the move event.
   * 
   * @param left the new left
   * @param top the new top
   */
  public void setPosition(int left, int top) {
    this.left = left;
    this.top = top;

    if (!boxReady) {
      return;
    }

    Point adj = adjustPosition(new Point(left, top));
    int ax = adj.x, ay = adj.y;

    El pel = getPositionEl();

    if (ax != Style.DEFAULT || ay != Style.DEFAULT) {
      pel.makePositionable();
      if (ax != Style.DEFAULT && ay != Style.DEFAULT) {
        pel.setLeftTop(ax, ay);
      } else if (ax != Style.DEFAULT) {
        pel.setLeft(ax);
      } else if (ay != Style.DEFAULT) {
        pel.setTop(ay);
      }
      onPosition(ax, ay);
      BoxComponentEvent be = new BoxComponentEvent(this);
      be.x = ax;
      be.y = ay;
      fireEvent(Events.Move, be);
    }
  }

  /**
   * True to enable a shadow that will be displayed behind the component
   * (defaults to false).
   * 
   * @param shadow true to enable the shadow
   */
  public void setShadow(boolean shadow) {
    this.shadow = shadow;
  }

  /**
   * True to enable a shim which uses a transparent iframe to stop content from
   * bleeding through.
   * 
   * @param shim true to enable a shim
   */
  public void setShim(boolean shim) {
    this.shim = shim;
  }

  /**
   * Sets the width and height of the component. This method fires the <i>Resize</i>
   * event.
   * 
   * @param width the new width to set
   * @param height the new height to set
   */
  public void setSize(int width, int height) {
    if (width != Style.DEFAULT) {
      attachSize.width = width;
    }
    if (height != Style.DEFAULT) {
      attachSize.height = height;
    }

    if (!boxReady) {
      return;
    }

    if (autoWidth) {
      width = -1;
      setStyleAttribute("width", "auto");
    } else if (attachSize.width != Style.DEFAULT) {
      el().setWidth(attachSize.width, adjustSize);
    }
    if (autoHeight) {
      height = -1;
      setStyleAttribute("height", "auto");
    } else if (attachSize.height != Style.DEFAULT) {
      el().setHeight(attachSize.height, adjustSize);
    }

    onResize(width, height);

    if (hasListeners) {
      BoxComponentEvent ce = new BoxComponentEvent(this);
      ce.width = width;
      ce.height = height;
      fireEvent(Events.Resize, ce);
    }
  }

  /**
   * Sets the width and height of the component. This method fires the <i>Resize</i>
   * event.
   * 
   * @param width the new width to set
   * @param height the new height to set
   */
  public void setSize(String width, String height) {
    this.width = width;
    this.height = height;

    if (!boxReady) {
      return;
    }

    if (autoWidth) {
      el().setWidth("auto");
    } else if (!width.equals(Style.UNDEFINED)) {
      el().setWidth(width);
    }
    if (autoHeight) {
      el().setHeight("auto");
    } else if (!height.equals(Style.UNDEFINED)) {
      el().setHeight(height);
    }

    int w = autoWidth ? -1 : getOffsetWidth();
    int h = autoHeight ? -1 : getOffsetHeight();

    onResize(w, h);

    if (hasListeners) {
      fireEvent(Events.Resize, new BoxComponentEvent(this, w, h));
    }
  }

  /**
   * Sets the width of the component. This method fires the <i>Resize</i>
   * event.
   * 
   * @param width the new width to set
   */
  public void setWidth(int width) {
    setSize(width, -1);
  }

  /**
   * Sets the width of the component. This method fires the <i>Resize</i>
   * event.
   * 
   * @param width the new width to set
   */
  public void setWidth(String width) {
    setSize(width, Style.UNDEFINED);
  }

  protected void afterRender() {
    super.afterRender();
    boxReady = true;

    if (getShadow() || shim) {
      layer = new Layer(getElement());
      layer.enableShadow(getShadow());
      layer.enableShim();
      setEl(layer);
    }

    if (attachSize.width != -1 || attachSize.height != -1) {
      setSize(attachSize.width, attachSize.height);
    }

    if (width != null || height != null) {
      setSize(width, height);
    }

    if (left != Style.DEFAULT || top != Style.DEFAULT) {
      setPosition(left, top);
    }
    if (pageX != Style.DEFAULT || pageY != Style.DEFAULT) {
      setPagePosition(pageX, pageY);
    }

    if (layer != null) {
      layer.sync(true);
    }

  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    BoxComponentEvent e = new BoxComponentEvent(this);
    e.event = event;
    return e;
  }

  /**
   * Returns the element to be used when positioning the component. Subclasses
   * may override as needed. Default method returns the component's root
   * element.
   * 
   * @return the position element
   */
  protected El getPositionEl() {
    return el();
  }

  /**
   * Returns the element to be used when resizing the component. Subclasses may
   * override as needed. Default method returns the component's root element.
   * 
   * @return the resize element
   */
  protected El getResizeEl() {
    return el();
  }

  /**
   * Called after the component is moved, this method is empty by default but
   * can be implemented by any subclass that needs to perform custom logic after
   * a move occurs.
   * 
   * @param x the new x position
   * @param y the new y position
   */
  protected void onPosition(int x, int y) {

  }

  /**
   * Called after the component is resized, this method is empty by default but
   * can be implemented by any subclass that needs to perform custom logic after
   * a resize occurs.
   * 
   * @param width the width
   * @param height the height
   */
  protected void onResize(int width, int height) {

  }

  private Point adjustPosition(Point point) {
    return point;
  }

}
