/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.examples.resources.client.model.MapValueProvider;
import com.sencha.gxt.examples.resources.client.model.ModelItem;
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
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Dynamic Line Chart", icon = "dynamicchart", category = "Charts", classes = {
    MapValueProvider.class, ModelItem.class})
public class DynamicExample implements IsWidget, EntryPoint {

  private final NumericAxis<ModelItem> axis = new NumericAxis<ModelItem>();

  public Widget asWidget() {
    final ListStore<ModelItem> store = new ListStore<ModelItem>(new ModelKeyProvider<ModelItem>() {
      @Override
      public String getKey(ModelItem item) {
        return String.valueOf(item.getKey());
      }
    });
    for (int i = 0; i < 12; i++) {
      ModelItem item = new ModelItem("item" + i);
      item.put("first", Math.random() * 100);
      store.add(item);
    }

    final ListStore<LineSeries<ModelItem>> fieldStore = new ListStore<LineSeries<ModelItem>>(
        new ModelKeyProvider<LineSeries<ModelItem>>() {
          @Override
          public String getKey(LineSeries<ModelItem> item) {
            return item.getYField().getPath();
          }
        });

    final Chart<ModelItem> chart = new Chart<ModelItem>();
    chart.setStore(store);
    chart.setShadowChart(true);
    chart.setAnimated(true);

    LineSeries<ModelItem> series = createLine("first");
    axis.addField(series.getYField());
    chart.addSeries(series);
    fieldStore.add(series);

    axis.setPosition(Position.LEFT);
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
    chart.addAxis(axis);

    Legend<ModelItem> legend = new Legend<ModelItem>();
    legend.setPosition(Position.RIGHT);
    legend.setItemHighlighting(true);
    legend.setItemHiding(true);
    chart.setLegend(legend);

    TextButton regenerate = new TextButton("Reload Data");
    regenerate.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        for (int i = 0; i < store.size(); i++) {
          ModelItem item = store.get(i);
          for (String field : item.keySet()) {
            item.put(field, Math.random() * 100);
          }
        }
        chart.redrawChart();
      }
    });

    final ComboBox<LineSeries<ModelItem>> box = new ComboBox<LineSeries<ModelItem>>(fieldStore,
        new LabelProvider<LineSeries<ModelItem>>() {
          @Override
          public String getLabel(LineSeries<ModelItem> item) {
            return item.getYField().getPath();
          }
        });

    final TextField fieldInput = new TextField();
    fieldInput.setValue("first");
    fieldInput.setAllowBlank(false);
    fieldInput.addValidator(new MaxLengthValidator(20));
    final RegExp regex = RegExp.compile("\\s");
    fieldInput.addValidator(new Validator<String>() {
		@Override
		public List<EditorError> validate(Editor<String> editor, String value) {
			if(regex.test(value)) {
				List<EditorError> errors = new ArrayList<EditorError>();
				errors.add(new DefaultEditorError(editor, "Field name cannot contain spaces.", ""));
				return errors;
			}
			return null;
		}
	});

    TextButton add = new TextButton("Add");
    add.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        String field = fieldInput.getValue();
        if (fieldInput.isCurrentValid() && field.length() > 0 && fieldStore.findModelWithKey(field) == null && fieldStore.size() < 10) {
          for (int i = 0; i < store.size(); i++) {
            ModelItem item = store.get(i);
            item.put(field, Math.random() * 100);
          }
          LineSeries<ModelItem> series = createLine(field);
          fieldStore.add(series);
          axis.addField(series.getYField());
          chart.addSeries(series);
          chart.redrawChart();
        }
      }
    });

    TextButton remove = new TextButton("Remove");
    remove.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        String field = box.getText();
        LineSeries<ModelItem> series = fieldStore.findModelWithKey(field);
        if (field.length() > 0 && series != null && fieldStore.size() > 0) {
          for (int i = 0; i < store.size(); i++) {
            ModelItem item = store.get(i);
            item.remove(field);
          }
          fieldStore.remove(series);
          axis.removeField(series.getYField());
          chart.removeSeries(series);
          chart.redrawChart();
        }
        box.clear();
      }
    });

    ToolBar toolBar = new ToolBar();
    toolBar.add(regenerate);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(add);
    toolBar.add(fieldInput);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(remove);
    toolBar.add(box);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Dynamic Chart");
    panel.setPixelSize(860, 720);
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

  private LineSeries<ModelItem> createLine(String field) {
    MapValueProvider valueProvider = new MapValueProvider(field);

    LineSeries<ModelItem> series = new LineSeries<ModelItem>();
    series.setYAxisPosition(Position.LEFT);
    Color color = new RGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    series.setStroke(color);
    series.setStrokeWidth(3);
    series.setShowMarkers(true);
    Sprite marker = Primitives.circle(0, 0, 6);
    marker.setFill(color);
    series.setMarkerConfig(marker);
    series.setYField(valueProvider);
    series.setLineHighlighter(new SeriesHighlighter() {

      @Override
      public void highlight(Sprite sprite) {
        DrawFx.createStrokeWidthAnimator(sprite, 6).run(250);
      }

      @Override
      public void unHighlight(Sprite sprite) {
        DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
      }
    });

    return series;
  }
}
