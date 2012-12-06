/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.ProgressBarCell;
import com.sencha.gxt.cell.core.client.ResizeCell;
import com.sencha.gxt.cell.core.client.SliderCell;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.DateCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Plant;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.theme.base.client.colorpalette.ColorPaletteBaseAppearance;
import com.sencha.gxt.widget.core.client.ColorPaletteCell;
import com.sencha.gxt.widget.core.client.ColorPaletteCell.ColorPaletteAppearance;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.CellSelectionEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(name = "Cell Grid", icon = "cellgrid", category = "Grid", classes = Plant.class)
public class CellGridExample implements IsWidget, EntryPoint {

  private static final String[] COLORS = new String[] {"161616", "002241", "006874", "82a700", "bbc039", "f3f1cd"};

  interface PlaceProperties extends PropertyAccess<Plant> {
    ValueProvider<Plant, Date> available();

    @Path("name")
    ModelKeyProvider<Plant> key();

    ValueProvider<Plant, String> name();

    ValueProvider<Plant, Integer> difficulty();

    ValueProvider<Plant, Double> progress();

    ValueProvider<Plant, String> color();
    
    ValueProvider<Plant, String> light();
  }

  private static final PlaceProperties properties = GWT.create(PlaceProperties.class);
  private ListStore<Plant> store;

  public class CellColumnResizer<M, T> implements ColumnWidthChangeHandler {

    private Grid<M> grid;
    private ColumnConfig<M, T> column;
    private ResizeCell<T> cell;

    public CellColumnResizer(Grid<M> grid, ColumnConfig<M, T> column, ResizeCell<T> cell) {
      this.grid = grid;
      this.column = column;
      this.cell = cell;
    }

    @Override
    public void onColumnWidthChange(ColumnWidthChangeEvent event) {
      if (column == event.getColumnConfig()) {
        int w = event.getColumnConfig().getWidth();
        int rows = store.size();

        int col = grid.getColumnModel().indexOf(column);

        cell.setWidth(w - 20);

        ListStore<M> store = grid.getStore();

        for (int i = 0; i < rows; i++) {
          M p = grid.getStore().get(i);

          // option 1 could be better for force fit where all columns are
          // resized
          // would need to run deferred using DelayedTask to ensure only run
          // once
          // grid.getStore().update(p);

          // option 2
          Element parent = grid.getView().getCell(i, col);
          if (parent != null) {
            parent = parent.getFirstChildElement();
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            cell.render(new Context(i, col, store.getKeyProvider().getKey(p)), column.getValueProvider().getValue(p),
                sb);
            parent.setInnerHTML(sb.toSafeHtml().asString());
          }

        }
      }
    }

  }

