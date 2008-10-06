/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.DomHelper;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ColumnModelEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.DragListener;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Region;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Item;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * <p>
 * This class encapsulates the user interface of an {@link Grid}. Methods of
 * this class may be used to access user interface elements to enable special
 * display effects. Do not change the DOM structure of the user interface.
 * </p>
 * <p>
 * This class does not provide ways to manipulate the underlying data. The data
 * model of a Grid is held in an {@link ListStore}.
 * </p>
 * 
 */
public class GridView extends BaseObservable {

  class GridSplitBar extends BoxComponent {

    private int startX;
    private int colIndex;
    private boolean dragging;
    private Draggable d;
    private DragListener listener = new DragListener() {

      @Override
      public void dragEnd(DragEvent de) {
        onDragEnd(de);
      }

      @Override
      public void dragStart(DragEvent de) {
        onDragStart(de);
      }

    };

    protected void onDragEnd(DragEvent e) {
      dragging = false;
      headerDisabled = false;
      setStyleAttribute("borderLeft", "none");
      el().setStyleAttribute("opacity", "0");
      el().setWidth(splitterWidth);
      bar.el().setVisibility(false);

      int endX = e.event.getClientX();
      int diff = endX - startX;
      onColumnSplitterMoved(colIndex, cm.getColumnWidth(colIndex) + diff);
    }

    protected void onDragStart(DragEvent e) {
      headerDisabled = true;
      dragging = true;
      setStyleAttribute("borderLeft", "1px solid black");
      setStyleAttribute("cursor", "default");
      el().setStyleAttribute("opacity", "1");
      el().setWidth(1);

      startX = e.event.getClientX();

      int cols = cm.getColumnCount();
      for (int i = 0, len = cols; i < len; i++) {
        Element hd = getHeaderCell(i);
        if (hd != null) {
          Region rr = El.fly(hd).getRegion();
          if (startX > rr.right - 5 && startX < rr.right + 5) {
            colIndex = findHeaderIndex(hd);
            break;
          }
        }
      }

      if (colIndex > -1) {
        Element c = getHeaderCell(colIndex);
        int x = startX;
        int minx = x - fly((com.google.gwt.user.client.Element) c).getX()
            - grid.getMinColumnWidth();
        int maxx = (grid.el().getX() + grid.el().getWidth()) - e.event.getClientX();
        d.setXConstraint(minx, maxx);
      }
    }

    protected void onMouseMove(Event event) {
      if (dragging) {
        return;
      }
      int x = event.getClientX();
      Region r = activeHdRegion;
      int hw = splitterWidth;

      el().setY(grid.el().getY());
      el().setHeight(grid.getHeight());

      Style ss = getElement().getStyle();

      if (x - r.left <= hw && cm.isResizable(activeHdIndex - 1)) {
        bar.el().setVisibility(true);
        el().setX(r.left);
        ss.setProperty("cursor", GXT.isSafari ? "e-resize" : "col-resize");
      } else if (r.right - x <= hw && cm.isResizable(activeHdIndex)) {
        el().setX(r.right - (hw / 2));
        bar.el().setVisibility(true);
        ss.setProperty("cursor", GXT.isSafari ? "w-resize" : "col-resize");
      } else {
        bar.el().setVisibility(false);
        ss.setProperty("cursor", "");
      }
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element target, int index) {
      super.onRender(target, index);

      setElement(DOM.createDiv(), target, index);
      el().setStyleAttribute("cursor", "col-resize");
      setStyleAttribute("position", "absolute");
      setWidth(5);

      el().setVisibility(false);
      el().setStyleAttribute("backgroundColor", "white");
      el().setStyleAttribute("opacity", "0");

      d = new Draggable(this);
      d.setUseProxy(false);
      d.setConstrainVertical(true);
      d.addDragListener(listener);
    }

  }

  // config
  protected boolean enableRowBody;
  protected GridViewConfig viewConfig;
  protected Grid grid;

  protected ColumnModel cm;
  protected ListStore ds;

  protected String emptyText;
  protected boolean deferEmptyText;
  protected boolean enableHdMenu = true;
  protected boolean autoFill;
  protected boolean forceFit;
  protected int scrollOffset = 19;
  protected boolean userResized;
  protected int borderWidth = 2;
  protected int splitterWidth = 5;

  protected GridTemplates templates;

  // elements
  protected El el, mainWrap, mainHd, innerHd, scroller, mainBody, focusEl;
  protected int activeHdIndex;
  
  private boolean showDirtyCells = true;
  private String cellSelector = "td.x-grid3-cell";
  private String hdStyle = "x-grid3-hd";
  private String rowSelector = "div.x-grid3-row";
  private GridSplitBar bar;
  private Menu menu;
  private SortInfo sortState;
  private int lastViewWidth;
  private boolean headerDisabled;
  private Element activeHd;
  private Region activeHdRegion;
  private Element activeHdBtn;
  private StoreListener listener;
  private EventListener headerListener;
  private EventListener scrollListener;
  private Listener columnListener;

