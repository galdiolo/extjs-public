/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.model.GapData;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Line Gap Chart", icon = "linegapchart", category = "Charts", classes = GapData.class)
public class LineGapExample implements IsWidget, EntryPoint {

  public interface GapDataPropertyAccess extends PropertyAccess<GapData> {
    ValueProvider<GapData, Double> gapless();

    ValueProvider<GapData, Double> gapped();

    ValueProvider<GapData, String> name();

    @Path("name")
    ModelKeyProvider<GapData> nameKey();
  }

  private static final GapDataPropertyAccess GapDataAccess = GWT.create(GapDataPropertyAccess.class);
  private static final String[] monthsFull = new String[] {
      "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
      "December"};

  public Widget asWidget() {
    final ListStore<GapData> store = new ListStore<GapData>(GapDataAccess.nameKey());
    setupStore(store);

    final Chart<GapData> chart = new Chart<GapData>();
    chart.setStore(store);
    chart.setShadowChart(true);

    NumericAxis<GapData> axis = new NumericAxis<GapData>();
    axis.setPosition(Position.LEFT);
    axis.addField(GapDataAccess.gapless());
    axis.addField(GapDataAccess.gapped());
    TextSprite title = new TextSprite("Number of Hits");
    title.setFontSize(18);
    axis.setTitleConfig(title);
    axis.setMinorTickSteps(1);
    axis.setDisplayGrid(true);
    PathSprite odd = new PathSprite();
    odd.setOpacity(1);
    odd.setFill(new Color("#ddd"));
    odd.setStroke(new Color("#bbb"));
    odd.setStrokeWidth(0.5);
    axis.setGridOddConfig(odd);
    axis.setMinimum(0);
    axis.setMaximum(100);
    chart.addAxis(axis);

    CategoryAxis<GapData, String> catAxis = new CategoryAxis<GapData, String>();
    catAxis.setPosition(Position.BOTTOM);
    catAxis.setField(GapDataAccess.name());
    title = new TextSprite("Month of the Year");
    title.setFontSize(18);
    catAxis.setTitleConfig(title);
    catAxis.setLabelProvider(new LabelProvider<String>() {
      @Override
      public String getLabel(String item) {
        return item.substring(0, 3);
      }
    });
    chart.addAxis(catAxis);

    final LineSeries<GapData> series = new LineSeries<GapData>();
    series.setLegendTitle("Gapless");
    series.setYAxisPosition(Position.LEFT);
    series.setYField(GapDataAccess.gapless());
    series.setStroke(new RGB(194, 0, 36));
    series.setShowMarkers(true);
    Sprite marker = Primitives.square(0, 0, 6);
    marker.setFill(new RGB(194, 0, 36));
    series.setMarkerConfig(marker);
    series.setHighlighting(true);
    chart.addSeries(series);

    final LineSeries<GapData> series2 = new LineSeries<GapData>();
    series2.setLegendTitle("Gapped");
    series2.setGapless(false);
    series2.setYAxisPosition(Position.LEFT);
    series2.setYField(GapDataAccess.gapped());
    series2.setStroke(new RGB(240, 165, 10));
    series2.setShowMarkers(true);
    series2.setSmooth(true);
    marker = Primitives.circle(0, 0, 6);
    marker.setFill(new RGB(240, 165, 10));
    series2.setMarkerConfig(marker);
    series2.setHighlighting(true);
    chart.addSeries(series2);

    final Legend<GapData> legend = new Legend<GapData>();
    legend.setPosition(Position.RIGHT);
    legend.setItemHighlighting(true);
    legend.setItemHiding(true);
    chart.setLegend(legend);

    TextButton regenerate = new TextButton("Reload Data");
    regenerate.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        setupStore(store);
        chart.redrawChart();
      }
    });
    ToggleButton animation = new ToggleButton("Animate");
    animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setAnimated(event.getValue());
      }
    });
    animation.setValue(true, true);
    ToggleButton shadow = new ToggleButton("Shadow");
    shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setShadowChart(event.getValue());
        chart.redrawChart();
      }
    });
    shadow.setValue(true);

    ToolBar toolBar = new ToolBar();
    toolBar.add(regenerate);
    toolBar.add(animation);
    toolBar.add(shadow);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Gap Chart");
    panel.setPixelSize(620, 500);
    panel.setBodyBorder(true);

    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);

    final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
    resize.setMinHeight(400);
    resize.setMinWidth(400);
    
    panel.addExpandHandler(new ExpandHandler() {
      @Override
      public void onExpand(ExpandEvent event) {
        resize.setEnabled(true);
      }
    });
    panel.addCollapseHandler(new CollapseHandler() {
      @Override
      public void onCollapse(CollapseEvent event) {
        resize.setEnabled(false);
      }
    });
    new Draggable(panel, panel.getHeader()).setUseProxy(false);

    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    layout.add(toolBar);

    chart.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(chart);

    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  public void setupStore(ListStore<GapData> store) {
    store.clear();
    for (int i = 0; i < 12; i++) {
      if (i % 4 != 0) {
        store.add(new GapData(monthsFull[i % 12], Math.random() * 100, Math.random() * 100));
      } else {
        store.add(new GapData(monthsFull[i % 12], Double.NaN, Double.NaN));
      }
    }
  }
}