  @Override
  public Widget asWidget() {
    // reduce the padding on text element as we have widgets in the cells
    SafeStyles textStyles = SafeStylesUtils.fromTrustedString("padding: 1px 3px;");

    ColumnConfig<Plant, String> nameColumn = new ColumnConfig<Plant, String>(properties.name(), 100, "Name");
    // IMPORTANT we want the text element (cell parent) to only be as wide as
    // the cell and not fill the cell
    nameColumn.setColumnTextClassName(CommonStyles.get().inlineBlock());
    nameColumn.setColumnTextStyle(textStyles);

    TextButtonCell button = new TextButtonCell();
    button.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        Context c = event.getContext();
        int row = c.getIndex();
        Plant p = store.get(row);
        Info.display("Event", "The " + p.getName() + " was clicked.");
      }
    });
    nameColumn.setCell(button);

    DateCell dateCell = new DateCell();
    dateCell.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        Info.display("Date Selected",
            "You selected " + DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).format(event.getValue()));
      }
    });
    dateCell.setPropertyEditor(new DateTimePropertyEditor(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)));

    ColumnConfig<Plant, Date> availableColumn = new ColumnConfig<Plant, Date>(properties.available(), 170, "Date");
    availableColumn.setColumnTextStyle(SafeStylesUtils.fromTrustedString("padding: 2px 3px;"));
    availableColumn.setCell(dateCell);

    ListStore<String> lights = new ListStore<String>(new ModelKeyProvider<String>() {
      @Override
      public String getKey(String item) {
        return item;
      }
    });

    lights.add("Mostly Shady");
    lights.add("Mostly Sunny");
    lights.add("Shade");
    lights.add("Sunny");
    lights.add("Sun or Shade");

    ColumnConfig<Plant, String> lightColumn = new ColumnConfig<Plant, String>(properties.light(), 130, "Light");
    lightColumn.setColumnTextStyle(SafeStylesUtils.fromTrustedString("padding: 2px 3px;"));

    ComboBoxCell<String> lightCombo = new ComboBoxCell<String>(lights, new LabelProvider<String>() {
      @Override
      public String getLabel(String item) {
        return item;
      }
    });
    lightCombo.addSelectionHandler(new SelectionHandler<String>() {

      @Override
      public void onSelection(SelectionEvent<String> event) {
        CellSelectionEvent<String> sel = (CellSelectionEvent<String>) event;
        Plant p = store.get(sel.getContext().getIndex());
        Info.display("Lightness Selected", p.getName() + " selected " + event.getSelectedItem());
      }
    });
    lightCombo.setTriggerAction(TriggerAction.ALL);
    lightCombo.setForceSelection(true);
    
    lightColumn.setCell(lightCombo);
    lightCombo.setWidth(110);
    
    
    ColumnConfig<Plant, String> colorColumn = new ColumnConfig<Plant, String>(properties.color(), 140, "Color");
    colorColumn.setColumnTextStyle(SafeStylesUtils.fromTrustedString("padding: 2px 3px;"));

    // This next line only works with any appearance that extends from Base
    ColorPaletteBaseAppearance appearance = GWT.create(ColorPaletteAppearance.class);
    appearance.setColumnCount(6);

    ColorPaletteCell colorPalette = new ColorPaletteCell(appearance, COLORS, COLORS) {
      @Override
      public boolean handlesSelection() {
        return true;
      }
    };
    colorPalette.addSelectionHandler(new SelectionHandler<String>() {

      @Override
      public void onSelection(SelectionEvent<String> event) {
        Info.display("Color Selected", "You selected " + event.getSelectedItem());
      }
    });
    colorColumn.setCell(colorPalette);

    ColumnConfig<Plant, Integer> difficultyColumn = new ColumnConfig<Plant, Integer>(properties.difficulty(), 150,
        "Durability");
    SliderCell slider = new SliderCell() {
      @Override
      public boolean handlesSelection() {
        return true;
      }
    };
    slider.setWidth(140);
    difficultyColumn.setCell(slider);

    final ColumnConfig<Plant, Double> progressColumn = new ColumnConfig<Plant, Double>(properties.progress(), 150,
        "Progress");
    final ProgressBarCell progress = new ProgressBarCell() {
      @Override
      public boolean handlesSelection() {
        return true;
      }
    };
    progress.setProgressText("{0}% Complete");
    progress.setWidth(140);
    progressColumn.setCell(progress);

    List<ColumnConfig<Plant, ?>> l = new ArrayList<ColumnConfig<Plant, ?>>();
    l.add(nameColumn);
    l.add(availableColumn);
    l.add(lightColumn);
    l.add(colorColumn);
    l.add(difficultyColumn);
    l.add(progressColumn);
    ColumnModel<Plant> cm = new ColumnModel<Plant>(l);

    store = new ListStore<Plant>(properties.key());

    List<Plant> plants = new ArrayList<Plant>(TestData.getPlants());
    for (Plant p : plants) {
      p.setColor(COLORS[Random.nextInt(4)]);
    }

    store.addAll(plants);

    final Grid<Plant> grid = new Grid<Plant>(store, cm);
    grid.setBorders(true);
    grid.getView().setAutoExpandColumn(nameColumn);
    grid.getView().setTrackMouseOver(false);

    grid.getColumnModel().addColumnWidthChangeHandler(new CellColumnResizer<Plant, Double>(grid, progressColumn, progress));

    FramedPanel cp = new FramedPanel();
    cp.setHeadingText("Cell Grid Example");
    cp.setWidget(grid);
    cp.setPixelSize(920, 410);
    cp.addStyleName("margin-10");

    cp.setButtonAlign(BoxLayoutPack.CENTER);
    cp.addButton(new TextButton("Reset", new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        store.rejectChanges();
      }
    }));

    cp.addButton(new TextButton("Save", new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        store.commitChanges();
      }
    }));
    return cp;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
