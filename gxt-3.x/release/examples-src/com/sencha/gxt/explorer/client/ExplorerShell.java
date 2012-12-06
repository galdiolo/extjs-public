/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.explorer.client.app.ui.ExampleDetailView;
import com.sencha.gxt.explorer.client.app.ui.ExampleListView;
import com.sencha.gxt.state.client.BorderLayoutStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class ExplorerShell extends BorderLayoutContainer {

  public ExplorerShell() {
    monitorWindowResize = true;
    Window.enableScrolling(false);
    setPixelSize(Window.getClientWidth(), Window.getClientHeight());

    setStateful(true);
    setStateId("explorerLayout");

    BorderLayoutStateHandler state = new BorderLayoutStateHandler(this);
    state.loadState();
  }

  private ContentPanel west;
  private SimpleContainer center;

  enum Theme {
    BLUE("Blue Theme"), GRAY("Gray Theme");

    private final String value;

    private Theme(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return value();
    }
  }

  @Inject
  public ExplorerShell(ExampleListView listView, ExampleDetailView detailView) {
    this();

    StringBuffer sb = new StringBuffer();
    sb.append("<div id='demo-theme'></div><div id=demo-title>Sencha GXT Explorer Demo</div>");

    HtmlLayoutContainer northPanel = new HtmlLayoutContainer(sb.toString());
    northPanel.setStateful(false);
    northPanel.setId("demo-header");
    northPanel.addStyleName("x-small-editor");

    ListStore<Theme> colors = new ListStore<Theme>(new ModelKeyProvider<Theme>() {

      @Override
      public String getKey(Theme item) {
        return item.name();
      }

    });
    colors.add(Theme.BLUE);
    colors.add(Theme.GRAY);

    final SimpleContainer con = new SimpleContainer();
    con.getElement().getStyle().setMargin(3, Unit.PX);
    con.setResize(false);

    ComboBox<Theme> combo = new ComboBox<Theme>(colors, new StringLabelProvider<Theme>());
    combo.setTriggerAction(TriggerAction.ALL);
    combo.setForceSelection(true);
    combo.setEditable(false);
    combo.getElement().getStyle().setFloat(Float.RIGHT);
    combo.setWidth(125);
    combo.setValue(Theme.GRAY);
    combo.addSelectionHandler(new SelectionHandler<ExplorerShell.Theme>() {

      @Override
      public void onSelection(SelectionEvent<Theme> event) {
        if (event.getSelectedItem() == Theme.BLUE) {
          Window.Location.replace("explorer-blue.html");
        } else {
          Window.Location.replace("explorer-gray.html");
        }
      }
    });

    con.add(combo);

    // not adding selector now

    BorderLayoutData northData = new BorderLayoutData(35);
    setNorthWidget(northPanel, northData);

    BorderLayoutData westData = new BorderLayoutData(200);
    westData.setMargins(new Margins(5, 0, 5, 5));
    westData.setSplit(true);
    westData.setCollapsible(true);
    westData.setCollapseHidden(true);
    westData.setCollapseMini(true);

    west = new ContentPanel();
    west.setHeadingText("Navigation");
    west.setBodyBorder(true);
    west.add(listView.asWidget());

    MarginData centerData = new MarginData();
    centerData.setMargins(new Margins(5));

    center = new SimpleContainer();
    center.add(detailView.asWidget());

    setWestWidget(west, westData);
    setCenterWidget(center, centerData);
  }

  @Override
  protected void onWindowResize(int width, int height) {
    setPixelSize(width, height);
  }

}