  /**
   * Returns the row element.
   * 
   * @param el the row element or any child element
   * @return the matching row element
   */
  public Element findRow(Element el) {
    if (el == null) {
      return null;
    }
    return fly(el).findParentElement(rowSelector, 10);
  }

  /**
   * Returns the row index.
   * 
   * @param elem the row or child of the row element
   * @return the index
   */
  public int findRowIndex(Element elem) {
    Element r = findRow(elem);
    return r != null ? r.getPropertyInt("rowIndex") : -1;
  }

  public void focusCell(int rowIndex, int colIndex, boolean hscroll) {
    Point xy = ensureVisible(rowIndex, colIndex, hscroll);
    if (xy != null) {
      focusEl.setXY(xy);
      focusEl.setFocus(true);
    }
  }

  public void focusRow(int rowIndex) {
    focusCell(rowIndex, 0, false);
  }

  /**
   * Returns the grid's &lt;TD> HtmlElement at the specified coordinates.
   * 
   * @param row the row index in which to find the cell
   * @param col the column index of the cell
   * @return the &lt;TD> at the specified coordinates
   */
  public Element getCell(int row, int col) {
    return getRow(row).getElementsByTagName("td").getItem(col);
  }

  /**
   * Returns the &lt;TD> HtmlElement which represents the Grid's header cell for
   * the specified column index.
   * 
   * @param index the column index
   * @return the &lt;TD> element.
   */
  public Element getHeaderCell(int index) {
    return mainHd.dom.getElementsByTagName("td").getItem(index);
  }

  /**
   * Return the &lt;TR> HtmlElement which represents a Grid row for the
   * specified index.
   * 
   * @param row the row index
   * @return the &lt;TR> element
   */
  public Element getRow(int row) {
    Element[] rows = getRows();
    if (row < rows.length) {
      return rows[row];
    }
    return null;
  }

  /**
   * Returns the view config.
   * 
   * @return the view config
   */
  public GridViewConfig getViewConfig() {
    return viewConfig;
  }

