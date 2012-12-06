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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.ScatterSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Data;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Filter Chart", icon = "filterchart", category = "Charts", classes = Data.class)
public class FilterChartExample implements IsWidget, EntryPoint {

  public interface DataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<Data, Double> data1();

    ValueProvider<Data, Double> data2();

    ValueProvider<Data, Double> data3();

    ValueProvider<Data, Double> data4();

    ValueProvider<Data, Double> data5();

    ValueProvider<Data, Double> data6();

    ValueProvider<Data, String> name();

    @Path("name")
    ModelKeyProvider<Data> nameKey();
  }

  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);

  private final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
  private final Chart<Data> chart = new Chart<Data>();

  private final DoublePropertyEditor editor = new DoublePropertyEditor();
  private final SpinnerField<Double> yMinField = new SpinnerField<Double>(editor);
  private final SpinnerField<Double> yMaxField = new SpinnerField<Double>(editor);
  private final SpinnerField<Double> xMinField = new SpinnerField<Double>(editor);
  private final SpinnerField<Double> xMaxField = new SpinnerField<Double>(editor);

  public Widget asWidget() {
    store.addAll(TestData.getData(50, 0, 100));
    store.setEnableFilters(true);

    chart.setStore(store);
    chart.setShadowChart(true);
    chart.setAnimated(true);
    chart.setDefaultInsets(10);

    NumericAxis<Data> axis = new NumericAxis<Data>();
    axis.setPosition(Position.LEFT);
    axis.addField(dataAccess.data1());
    axis.addField(dataAccess.data2());
    axis.addField(dataAccess.data3());
    chart.addAxis(axis);

    NumericAxis<Data> axis2 = new NumericAxis<Data>();
    axis2.setPosition(Position.BOTTOM);
    axis2.addField(dataAccess.data4());
    axis2.addField(dataAccess.data5());
    axis2.addField(dataAccess.data6());
    chart.addAxis(axis2);

    final ScatterSeries<Data> series = new ScatterSeries<Data>();
    series.setYAxisPosition(Position.LEFT);
    series.setYField(dataAccess.data1());
    series.setXAxisPosition(Position.BOTTOM);
    series.setXField(dataAccess.data4());
    Sprite marker = Primitives.cross(0, 0, 8);
    marker.setFill(RGB.GREEN);
    series.setMarkerConfig(marker);
    series.setHighlighting(true);
    chart.addSeries(series);

    final ScatterSeries<Data> series2 = new ScatterSeries<Data>();
    series2.setYAxisPosition(Position.LEFT);
    series2.setYField(dataAccess.data2());
    series2.setXAxisPosition(Position.BOTTOM);
    series2.setXField(dataAccess.data5());
    marker = Primitives.circle(0, 0, 8);
    marker.setFill(RGB.RED);
    series2.setMarkerConfig(marker);
    series2.setHighlighting(true);
    chart.addSeries(series2);

    final ScatterSeries<Data> series3 = new ScatterSeries<Data>();
    series3.setYAxisPosition(Position.LEFT);
    series3.setYField(dataAccess.data3());
    series3.setXAxisPosition(Position.BOTTOM);
    series3.setXField(dataAccess.data6());
    marker = Primitives.plus(0, 0, 8);
    marker.setFill(RGB.BLUE);
    series3.setMarkerConfig(marker);
    series3.setHighlighting(true);
    chart.addSeries(series3);

    TextButton regenerate = new TextButton("Reload Data");
    regenerate.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        store.clear();
        store.addAll(TestData.getData(50, 0, 100));
        chart.redrawChart();
      }
    });

    final StoreFilter<Data> storeFilter = new StoreFilter<Data>() {
      @Override
      public boolean select(Store<Data> store, Data parent, Data item) {
        Double yMin = yMinField.getValue();
        if (yMin == null) {
          // if field value is not valid use not a number
          yMin = Double.NaN;
        }
        Double yMax = yMaxField.getValue();
        if (yMax == null) {
          yMax = Double.NaN;
        }

        double data1 = dataAccess.data1().getValue(item);
        if (data1 < yMin || data1 > yMax) {
          return false;
        }

        double data2 = dataAccess.data2().getValue(item);
        if (data2 < yMin || data2 > yMax) {
          return false;
        }

        double data3 = dataAccess.data3().getValue(item);
        if (data3 < yMin || data3 > yMax) {
          return false;
        }

        Double xMin = xMinField.getValue();
        if (xMin == null) {
          xMin = Double.NaN;
        }
        Double xMax = xMaxField.getValue();
        if (xMax == null) {
          xMax = Double.NaN;
        }

        double data4 = dataAccess.data4().getValue(item);
        if (data4 < xMin || data4 > xMax) {
          return false;
        }

        double data5 = dataAccess.data5().getValue(item);
        if (data5 < xMin || data5 > xMax) {
          return false;
        }

        double data6 = dataAccess.data6().getValue(item);
        if (data6 < xMin || data6 > xMax) {
          return false;
        }

        return true;
      }
    };

    ValueChangeHandler<Double> valueChangeHandler = new ValueChangeHandler<Double>() {
      @Override
      public void onValueChange(ValueChangeEvent<Double> event) {
        store.getFilters().clear();
        store.addFilter(storeFilter);
        chart.redrawChart();
      }
    };

    yMinField.setIncrement(1d);
    yMinField.setMinValue(0d);
    yMinField.setMaxValue(100d);
    yMinField.setValue(0d);
    yMinField.setAllowBlank(false);
    yMinField.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
    yMinField.addValueChangeHandler(valueChangeHandler);
    yMinField.setWidth(70);

    yMaxField.setIncrement(1d);
    yMaxField.setMinValue(0d);
    yMaxField.setMaxValue(100d);
    yMaxField.setValue(100d);
    yMaxField.setAllowBlank(false);
    yMaxField.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
    yMaxField.addValueChangeHandler(valueChangeHandler);
    yMaxField.setWidth(70);

    xMinField.setIncrement(1d);
    xMinField.setMinValue(0d);
    xMinField.setMaxValue(100d);
    xMinField.setValue(0d);
    xMinField.setAllowBlank(false);
    xMinField.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
    xMinField.addValueChangeHandler(valueChangeHandler);
    xMinField.setWidth(70);

    xMaxField.setIncrement(1d);
    xMaxField.setMinValue(0d);
    xMaxField.setMaxValue(100d);
    xMaxField.setValue(100d);
    xMaxField.setAllowBlank(false);
    xMaxField.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
    xMaxField.addValueChangeHandler(valueChangeHandler);
    xMaxField.setWidth(70);

    store.addFilter(storeFilter);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Filter Chart");
    panel.setPixelSize(620, 500);
    panel.setBodyBorder(true);

    final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
    resize.setMinHeight(400);
    resize.setMinWidth(560);
    
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

    ToolBar toolBar = new ToolBar();
    toolBar.setEnableOverflow(false);
    toolBar.setHorizontalSpacing(5);
    toolBar.add(createLabel(yMinField, "Y Min"));
    toolBar.add(yMinField);
    toolBar.add(createLabel(yMaxField, "Y Max"));
    toolBar.add(yMaxField);
    toolBar.add(createLabel(xMinField, "X Min"));
    toolBar.add(xMinField);
    toolBar.add(createLabel(xMaxField, "X Max"));
    toolBar.add(xMaxField);
    toolBar.add(regenerate);

    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);

    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    layout.add(toolBar);

    chart.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(chart);

    return panel;

  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private FieldLabel createLabel(SpinnerField<Double> field, String label) {
    FieldLabel fieldLabel = new FieldLabel(field, label);
    fieldLabel.setLabelWidth(36);
    return fieldLabel;
  }
}
