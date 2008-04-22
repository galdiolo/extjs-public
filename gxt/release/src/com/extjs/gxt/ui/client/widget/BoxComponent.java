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
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;

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
   * True to use height:'auto', false to use fixed height (defaults to false).
   */
  public boolean autoHeight;

  /**
   * True to use width:'auto', false to use fixed width (defaults to false).
   */
  public boolean autoWidth;

  /**
   * True to defer height calculations to an external component, false to allow
   * this component to set its own height (defaults to false).
   */
  public boolean deferHeight;

  /**
   * True to enable a shadow that will be displayed behind the component
   * (defaults to false).
   */
  public boolean shadow = false;

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

  private String width, height;
  private int left = Style.DEFAULT, top = Style.DEFAULT;
  private int pageX = Style.DEFAULT, pageY = Style.DEFAULT;
  protected Size attachSize = new Size(-1, -1);
  private boolean boxReady;

  /**
   * Gets the current box measurements of the component's underlying element.
   * The component must be attached to return page coordinates.
   * 
   * @param local if true the element's left and top are returned instead of
   *            page coordinates
   * @return the component's bounds
   */
  public Rectangle getBounds(boolean local) {
    return el.getBounds(local);
  }

  /**
   * Returns the component's current position. The component must be attached to
   * return page coordinates.
   * 
   * @param local true to return the element's left and top rather than page
   *            coordinates
   * @return the position
   */
  public Point getPosition(boolean local) {
    if (local) {
      return new Point(el.getLeft(true), el.getTop(true));
    }
    return el.getXY();
  }

  /**
   * Returns the component's size.
   * 
   * @return the size
   */
  public Size getSize() {
    return el.getSize();
  }

  /**
   * Returns the component's offset height.
   * 
   * @return the height
   */
  public int getHeight() {
    return el.getHeight();
  }

  /**
   * Return the component's height.
   * 
   * @param content true to get the height minus borders and padding
   * @return the height
   */
  public int getHeight(boolean content) {
    return el.getHeight(content);
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
    return el.getWidth(content);
  }

  public void recalculate() {
    onResize(getOffsetWidth(), getOffsetHeight());
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
   * @param point the new location
   */
  public void setPagePosition(Point point) {
    setPagePosition(point.x, point.y);
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
    el.setXY(pageX, pageY);
    onPosition(pageX, pageY);
    BoxComponentEvent ce = new BoxComponentEvent(this);
    ce.x = pageX;
    ce.y = pageY;
    fireEvent(Events.Move, ce);
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
   * Sets the width and height of the component. This method fires the <i>Resize</i>
   * event.
   * 
   * @param width the new width to set
   * @param height the new height to set
   */
  public void setSize(int width, int height) {
    if (width != Style.DEFAULT) {
      autoWidth = false;
      attachSize.width = width;
    }
    if (height != Style.DEFAULT) {
      autoHeight = false;
      attachSize.height = height;
    }

    if (!boxReady) {
      return;
    }

    if (autoWidth) {
      setStyleAttribute("width", "auto");
    } else if (attachSize.width != Style.DEFAULT) {
      el.setWidth(attachSize.width, adjustSize);
    }
    if (autoHeight) {
      setStyleAttribute("height", "auto");
    } else if (attachSize.height != Style.DEFAULT) {
      el.setHeight(attachSize.height, adjustSize);
    }
    
    onResize(width, height);
    
    BoxComponentEvent ce = new BoxComponentEvent(this);
    ce.width = width;
    ce.height = height;
    fireEvent(Events.Resize, ce);
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
      el.setWidth("auto");
    } else if (!width.equals(Style.UNDEFINED)) {
      el.setWidth(width);
    }
    if (autoHeight) {
      el.setHeight("auto");
    } else if (!height.equals(Style.UNDEFINED)) {
      el.setHeight(height);
    }

    onResize(getOffsetWidth(), getOffsetHeight());
    BoxComponentEvent be = new BoxComponentEvent(this);
    be.width = getOffsetWidth();
    be.height = getOffsetHeight();
    fireEvent(Events.Resize, be);
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

    if (shadow || shim) {
      layer = new Layer(getElement());
      layer.enableShadow(shadow);
      layer.enableShim();
      el = layer;
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

  /**
   * Returns the element to be used when positioning the component. Subclasses
   * may override as needed. Default method returns the component's root
   * element.
   * 
   * @return the position element
   */
  protected El getPositionEl() {
    return el;
  }

  /**
   * Returns the element to be used when resizing the component. Subclasses may
   * override as needed. Default method returns the component's root element.
   * 
   * @return the resize element
   */
  protected El getResizeEl() {
    return el;
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
