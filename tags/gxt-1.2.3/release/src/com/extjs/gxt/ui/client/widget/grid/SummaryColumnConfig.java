/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.google.gwt.i18n.client.NumberFormat;

public class SummaryColumnConfig extends ColumnConfig {

  private SummaryType summaryType;
  private SummaryRenderer summaryRenderer;
  private NumberFormat summaryFormat;

  public SummaryColumnConfig() {
    super();
  }

  public NumberFormat getSummaryFormat() {
    return summaryFormat;
  }

  public void setSummaryFormat(NumberFormat summaryFormat) {
    this.summaryFormat = summaryFormat;
  }

  public SummaryColumnConfig(String id, String name, int width) {
    super(id, name, width);
  }

  public SummaryRenderer getSummaryRenderer() {
    return summaryRenderer;
  }

  public SummaryType getSummaryType() {
    return summaryType;
  }

  public void setSummaryRenderer(SummaryRenderer summaryRenderer) {
    this.summaryRenderer = summaryRenderer;
  }

  public void setSummaryType(SummaryType summaryType) {
    this.summaryType = summaryType;
  }

}
