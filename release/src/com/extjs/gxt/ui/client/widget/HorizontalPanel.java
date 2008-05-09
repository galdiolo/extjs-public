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
import com.extjs.gxt.ui.client.widget.layout.TableRowLayout;
import com.google.gwt.user.client.Element;

/**
 * A {@link Container} that lays out its children in a single row using a
 * {@link TableRowLayout}. Each child widget can control its cell's properties
 * using an associated {@link TableData} instance.
 * 
 * <pre><code>
 * HoriztonalPanel hp = new HorizontalPanel();
 * 
 * hp.add(new Label("Aligned Center"));
 * hp.add(new Label("Aligned Right"), new TableData("right", "middle");
 * </pre></code>
 */
public class HorizontalPanel extends Container {

  private String tableWidth;
  private String tableHeight;
  private HorizontalAlignment horizontalAlign = HorizontalAlignment.LEFT;
  private VerticalAlignment verticalAlign = VerticalAlignment.TOP;
  private int spacing = 0;
  private TableRowLayout layout;

  public HorizontalPanel() {
    setMonitorResize(true);
  }

  /**
   * @return the horizontalAlign
   */
  public HorizontalAlignment getHorizontalAlign() {
    return horizontalAlign;
  }

  /**
   * Returns the table's height.
   * 
   * @return the table height
   */
  public String getTableHeight() {
    return tableHeight;
  }

  /**
   * @return the tableWidth
   */
  public String getTableWidth() {
    return tableWidth;
  }

  /**
   * Returns the panel's vertical alignment.
   * 
   * @return the vertical aignment
   */
  public VerticalAlignment getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * The horizontal cell alignment (defaults to LEFT).
   * 
   * @param horizontalAlign horizontal alignment
   */
  public void setHorizontalAlign(HorizontalAlignment horizontalAlign) {
    this.horizontalAlign = horizontalAlign;
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

  /**
   * Sets the table's height.
   * 
   * @param tableHeight the table height
   */
  public void setTableHeight(String tableHeight) {
    this.tableHeight = tableHeight;
  }

  /**
   * Sets the width of the internal table.
   * 
   * @param tableWidth the table width
   */
  public void setTableWidth(String tableWidth) {
    this.tableWidth = tableWidth;
  }

  /**
   * Sets the panel' vertical alignment.
   * 
   * @param verticalAlign the vertical alignment
   */
  public void setVerticalAlign(VerticalAlignment verticalAlign) {
    this.verticalAlign = verticalAlign;
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    setStyleAttribute("overflow", "visible");
    layout = new TableRowLayout();
    layout.horizontalAlign = getHorizontalAlign();
    layout.verticalAlign = getVerticalAlign();
    layout.cellSpacing = spacing;
    layout.cellPadding = 0;
    layout.width = getTableWidth();
    layout.height = getTableHeight();
    setLayout(layout);
    layout();
  }

}
