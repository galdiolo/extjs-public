/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;


import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;

/**
 * A {@link Container} that lays out its children in a single column using a
 * {@link TableLayout}. Each child widget can control its cell's properties
 * using an associated {@link TableData} instance.
 */
public class VerticalPanel extends Container {

  /**
   * The horizontal cell alignment (defaults to LEFT).
   * <p>
   * Valid values are:
   * <ul>
   * <li>HorizontalAlignment.LEFT</li>
   * <li>HorizontalAlignment.CENTER</li>
   * <li>HorizontalAlignment.RIGHT</li>
   * </ul>
   * </p>
   */
  public HorizontalAlignment horizontalAlign = HorizontalAlignment.LEFT;

  /**
   * The vertical cell alignment (defaults to TOP).
   * <p>
   * Valid values are:
   * <ul>
   * <li>VerticalAlignment.TOP</li>
   * <li>VerticalAlignment.MIDDLE</li>
   * <li>VertcialAlginment.BOTTOM</li>
   * </ul>
   * </p>
   */
  public VerticalAlignment verticalAlign = VerticalAlignment.TOP;

  /**
   * The cell spacing in pixels (defaults to 0).
   */
  public int spacing = 0;

  /**
   * The width of the table (defaults to null).
   */
  public String tableWidth;

  /**
   * The height of the table (defaults to null).
   */
  public String tableHeight;

  private TableLayout layout;

  /**
   * Creates a new vertical panel.
   */
  public VerticalPanel() {
    setMonitorResize(false);
  }

  /**
   * Sets the panel's spacing.
   * 
   * @param spacing the spacing
   */
  public void setSpacing(int spacing) {
    this.spacing = spacing;
    if (layout != null) {
      layout.setCellSpacing(spacing);
    }
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    setStyleAttribute("overflow", "visible");
    layout = new TableLayout(1);
    layout.horizontalAlign = horizontalAlign;
    layout.verticalAlign = verticalAlign;
    layout.cellSpacing = spacing;
    layout.cellPadding = 0;
    layout.width = tableWidth;
    layout.height = tableHeight;
    setLayout(layout);
    layout();
  }
}
