/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.SplitBar;


/**
 * Layout data describing a "region" in a border panel.
 */
public class BorderLayoutData {

  /**
   * The region the mapped content panel will be placed.
   */
  public LayoutRegion region;

  /**
   * When a collapsed region's bar is clicked, the region's panel will be
   * displayed as a floated panel that will close again once the user mouses out
   * of that panel (or clicks out if autoHide = false). Setting animFloat to
   * false will prevent the open and close of these floated panels from being
   * animated (defaults to true).
   */
  public boolean animFloat;

  /**
   * When a collapsed region's bar is clicked, the region's panel will be
   * displayed as a floated panel. If autoHide is true, the panel will
   * automatically hide after the user mouses out of the panel. If autoHide is
   * false, the panel will continue to display until the user clicks outside of
   * the panel (defaults to true).
   */
  public boolean autoHide;

  /**
   * By default, collapsible regions are collapsed by clicking the
   * expand/collapse tool button that renders into the region's title bar.
   * Optionally, when collapseMode is set to 'mini' the region's split bar will
   * also display a small collapse button in the center of the bar. In 'mini'
   * mode the region will collapse to a thinner bar than in normal mode. By
   * default collapseMode is undefined, and the only two supported values are
   * undefined and 'mini'. Note that if a collapsible region does not have a
   * title bar, then collapseMode must be set to 'mini' in order for the region
   * to be collapsible by the user as the tool button will not be rendered.
   */
  public String collapseMode;

  /**
   * True to allow the user to collapse this region (defaults to false). If
   * true, an expand/collapse tool button will automatically be rendered into
   * the title bar of the region, otherwise the button will not be shown. Note
   * that a title bar is required to display the toggle button -- if no region
   * title is specified, the region will only be collapsible if
   * {@link #collapseMode} is set to 'mini'.
   */
  public boolean collapsible;

  /**
   * An object containing margins to apply to the region.
   */
  public Margins margins;

  /**
   * An object containing margins to apply to the region's collapsed element.
   */
  public Margins cmargins;

  /**
   * True to display a {@link SplitBar} between this region and its
   * neighbor, allowing the user to resize the regions dynamically (defaults to
   * false). When split = true, it is common to specify a {@link #minSize} and
   * {@link #maxSize} for the region.
   */
  public boolean split;

  /**
   * True to allow clicking a collapsed region's bar to display the region's
   * panel floated above the layout, false to force the user to fully expand a
   * collapsed region by clicking the expand button to see it again (defaults to
   * true).
   */
  public boolean floatable = true;

  /**
   * The minimum allowable size in pixels for this region (defaults to 50).
   */
  public int minSize = 50;

  /**
   * The maximum allowable size in pixels for this region (defaults to 500).
   */
  public int maxSize = 500;

  /**
   * The region's preferred size. Values of 1 or less are treated as
   * percentages (defaults to 200).
   */
  public float size = 200;

  /**
   * True to hide the region (defauls to false).
   */
  public boolean exclude;

  Margins defaultMargins = new Margins(0, 0, 0, 0);
  Margins defaultNSMargins = new Margins(5, 5, 5, 5);
  Margins defaultEWMargins = new Margins(5, 0, 5, 0);
  
  /**
   * Creates a new border layout data.
   * 
   * @param region the region
   */
  public BorderLayoutData(LayoutRegion region) {
    this.region = region;
  }

  /**
   * Creates a new border layout data.
   * 
   * @param region the region
   * @param size the region's size
   */
  public BorderLayoutData(LayoutRegion region, float size) {
    this.region = region;
    this.size = size;
  }
  
  public BorderLayoutData(LayoutRegion region, float size, int min, int max) {
    this(region, size);
    this.minSize = min;
    this.maxSize = max;
    split = true;
  }

}