  public void handleComponentEvent(ComponentEvent ce) {
    Element row = findRow(ce.getTarget());
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        if (row != null) onRowOver(row);
        break;
      case Event.ONMOUSEOUT:
        if (row != null) onRowOut(row);
        break;
      case Event.ONMOUSEDOWN:
        onMouseDown((GridEvent) ce);
        break;
    }
  }

  /**
   * Initializes the view.
   * 
   * @param grid the grid
   */
  public void init(Grid grid) {
    this.grid = grid;
    this.cm = grid.getColumnModel();

    initListeners();

    initTemplates();
    initData(grid.getStore(), cm);
    initUI(grid);
  }

  /**
   * Initializes the data.
   * 
   * @param ds the data store
   * @param cm the column model
   */
  public void initData(ListStore ds, ColumnModel cm) {
    if (this.ds != null) {
      this.ds.removeStoreListener(listener);
    }
    if (ds != null) {
      ds.addStoreListener(listener);
    }
    this.ds = ds;

    if (this.cm != null) {

    }
    if (cm != null) {
      cm.addListener(Events.HiddenChange, columnListener);
    }
    this.cm = cm;
  }

  public void initUI(final Grid grid) {
    grid.addListener(Events.HeaderClick, new Listener<GridEvent>() {
      public void handleEvent(GridEvent e) {
        if (!e.getTargetEl().is(".x-grid3-hd-btn")) {
          onHeaderClick(grid, e.colIndex);
        }
      }
    });
  }

  public boolean isAutoFill() {
    return autoFill;
  }

  public boolean isForceFit() {
    return forceFit;
  }

  public boolean isShowDirtyCells() {
    return showDirtyCells;
  }

  public void layout() {
    if (mainBody == null) {
      return;
    }

    El c = grid.el();
    Size csize = c.getStyleSize();

    int vw = csize.width;
    int vh = 0;
    if (vw < 10 || csize.height < 20) {
      return;
    }

    if (grid.isAutoHeight()) {
      el.setWidth(csize.width);
      scroller.dom.getStyle().setProperty("overflow", "visible");
    } else {
      el.setSize(csize.width, csize.height);
      int hdHeight = mainHd.getHeight();
      vh = csize.height - hdHeight;

      scroller.setSize(vw + 1, vh);

      if (innerHd != null) {
        innerHd.dom.getStyle().setProperty("width", vw + "px");
      }
    }

    if (forceFit) {
      if (lastViewWidth != vw) {
        fitColumns(false, false, -1);
        lastViewWidth = vw;
      }
    } else {
      autoExpand(false);
      syncHeaderScroll();
    }

    templateOnLayout(vw, vh);
  }

  public void refresh(boolean headerToo) {
    if (grid instanceof EditorGrid) {
      ((EditorGrid) grid).stopEditing(true);
    }

    String result = renderBody();
    mainBody.setInnerHtml(result);

    if (headerToo) {
      updateHeaders();
    }
    processRows(0, true);
    layout();

    fireEvent(Events.Refresh);
  }

  public void render() {
    this.cm = grid.getColumnModel();

    renderUI();

    grid.sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS);
  }

  /**
   * Scrolls the grid to the top.
   */
  public void scrollToTop() {
    scroller.setScrollTop(0);
    scroller.setScrollLeft(0);
  }

  /**
   * True to auto expand the columns to fit the grid <b>when the grid is
   * created</b>.
   * 
   * @param autoFill true to expand
   */
  public void setAutoFill(boolean autoFill) {
    this.autoFill = autoFill;
  }

  /**
   * True to auto expand/contract the size of the columns to fit the grid width
   * and prevent horizontal scrolling.
   * 
   * @param forceFit true to force fit
   */
  public void setForceFit(boolean forceFit) {
    this.forceFit = forceFit;
  }

  public void setShowDirtyCells(boolean showDirtyCells) {
    this.showDirtyCells = showDirtyCells;
  }

  /**
   * Sets the view config.
   * 
   * @param viewConfig the view config
   */
  public void setViewConfig(GridViewConfig viewConfig) {
    this.viewConfig = viewConfig;
  }

  protected void addRowStyle(Element elem, String style) {
    if (elem != null) {
      fly(elem).addStyleName(style);
    }
  }

  protected void autoExpand(boolean preventUpdate) {
    if (!userResized && grid.getAutoExpandColumn() != null) {
      int tw = cm.getTotalWidth(false);
      int aw = grid.getWidth(true) - scrollOffset;
      if (tw != aw) {
        int ci = cm.getIndexById(grid.getAutoExpandColumn());
        int currentWidth = cm.getColumnWidth(ci);
        int cw = Math.min(Math.max(((aw - tw) + currentWidth), grid.getAutoExpandMin()),
            grid.getAutoExpandMax());
        if (cw != currentWidth) {
          cm.setColumnWidth(ci, cw, true);
          if (!preventUpdate) {
            updateColumnWidth(ci, cw);
          }
        }
      }
    }
  }

  protected Menu createContextMenu(final int colIndex) {
    final Menu menu = new Menu();

    if (cm.isSortable(colIndex)) {
      MenuItem item = new MenuItem();
      item.setText(GXT.MESSAGES.gridView_sortAscText());
      item.setIconStyle("my-icon-asc");
      item.addSelectionListener(new SelectionListener<ComponentEvent>() {
        public void componentSelected(ComponentEvent ce) {
          ds.sort(cm.getDataIndex(colIndex), SortDir.ASC);
        }

      });
      menu.add(item);

      item = new MenuItem();
      item.setText(GXT.MESSAGES.gridView_sortDescText());
      item.setIconStyle("my-icon-desc");
      item.addSelectionListener(new SelectionListener<ComponentEvent>() {
        public void componentSelected(ComponentEvent ce) {
          ds.sort(cm.getDataIndex(colIndex), SortDir.DESC);
        }
      });
      menu.add(item);
    }

    MenuItem columns = new MenuItem();
    columns.setText(GXT.MESSAGES.gridView_columnsText());
    columns.setIconStyle("x-cols-icon");

    final Menu columnMenu = new Menu();

    int cols = cm.getColumnCount();
    for (int i = 0; i < cols; i++) {
      if (cm.getColumnHeader(i) == null || cm.getColumnHeader(i).equals("") || cm.isFixed(i)) {
        continue;
      }
      final int fcol = i;
      final CheckMenuItem check = new CheckMenuItem();
      check.setHideOnClick(false);
      check.setText(cm.getColumnHeader(i));
      check.setChecked(!cm.isHidden(i));
      check.addSelectionListener(new SelectionListener<ComponentEvent>() {
        public void componentSelected(ComponentEvent ce) {
          cm.setHidden(fcol, !cm.isHidden(fcol));
          restrictMenu(columnMenu);
        }
      });
      columnMenu.add(check);
    }

    restrictMenu(columnMenu);

    columns.setSubMenu(columnMenu);
    menu.add(columns);

    return menu;
  }

  protected void deleteRows(ListStore store, int firstRow, int lastRow) {
    if (store.getCount() < 1) {
      refresh(false);
      return;
    }
    removeRows(firstRow, lastRow);
  }

  protected void doAttach() {
    DOM.setEventListener(mainHd.dom, headerListener);
    if (bar != null) {
      DOM.setEventListener(bar.getElement(), bar);
    }
    if (scroller != null) {
      DOM.setEventListener(scroller.dom, scrollListener);
    }
  }

  protected void doDetach() {
    DOM.setEventListener(mainHd.dom, null);
    if (bar != null) {
      DOM.setEventListener(bar.getElement(), null);
    }
    if (scroller != null) {
      DOM.setEventListener(scroller.dom, null);
    }
  }

  protected String doRender(List<ColumnData> cs, List rows, int startRow, int colCount,
      boolean stripe) {

    int last = colCount - 1;
    String tstyle = "width:" + getTotalWidth() + ";";

    StringBuilder buf = new StringBuilder();
    StringBuilder cb = new StringBuilder();

    for (int j = 0; j < rows.size(); j++) {
      ModelData model = (ModelData) rows.get(j);
      Record r = ds.hasRecord(model) ? ds.getRecord(model) : null;

      int rowIndex = (j + startRow);

      for (int i = 0; i < colCount; i++) {
        ColumnData c = cs.get(i);
        String rv = getRenderedValue(c, rowIndex, i, model, c.id);
        String css = i == 0 ? "x-grid-cell-first " : (i == last ? "x-grid3-cell-last " : " ") + " "
            + c.css;
        String attr = c.cellAttr != null ? c.cellAttr : "";
        String cellAttr = c.cellAttr != null ? c.cellAttr : "";

        if (showDirtyCells && r != null && r.getChanges().containsKey(c.id)) {
          css += " x-grid3-dirty-cell";
        }

        cb.append(templates.cell(c.id, css, attr, cellAttr, c.style, rv));
      }

      String alt = "";
      if (stripe && ((rowIndex + 1) % 2 == 0)) {
        alt += " x-grid3-row-alt";
      }

      if (showDirtyCells && r != null && r.isDirty()) {
        alt += " x-grid3-dirty-row";
      }

      if (viewConfig != null) {
        alt += " " + viewConfig.getRowStyle(model, rowIndex, ds);
      }
      buf.append(templates.row(tstyle, alt, colCount, cb.toString(), enableRowBody, "", ""));
      cb = new StringBuilder();
    }
    return buf.toString();
  }

  protected Point ensureVisible(int row, int col, boolean hscroll) {
    if (row < 0 || row > ds.getCount()) {
      return null;
    }

    Element rowEl = getRow(row);
    Element cellEl = null;
    if (!(!hscroll && col == 0)) {
      while (cm.isHidden(col)) {
        col++;
      }
      cellEl = getCell(row, col);

    }

    if (rowEl == null) {
      return null;
    }

    Element c = scroller.dom;

    int ctop = 0;
    Element p = rowEl, stope = el.dom;
    while (p != null && p != stope) {
      ctop += p.getOffsetTop();
      p = p.getOffsetParent().cast();
    }
    ctop -= mainHd.dom.getOffsetHeight();

    int cbot = ctop + rowEl.getOffsetHeight();

    int ch = c.getOffsetHeight();
    int stop = c.getScrollTop();
    int sbot = stop + ch;

    if (ctop < stop) {
      c.setScrollTop(ctop);
    } else if (cbot > sbot) {
      c.setScrollTop(cbot -= ch);
    }

    if (hscroll) {
      int cleft = cellEl.getOffsetLeft();
      int cright = cleft + cellEl.getOffsetWidth();
      int sleft = c.getScrollLeft();
      int sright = sleft + c.getOffsetWidth();
      if (cleft < sleft) {
        c.setScrollLeft(cleft);
      } else if (cright > sright) {
        c.setScrollLeft(cright - c.getOffsetWidth());
      }
    }

    return cellEl != null ? fly(cellEl).getXY() : new Point(c.getScrollLeft(), fly(rowEl).getY());
  }

  protected Element findCell(Element elem) {
    if (elem == null) {
      return null;
    }
    return fly(elem).findParentElement(cellSelector, 3);
  }

  protected int findCellIndex(Element elem, String requiredStyle) {
    Element cell = findCell(elem);
    if (cell != null && (requiredStyle == null || fly(cell).hasStyleName(requiredStyle))) {
      return getCellIndex(cell);
    }
    return -1;
  }

  protected Element findHeaderCell(Element elem) {
    Element cell = findCell(elem);
    return cell != null && fly(cell).hasStyleName(hdStyle) ? cell : null;
  }

  protected int findHeaderIndex(Element elem) {
    return findCellIndex(elem, hdStyle);
  }

  protected void fitColumns(final boolean preventRefresh, final boolean onlyExpand, int omitColumn) {
    int tw = cm.getTotalWidth(false);
    double aw = grid.el().firstChild().getWidth(true) - scrollOffset;

    if (aw > 2000) {
      return;
    }
    final int oc = omitColumn;
    if (aw < 20) { // not initialized, so don't screw up the default widths
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          fitColumns(preventRefresh, onlyExpand, oc);
        }
      });
      return;
    }

    int extra = (int) aw - tw;

    if (extra == 0) {
      return;
    }

    int vc = cm.getColumnCount(true);
    int ac = vc - (omitColumn != -1 ? 1 : 0);

    if (ac == 0) {
      ac = 1;
      omitColumn = -1;
    }

    int colCount = cm.getColumnCount();
    Stack<Integer> cols = new Stack<Integer>();
    int extraCol = 0;
    int width = 0;
    int w;

    for (int i = 0; i < colCount; i++) {
      if (!cm.isHidden(i) && !cm.isFixed(i) && i != omitColumn) {
        w = cm.getColumnWidth(i);
        cols.push(i);
        extraCol = i;
        cols.push(w);
        width += w;
      }
    }

    double frac = (aw - cm.getTotalWidth()) / width;

    while (cols.size() > 0) {
      w = cols.pop();
      int i = cols.pop();
      int ww = Math.max(grid.getMinColumnWidth(), (int) Math.floor(w + w * frac));
      cm.setColumnWidth(i, ww, true);
    }

    if ((tw = cm.getTotalWidth(false)) > aw) {
      int adjustCol = ac != vc ? omitColumn : extraCol;
      cm.setColumnWidth(adjustCol, (int) Math.max(1, cm.getColumnWidth(adjustCol) - (tw - aw)),
          true);
    }

    if (!preventRefresh) {
      updateAllColumnWidths();
    }

  }

  protected El fly(Element elem) {
    return El.fly(elem);
  }

  protected int getCellIndex(Element elem) {
    if (elem != null) {
      String id = getCellIndexId(elem);
      if (id != null) {
        return cm.getIndexById(id);
      }
    }
    return -1;
  }

  protected List<ColumnData> getColumnData() {
    int colCount = cm.getColumnCount();
    List<ColumnData> cs = new ArrayList<ColumnData>();
    for (int i = 0; i < colCount; i++) {
      String name = cm.getDataIndex(i);
      ColumnData data = new ColumnData();
      data.name = name == null ? cm.getColumnId(i) : name;
      data.renderer = cm.getRenderer(i);
      data.id = cm.getColumnId(i);
      data.style = getColumnStyle(i, false);
      cs.add(data);
    }
    return cs;
  }

  protected Element getEditorParent() {
    return scroller.dom;
  }

  protected String getRenderedValue(ColumnData data, int rowIndex, int colIndex, ModelData m,
      String property) {
    GridCellRenderer r = cm.getRenderer(colIndex);
    if (r != null) {
      return r.render(ds.getAt(rowIndex), property, data, rowIndex, colIndex, ds);
    }
    Object val = m.get(property);

    ColumnConfig c = cm.getColumn(colIndex);

    if (val != null && c.getNumberFormat() != null) {
      Number n = (Number) val;
      NumberFormat nf = cm.getColumn(colIndex).getNumberFormat();
      val = nf.format(n.doubleValue());
    } else if (val != null && c.getDateTimeFormat() != null) {
      DateTimeFormat dtf = c.getDateTimeFormat();
      val = dtf.format((Date) val);
    }

    if (val != null) {
      return val.toString();
    }
    return "";
  }

  protected Element[] getRows() {
    NodeList ns = mainBody.dom.getChildNodes();
    Element[] rows = new Element[ns.getLength()];
    for (int i = 0, len = ns.getLength(); i < len; i++) {
      rows[i] = ns.getItem(i).cast();
    }

    return rows;
  }

  protected Point getScrollState() {
    return new Point(scroller.getScrollLeft(), scroller.getScrollTop());
  }

  protected String getTotalWidth() {
    return cm.getTotalWidth() + "px";
  }

  protected void initElements() {
    NodeList cs = grid.getElement().getFirstChild().getChildNodes();

    el = grid.el().firstChild();

    mainWrap = new El((com.google.gwt.user.client.Element) cs.getItem(0));
    mainHd = mainWrap.firstChild();

    if (grid.isHideHeaders()) {
      mainHd.setVisible(false);
    }

    innerHd = mainHd.firstChild();

    scroller = mainWrap.getChild(1);
    scroller.addEventsSunk(Event.ONSCROLL);

    if (forceFit) {
      scroller.setStyleAttribute("overflowX", "hidden");
    }

    mainBody = scroller.firstChild();
    focusEl = scroller.getChild(1);
  }

  protected void initListeners() {
    listener = new StoreListener() {

      @Override
      public void storeAdd(StoreEvent se) {
        onAdd(ds, se.models, se.index);
      }

      @Override
      public void storeBeforeDataChanged(StoreEvent se) {
        onBeforeDataChanged(se);
      }

      @Override
      public void storeClear(StoreEvent se) {
        onClear(se);
      }

      @Override
      public void storeDataChanged(StoreEvent se) {
        onDataChanged(se);
      }

      @Override
      public void storeFilter(StoreEvent se) {
        onDataChanged(se);
      }

      @Override
      public void storeRemove(StoreEvent se) {
        onRemove(ds, se.model, se.index, false);
      }

      @Override
      public void storeUpdate(StoreEvent se) {
        onUpdate(ds, se.model);
      }

    };
    headerListener = new EventListener() {

      public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOVER:
            handleHdOver(event);
            break;
          case Event.ONMOUSEOUT:
            handleHdOut(event);
            break;
          case Event.ONMOUSEMOVE:
            handleHdMove(event);
            break;
          case Event.ONCLICK:
            handleHdDown(event);
            break;
        }
      }

    };

    scrollListener = new EventListener() {
      public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONSCROLL) {
          syncScroll();
        }
      }
    };

    columnListener = new Listener<ColumnModelEvent>() {

      public void handleEvent(ColumnModelEvent be) {
        switch (be.type) {
          case Events.HiddenChange:
            onHiddenChange(cm, be.colIndex, be.hidden);
            break;
        }
      }

    };
  }

  protected void initTemplates() {
    templates = GWT.create(GridTemplates.class);
  }

  protected void insertRows(ListStore store, int firstRow, int lastRow, boolean isUpdate) {
    if (isUpdate && firstRow == 0 && lastRow == store.getCount() - 1) {
      refresh(false);
      return;
    }
    String html = renderRows(firstRow, lastRow);
    Element before = getRow(firstRow);
    if (before != null) {
      DomHelper.insertBefore((com.google.gwt.user.client.Element) before, html);
    } else {
      DomHelper.insertHtml("beforeEnd", mainBody.dom, html);
    }

    if (!isUpdate) {
      processRows(firstRow, false);
    }
  }

  protected void onAdd(ListStore store, List models, int index) {
    insertRows(store, index, index + (models.size() - 1), false);
  }

  protected void onBeforeDataChanged(StoreEvent se) {
    if (grid.isLoadMask()) {
      grid.el().mask(GXT.MESSAGES.loadMask_msg());
    }
  }

  protected void onCellDeselect(int row, int col) {
    Element cell = getCell(row, col);
    if (cell != null) {
      fly(cell).removeStyleName("x-grid3-cell-selected");
    }
  }

  protected void onCellSelect(int row, int col) {
    Element cell = getCell(row, col);
    if (cell != null) {
      fly(cell).addStyleName("x-grid3-cell-selected");
    }
  }

  protected void onClear(StoreEvent se) {
    refresh(false);
  }

  protected void onClick(ComponentEvent ce) {
    Element row = findRow(ce.getTarget());
    if (row != null) {
      GridEvent e = (GridEvent) ce;
      e.rowIndex = findRowIndex(row);
      grid.fireEvent(Events.RowClick, e);
    }
  }

  protected void onColumnMove(ColumnModel cm, int oldIndex, int newIndex) {
    // indexMap = null;
    Point s = getScrollState();
    refresh(true);
    restoreScroll(s);
    templateAfterMove(newIndex);
  }

  protected void onDataChanged(StoreEvent se) {
    refresh(false);
    updateHeaderSortState();
    if (grid.isLoadMask()) {
      grid.el().unmask();
    }
  }

  protected void onHeaderChange() {
    updateHeaders();
  }

  protected void onHeaderClick(Grid grid, int column) {
    if (headerDisabled || !cm.isSortable(column)) {
      return;
    }
    grid.store.sort(cm.getDataIndex(column), null);
  }

  protected void onHiddenChange(ColumnModel cm, int col, boolean hidden) {
    updateColumnHidden(col, hidden);
  }

  protected void onMouseDown(GridEvent ge) {

  }

  protected void onRemove(ListStore ds, ModelData m, int index, boolean isUpdate) {
    removeRow(index);
    processRows(0, false);
  }

  protected void onRowDeselect(int rowIndex) {
    Element row = getRow(rowIndex);
    if (row != null) {
      removeRowStyle(row, "x-grid3-row-selected");
    }
  }

  protected void onRowOut(Element row) {
    if (grid.isTrackMouseOver()) {
      removeRowStyle(row, "x-grid3-row-over");
    }
  }

  protected void onRowOver(Element row) {
    if (grid.isTrackMouseOver()) {
      addRowStyle(row, "x-grid3-row-over");
    }
  }

  protected void onRowSelect(int rowIndex) {
    Element row = getRow(rowIndex);
    if (row != null) {
      onRowOut(row);
      addRowStyle(row, "x-grid3-row-selected");
    }
  }

  protected void onUpdate(ListStore store, ModelData model) {
    refreshRow(store.indexOf(model));
  }

  protected void processRows(int startRow, boolean skipStript) {
    if (ds.getCount() < 1) {
      return;
    }
    Element[] rows = getRows();
    for (int i = 0, len = rows.length; i < len; i++) {
      Element row = rows[i];
      row.setPropertyInt("rowIndex", i);
    }
  }

  protected void refreshRow(int row) {
    ModelData m = ds.getAt(row);
    if (m != null) {
      insertRows(ds, row, row, true);
      getRow(row).setPropertyInt("rowIndex", row);
      onRemove(ds, m, row + 1, true);
      GridEvent e = new GridEvent(grid);
      e.rowIndex = row;
      e.model = ds.getAt(row);
      fireEvent(Events.RowUpdated, e);
    }
  }

  protected void removeRow(int row) {
    Element r = getRow(row);
    if (r != null) {
      fly(r).removeFromParent();
    }
  }

  protected void removeRows(int firstRow, int lastRow) {
    for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
      mainBody.getChild(firstRow).removeFromParent();
    }
  }

  protected void removeRowStyle(Element row, String style) {
    fly(row).removeStyleName(style);
  }

  protected String renderBody() {
    String markup = renderRows(0, -1);
    return templates.body(markup);
  }

  protected String renderHeaders() {
    int count = cm.getColumnCount();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < count; i++) {
      String istyle = cm.getColumnAlignment(i) == HorizontalAlignment.RIGHT ? "padding-right:16px"
          : null;
      sb.append(templates.headerCell(cm.getColumnId(i), cm.getColumnHeader(i), getColumnStyle(i,
          true), cm.getColumnToolTip(i), istyle, GXT.BLANK_IMAGE_URL, enableHdMenu));
    }
    return templates.header(sb.toString(), "width: " + getTotalWidth() + ";");
  }

  protected String renderRows(int startRow, int endRow) {
    int colCount = cm.getColumnCount();

    if (ds.getCount() < 1) {
      return "";
    }

    List<ColumnData> cs = getColumnData();

    if (endRow == -1) {
      endRow = ds.getCount() - 1;
    }

    List rs = ds.getRange(startRow, endRow);

    return doRender(cs, rs, startRow, colCount, grid.isStripeRows());

  }

  protected void renderUI() {
    String header = renderHeaders();
    String body = templates.body("");

    String html = templates.master(body, header);

    grid.getElement().setInnerHTML(html);

    initElements();

    mainBody.setInnerHtml(renderRows(0, -1));
    processRows(0, true);

    if (grid.isEnableColumnResize()) {
      bar = new GridSplitBar();
      bar.render(grid.getElement());
    }

    mainHd.addEventsSunk(Event.MOUSEEVENTS | Event.ONCLICK);
    updateHeaderSortState();

    if (!GXT.isGecko) {
      grid.disableTextSelection(true);
    }
  }

  protected void restoreScroll(Point state) {
    scroller.setScrollLeft(state.x);
    scroller.setScrollTop(state.y);
  }

  protected void templateAfterMove(int index) {
    // template method
  }

  protected void templateOnAllColumnWidthsUpdated(List<String> ws, String tw) {
    // template method
  }

  protected void templateOnColumnHiddenUpdated(int col, boolean hidden, String tw) {
    // template method
  }

  protected void templateOnColumnWidthUpdated(int col, String w, String tw) {
    // template method
  }

  protected void templateOnLayout(int vw, int vh) {
    // template method
  }

  protected void templateUpdateColumnText(int col, String text) {
    // template method
  }

  protected void updateAllColumnWidths() {
    String tw = getTotalWidth();
    int clen = cm.getColumnCount();
    Stack<String> ws = new Stack<String>();
    for (int i = 0; i < clen; i++) {

      ws.push(getColumnWidth(i));
    }

    innerHd.firstChild().firstChild().setWidth(tw);

    for (int i = 0; i < clen; i++) {
      Element hd = getHeaderCell(i);
      hd.getStyle().setProperty("width", ws.get(i));
    }

    Element[] ns = getRows();
    for (int i = 0, len = ns.length; i < len; i++) {
      Element elem = ns[i];
      elem.getStyle().setProperty("width", tw);

      elem = elem.getFirstChildElement();
      elem.getStyle().setProperty("width", tw);
      TableSectionElement section = elem.cast();
      Element row = section.getRows().getItem(0);

      for (int j = 0; j < clen; j++) {
        Element cell = row.getChildNodes().getItem(j).cast();
        cell.getStyle().setProperty("width", "" + ws.get(j));
      }
    }

    templateOnAllColumnWidthsUpdated(ws, tw);
  }

  protected void updateColumnHidden(int index, boolean hidden) {
    String tw = getTotalWidth();
    String display = hidden ? "none" : "";

    innerHd.dom.getFirstChildElement().getFirstChildElement().getStyle().setProperty("width", tw);
    Element hd = getHeaderCell(index);
    hd.getStyle().setProperty("display", display);

    Element[] ns = getRows();
    for (int i = 0, len = ns.length; i < len; i++) {
      Element elem = ns[i];
      elem.getStyle().setProperty("width", tw);
      elem.getFirstChildElement().getStyle().setProperty("width", tw);
      TableSectionElement e = elem.getFirstChild().cast();
      Element cell = e.getRows().getItem(0).getChildNodes().getItem(index).cast();
      cell.getStyle().setProperty("display", display);
    }

    templateOnColumnHiddenUpdated(index, hidden, tw);

    lastViewWidth = -1;
    layout();
  }

  protected void updateColumnWidth(int col, int width) {
    String tw = getTotalWidth();
    String w = getColumnWidth(col);

    innerHd.firstChild().firstChild().setWidth(tw);

    Element hd = getHeaderCell(col);
    hd.getStyle().setProperty("width", w);

    Element[] ns = getRows();
    for (int i = 0, len = ns.length; i < len; i++) {
      Element elem = ns[i];
      elem.getStyle().setProperty("width", tw);
      elem.getFirstChildElement().getStyle().setProperty("width", tw);
      TableSectionElement e = elem.getFirstChild().cast();
      Element cell = e.getRows().getItem(0).getChildNodes().getItem(col).cast();
      cell.getStyle().setProperty("width", w);
    }

    templateOnColumnWidthUpdated(col, w, tw);
  }

  protected void updateHeaders() {
    innerHd.firstChild().setInnerHtml(renderHeaders());
  }

  protected void updateHeaderSortState() {
    SortInfo state = ds.getSortState();
    if (state == null || state.getSortField() == null) {
      return;
    }
    if (this.sortState == null || (!this.sortState.getSortField().equals(state.getSortField()))
        || this.sortState.getSortDir() != state.getSortDir()) {
      GridEvent e = new GridEvent(grid);
      e.sortInfo = state;
      grid.fireEvent(Events.SortChange, e);
    }
    this.sortState = state;
    int sortColumn = cm.findColumnIndex(state.getSortField());
    if (sortColumn != -1) {
      updateSortIcon(sortColumn, state.getSortDir());
    }
  }

  protected void updateSortIcon(int colIndex, SortDir dir) {
    Element[] hds = mainHd.select("td");
    for (int i = 0; i < hds.length; i++) {
      fly(hds[i]).removeStyleName("sort-asc");
      fly(hds[i]).removeStyleName("sort-desc");
    }
    String s = dir == SortDir.DESC ? "sort-desc" : "sort-asc";
    fly(hds[colIndex]).addStyleName(s);
  }

  private native String getCellIndexId(Element elem) /*-{
     if (!$wnd.GXT.___colRe) {
     $wnd.GXT.___colRe = new RegExp("x-grid3-td-([^\\s]+)");
     }
     if (elem) {
     var m = elem.className.match($wnd.GXT.___colRe);
     if(m && m[1]){
     return m[1];
     }
     }
     return null;
     
     }-*/;

  private String getColumnStyle(int colIndex, boolean isHeader) {
    String style = cm.getColumnStyle(colIndex);
    if (style == null) style = "";
    style += "width:" + getColumnWidth(colIndex) + ";";
    if (cm.isHidden(colIndex)) {
      style += "display:none;";
    }
    HorizontalAlignment align = cm.getColumnAlignment(colIndex);
    if (align != null) {
      style += "text-align:" + align.name() + ";";
    }
    return style;
  }

  private String getColumnWidth(int col) {
    int w = cm.getColumnWidth(col);
    return (XDOM.isVisibleBox ? w : (w - borderWidth > 0 ? w - borderWidth : 0)) + "px";
  }

  private void handleHdDown(Event e) {
    if (DOM.isOrHasChild((com.google.gwt.user.client.Element) activeHdBtn,
        (com.google.gwt.user.client.Element) e.getTarget())) {
      e.cancelBubble(true);
      e.preventDefault();

      final Element hd = findHeaderCell(e.getTarget());
      fly(hd).addStyleName("x-grid3-hd-menu-open");

      int colIndex = findHeaderIndex(e.getTarget());
      menu = createContextMenu(colIndex);

      GridEvent ge = new GridEvent(grid);
      ge.colIndex = colIndex;
      ge.menu = menu;
      if (!grid.fireEvent(Events.HeaderContextMenu, ge)) {
        return;
      }

      menu.addListener(Events.Hide, new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent be) {
          fly(hd).removeStyleName("x-grid3-hd-menu-open");
        }

      });
      menu.show((com.google.gwt.user.client.Element) activeHdBtn, "tl-bl");

    }
  }

  private void handleHdMove(Event e) {
    if (activeHd != null && !headerDisabled) {
      bar.onMouseMove(e);
    }
  }

  private void handleHdOut(Event e) {
    Element hd = findHeaderCell(e.getTarget());
    if (hd != null) {
      activeHd = null;
      fly(hd).removeStyleName("x-grid3-hd-over");
      hd.getStyle().setProperty("cursor", "");
    }
  }

  private void handleHdOver(Event e) {
    Element hd = findHeaderCell(e.getTarget());
    if (hd != null && !headerDisabled) {
      activeHd = hd;
      activeHdIndex = getCellIndex(hd);
      activeHdRegion = fly(hd).getRegion();
      if (!cm.isMenuDisabled(activeHdIndex)) {
        fly(hd).addStyleName("x-grid3-hd-over");
        activeHdBtn = fly(hd).childElement(".x-grid3-hd-btn");
        if (activeHdBtn != null) {
          activeHdBtn.getStyle().setProperty("height", fly(hd).firstChild().getHeight() + "px");
        }
      }
    }
  }

  private void onColumnSplitterMoved(int colIndex, int width) {
    userResized = true;
    cm.setColumnWidth(colIndex, width);

    if (forceFit) {
      fitColumns(true, false, colIndex);
      updateAllColumnWidths();
    } else {
      updateColumnWidth(colIndex, width);
    }

    GridEvent e = new GridEvent(grid);
    e.colIndex = colIndex;
    e.width = width;
    grid.fireEvent(Events.ColumnResize, e);
  }

  private void restrictMenu(Menu columns) {
    int count = 0;
    for (int i = 0, len = cm.getColumnCount(); i < len; i++) {
      if (!cm.isHidden(i) && !cm.isFixed(i)) {
        count++;
      }
    }

    if (count == 1) {
      for (Item item : columns.getItems()) {
        CheckMenuItem ci = (CheckMenuItem) item;
        if (ci.isChecked()) {
          ci.disable();
        }
      }
    } else {
      for (Item item : columns.getItems()) {
        item.enable();
      }
    }
  }

  private void syncHeaderScroll() {
    innerHd.setScrollLeft(scroller.getScrollLeft());
    innerHd.setScrollLeft(scroller.getScrollLeft());
  }

  private void syncScroll() {
    syncHeaderScroll();
    GridEvent ge = new GridEvent(grid);
    ge.scrollLeft = scroller.getScrollLeft();
    ge.scrollTop = scroller.getScrollTop();
    grid.fireEvent(Events.BodyScroll, ge);
  }

}
