/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.desktop.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.desktop.client.TaskBar;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.Window.CloseAction;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class DesktopApp implements EntryPoint {

  private Desktop desktop = new Desktop();

  public void onModuleLoad() {
    SelectionListener<ComponentEvent> listener = new SelectionListener<ComponentEvent>() {
      @Override
      public void componentSelected(ComponentEvent ce) {
        Window w = null;
        if (ce instanceof MenuEvent) {
          MenuEvent me = (MenuEvent) ce;
          w = me.item.getData("window");
        } else {
          w = ce.component.getData("window");
        }
        if (w != null) {
          w.show();
        }
      }
    };

    Window gridWindow = createGridWindow();
    Window accordionWindow = createAccordionWindow();

    Shortcut s1 = new Shortcut();
    s1.setText("Grid Window");
    s1.setId("grid-win-shortcut");
    s1.setData("window", gridWindow);
    s1.addSelectionListener(listener);
    desktop.addShortcut(s1);

    Shortcut s2 = new Shortcut();
    s2.setText("Accordion Window");
    s2.setId("acc-win-shortcut");
    s2.setData("window", accordionWindow);
    s2.addSelectionListener(listener);
    desktop.addShortcut(s2);

    TaskBar taskBar = desktop.getTaskBar();

    StartMenu menu = taskBar.getStartMenu();
    menu.setHeading("Darrell Meyer");
    menu.setIconStyle("user");

    MenuItem menuItem = new MenuItem("Grid Window");
    menuItem.setData("window", gridWindow);
    menuItem.setIconStyle("icon-grid");
    menuItem.addSelectionListener(listener);
    menu.add(menuItem);

    menuItem = new MenuItem("Tab Window");
    menuItem.setIconStyle("tabs");
    menuItem.addSelectionListener(listener);
    menuItem.setData("window", createTabWindow());
    menu.add(menuItem);

    menuItem = new MenuItem("Accordion Window");
    menuItem.setIconStyle("accordion");
    menuItem.addSelectionListener(listener);
    menuItem.setData("window", accordionWindow);
    menu.add(menuItem);

    menuItem = new MenuItem("Bogus Submenu");
    menuItem.setIconStyle("bogus");

    Menu sub = new Menu();

    for (int i = 0; i < 5; i++) {
      MenuItem item = new MenuItem("Bogus Window " + (i + 1));
      item.setData("window", createBogusWindow(i));
      item.addSelectionListener(listener);
      sub.add(item);
    }

    menuItem.setSubMenu(sub);
    menu.add(menuItem);

    // tools
    MenuItem tool = new MenuItem("Settings");
    tool.setIconStyle("settings");
    tool.addSelectionListener(new SelectionListener<ComponentEvent>() {
      @Override
      public void componentSelected(ComponentEvent ce) {
        Info.display("Event", "The 'Settings' tool was clicked");
      }
    });
    menu.addTool(tool);

    menu.addToolSeperator();

    tool = new MenuItem("Logout");
    tool.setIconStyle("logout");
    tool.addSelectionListener(new SelectionListener<ComponentEvent>() {
      @Override
      public void componentSelected(ComponentEvent ce) {
        Info.display("Event", "The 'Logout' tool was clicked");
      }
    });
    menu.addTool(tool);
  }

  private Window createAccordionWindow() {
    final Window w = new Window();
    w.setCloseAction(CloseAction.CLOSE);
    w.setMinimizable(true);
    w.setMaximizable(true);
    w.setIconStyle("accordion");
    w.setHeading("Accordion Window");
    w.setWidth(200);
    w.setHeight(350);

    ToolBar toolBar = new ToolBar();
    TextToolItem item = new TextToolItem();
    item.setIconStyle("icon-connect");
    toolBar.add(item);

    toolBar.add(new SeparatorToolItem());
    w.setTopComponent(toolBar);

    item = new TextToolItem();
    item.setIconStyle("icon-user-add");
    toolBar.add(item);

    item = new TextToolItem();
    item.setIconStyle("icon-user-delete");
    toolBar.add(item);

    w.setLayout(new AccordionLayout());

    ContentPanel cp = new ContentPanel();
    cp.setHeading("Online Users");
    cp.setScrollMode(Scroll.AUTO);
    cp.getHeader().addTool(new ToolButton("x-tool-refresh"));

    w.add(cp);

    Tree tree = new Tree();
    TreeItem family = new TreeItem("Family");
    tree.getRootItem().add(family);
    family.add(newItem("Darrell", "user"));
    family.add(newItem("Maro", "user-girl"));
    family.add(newItem("Lia", "user-kid"));
    family.add(newItem("Alec", "user-kid"));
    family.setExpanded(true);

    TreeItem friends = new TreeItem("Friends");
    tree.getRootItem().add(friends);
    friends.add(newItem("Abe", "user"));
    friends.add(newItem("Mary", "user-girl"));
    friends.add(newItem("Sally", "user-girl"));
    friends.add(newItem("Jack", "user"));
    friends.setExpanded(true);

    cp.add(tree);

    cp = new ContentPanel();
    cp.setHeading("Settings");
    cp.setBodyStyleName("pad-text");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    w.add(cp);

    cp = new ContentPanel();
    cp.setHeading("Stuff");
    cp.setBodyStyleName("pad-text");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    w.add(cp);

    cp = new ContentPanel();
    cp.setHeading("More Stuff");
    cp.setBodyStyleName("pad-text");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    w.add(cp);
    return w;
  }

  private Window createGridWindow() {
    Window w = new Window();
    w.setCloseAction(CloseAction.CLOSE);
    w.setIconStyle("icon-grid");
    w.setMinimizable(true);
    w.setMaximizable(true);
    w.setHeading("Grid Window");
    w.setSize(500, 400);
    w.setLayout(new FitLayout());

    GroupingStore<Stock> store = new GroupingStore<Stock>();
    store.add(TestData.getCompanies());
    store.groupBy("industry");

    ColumnConfig company = new ColumnConfig("name", "Company", 60);
    ColumnConfig price = new ColumnConfig("open", "Price", 20);
    price.setNumberFormat(NumberFormat.getCurrencyFormat());
    ColumnConfig change = new ColumnConfig("change", "Change", 20);
    ColumnConfig industry = new ColumnConfig("industry", "Industry", 20);
    ColumnConfig last = new ColumnConfig("date", "Last Updated", 20);
    last.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));

    List<ColumnConfig> config = new ArrayList<ColumnConfig>();
    config.add(company);
    config.add(price);
    config.add(change);
    config.add(industry);
    config.add(last);

    final ColumnModel cm = new ColumnModel(config);

    GroupingView view = new GroupingView();
    view.setForceFit(true);
    view.setGroupRenderer(new GridGroupRenderer() {
      public String render(GroupColumnData data) {
        String f = cm.getColumnById(data.field).getHeader();
        String l = data.models.size() == 1 ? "Item" : "Items";
        return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
      }
    });

    Grid<Stock> grid = new Grid<Stock>(store, cm);
    grid.setView(view);
    grid.setBorders(true);

    w.add(grid);
    return w;
  }

  private Window createTabWindow() {
    Window w = new Window();
    w.setCloseAction(CloseAction.CLOSE);
    w.setMinimizable(true);
    w.setMaximizable(true);
    w.setSize(740, 480);
    w.setIconStyle("tabs");
    w.setHeading("Tab Window");

    w.setLayout(new FitLayout());

    TabPanel panel = new TabPanel();

    for (int i = 0; i < 4; i++) {
      TabItem item = new TabItem("Tab Item " + (i + 1));
      item.addText("Something useful would be here");
      panel.add(item);
    }

    w.add(panel);
    return w;
  }

  private TreeItem newItem(String text, String iconStyle) {
    TreeItem item = new TreeItem(text);
    item.setIconStyle(iconStyle);
    return item;
  }

  private Window createBogusWindow(int index) {
    Window w = new Window();
    w.setCloseAction(CloseAction.CLOSE);
    w.setIconStyle("bogus");
    w.setMinimizable(true);
    w.setMaximizable(true);
    w.setHeading("Bogus Window " + ++index);
    w.setSize(400, 300);
    return w;
  }
}
