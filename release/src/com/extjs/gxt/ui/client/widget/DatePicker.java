/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.Date;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.CompositeElement;
import com.extjs.gxt.ui.client.core.CompositeFunction;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.messages.MyMessages;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

/**
 * Simple date picker.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : DatePickerEvent(datePicker, date)<br>
 * <div>Fires when a date is selected.</div>
 * <ul>
 * <li>datePicker : this</li>
 * <li>date : the selected date</li>
 * </ul>
 * </dd>
 * </dt>
 */
public class DatePicker extends BoxComponent {

  class Header extends Component {

    protected void doAttachChildren() {
      monthBtn.onAttach();
      prevBtn.onAttach();
      nextBtn.onAttach();
    }

    protected void doDetachChildren() {
      monthBtn.onDetach();
      prevBtn.onDetach();
      nextBtn.onDetach();
    }

    @Override
    protected void onRender(Element target, int index) {
      StringBuffer sb = new StringBuffer();
      sb.append("<table width=100% cellpadding=0 cellspacing=0><tr>");
      sb.append("<td class=x-date-left></td><td class=x-date-middle align=center></td>");
      sb.append("<td class=x-date-right></td></tr></table>");

      setElement(XDOM.create(sb.toString()));
      el.insertInto(target, index);

      el.setWidth(175, true);

      String pt = prevText != null ? prevText : messages.datePicker_prevText();
      String nt = nextText != null ? nextText : messages.datePicker_nextText();

      monthBtn = new Button("&#160;", new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          showMonthPicker();
        }
      });

      monthBtn.render(el.selectNode(".x-date-middle").dom);
      monthBtn.el.child("tr").addStyleName("x-btn-with-menu");

      prevBtn = new IconButton("x-date-left-icon", new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          showPrevMonth();
        }
      });
      prevBtn.setToolTip(pt);
      prevBtn.render(el.selectNode(".x-date-left").dom);

      nextBtn = new IconButton("x-date-right-icon", new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          showNextMonth();
        }
      });
      nextBtn.setToolTip(nt);
      nextBtn.render(el.selectNode(".x-date-right").dom);
    }

  }

  /**
   * The text to display on the button that selects the current date (defaults
   * to "Today").
   */
  public String todayText;

  /**
   * The text to display on the ok button.
   */
  public String okText = "&#160;OK&#160;";

  /**
   * The text to display on the cancel button
   */
  public String cancelText;

  /**
   * The tooltip to display for the button that selects the current date
   * (defaults to "{current date} (Spacebar)").
   */
  public String todayTip;

  /**
   * The error text to display if the minDate validation fails (defaults to
   * "This date is before the minimum date").
   */
  public String minText;

  /**
   * The error text to display if the maxDate validation fails (defaults to
   * "This date is after the maximum date").
   */
  public String maxText;

  /**
   * The previous month navigation button tooltip (defaults to 'Previous Month
   * (Control+Left)').
   */
  public String prevText;

  /**
   * The next month navigation button tooltip (defaults to 'Next Month
   * (Control+Right)').
   */
  public String nextText;

  /**
   * The header month selector tooltip (defaults to 'Choose a month
   * (Control+Up/Down to move years)').
   */
  public String monthYearText;
  
  private int firstDOW;
  private Date minDate;
  private Date maxDate;
  private int startDay;
  private long today;
  private int mpyear;
  private Grid days, grid;
  private Component header;
  private HorizontalPanel footer;
  private DateWrapper activeDate, value;
  private int mpSelMonth, mpSelYear;
  private Button todayBtn, monthBtn;
  private Element[] cells;
  private Element[] textNodes;
  private IconButton prevBtn, nextBtn;
  private CompositeElement mpMonths, mpYears;
  private El monthPicker;
  private DateTimeConstants constants = (DateTimeConstants) GWT.create(DateTimeConstants.class);
  private MyMessages messages = (MyMessages) GWT.create(MyMessages.class);

  /**
   * Creates a new date picker.
   */
  public DatePicker() {
    baseStyle = "x-date-picker";
  }

  @Override
  public void focus() {
    super.focus();
    {
      update(activeDate);
    }
  }

  /**
   * Returns the field's maximum allowed date.
   * 
   * @return the max date
   */
  public Date getMaxDate() {
    return maxDate;
  }

  /**
   * Returns the picker's minimum data.
   * 
   * @return the min date
   */
  public Date getMinDate() {
    return minDate;
  }

  /**
   * Returns the picker's start day.
   * 
   * @return the start day
   */
  public int getStartDay() {
    return startDay;
  }

  /**
   * Gets the current selected value of the date field.
   * 
   * @return the date
   */
  public Date getValue() {
    return value.asDate();
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    switch (ce.getEventType()) {
      case Event.ONCLICK:
        onClick(ce);
        return;
    }
  }

  /**
   * Sets the picker's maximum allowed date.
   * 
   * @param maxDate the max date
   */
  public void setMaxDate(Date maxDate) {
    this.maxDate = maxDate;
  }

  /**
   * Sets the picker's minimum allowed date.
   * 
   * @param minDate the min date
   */
  public void setMinDate(Date minDate) {
    this.minDate = minDate;
  }

  /**
   * Sets the picker's start day
   * 
   * @param startDay the start day
   */
  public void setStartDay(int startDay) {
    this.startDay = startDay;
  }

  /**
   * Sets the value of the date field.
   * 
   * @param date the date
   */
  public void setValue(Date date) {
    setValue(date, false);
  }

  /**
   * Sets the value of the date field.
   * 
   * @param date the date
   * @param supressEvent true to spress the select event
   */
  public void setValue(Date date, boolean supressEvent) {
    this.value = new DateWrapper(date).clearTime();
    if (rendered) {
      update(value);
    }
    if (!supressEvent) {
      DatePickerEvent de = new DatePickerEvent(this);
      de.date = date;
      fireEvent(Events.Select, de);
    }

  }

  @Override
  protected void doAttachChildren() {
    header.onAttach();
    footer.onAttach();
    WidgetHelper.doAttach(grid);
  }

  @Override
  protected void doDetachChildren() {
    header.onDetach();
    footer.onDetach();
    WidgetHelper.doDetach(grid);
    monthPicker.setVisible(false);
  }

  protected void onClick(ComponentEvent be) {
    be.stopEvent();
    El target = be.getTargetEl();
    El pn = null;
    String cls = target.getStyleName();
    if (cls.equals("x-date-left-a")) {
      showPrevMonth();
    } else if (cls.equals("x-date-right-a")) {
      showNextMonth();
    }
    if ((pn = target.up("td.x-date-mp-month", 2)) != null) {
      mpMonths.removeStyleName("x-date-mp-sel");
      El elem = target.up("td.x-date-mp-month", 2);
      elem.addStyleName("x-date-mp-sel");
      mpSelMonth = pn.getIntElementAttribute("xmonth");
    } else if ((pn = target.up("td.x-date-mp-year", 2)) != null) {
      mpYears.removeStyleName("x-date-mp-sel");
      El elem = target.up("td.x-date-mp-year", 2);
      elem.addStyleName("x-date-mp-sel");
      mpSelYear = pn.getIntElementAttribute("xyear");
    } else if (target.is("button.x-date-mp-ok")) {
      DateWrapper d = new DateWrapper(mpSelYear, mpSelMonth, activeDate.getDate());
      update(d);
      hideMonthPicker();
    } else if (target.is("button.x-date-mp-cancel")) {
      hideMonthPicker();
    } else if (target.is("a.x-date-mp-prev")) {
      updateMPYear(mpyear - 10);
    } else if (target.is("a.x-date-mp-next")) {
      updateMPYear(mpyear + 10);
    }

    if (GXT.isSafari) {
      focus();
    }
  }

  protected void onDayClick(ComponentEvent ce) {
    ce.stopEvent();
    El target = ce.getTargetEl();
    El el = target.findParent("a", 5);

    if (el != null) {
      String dt = el.getElementAttribute("dateValue");
      if (dt != null) {
        handleDateClick(el, dt);
        return;
      }
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);

    if (minText == null) {
      minText = GXT.MESSAGES.datePicker_minText();
    }
    if (maxText == null) {
      maxText = GXT.MESSAGES.datePicker_maxText();
    }

    String tt = todayText != null ? todayText : messages.datePicker_todayText();

    header = new Header();
    header.render(getElement());

    days = new Grid(1, 7);
    days.setStyleName("x-date-days");
    days.setCellPadding(0);
    days.setCellSpacing(0);
    days.setBorderWidth(0);

    String[] dn = constants.narrowWeekdays();
    firstDOW = Integer.parseInt(constants.firstDayOfTheWeek()) - 1;

    days.setHTML(0, 0, "<span>" + dn[(0 + firstDOW) % 7] + "</span>");
    days.setHTML(0, 1, "<span>" + dn[(1 + firstDOW) % 7]);
    days.setHTML(0, 2, "<span>" + dn[(2 + firstDOW) % 7]);
    days.setHTML(0, 3, "<span>" + dn[(3 + firstDOW) % 7]);
    days.setHTML(0, 4, "<span>" + dn[(4 + firstDOW) % 7]);
    days.setHTML(0, 5, "<span>" + dn[(5 + firstDOW) % 7]);
    days.setHTML(0, 6, "<span>" + dn[(6 + firstDOW) % 7]);

    grid = new Grid(6, 7);
    grid.setStyleName("x-date-inner");
    grid.setCellSpacing(0);
    grid.setCellPadding(0);
    grid.addTableListener(new TableListener() {
      public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        Event event = DOM.eventGetCurrentEvent();
        ComponentEvent be = new ComponentEvent(DatePicker.this, event);
        onDayClick(be);
      }
    });

    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 7; col++) {
        grid.setHTML(row, col, "<a href=#><span></span></a>");
      }
    }

    footer = new HorizontalPanel();
    footer.setTableWidth("100%");
    footer.setHorizontalAlign(HorizontalAlignment.CENTER);

    footer.setStyleName("x-date-bottom");
    footer.setWidth(175);

    todayBtn = new Button(tt, new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        selectToday();
      }
    });
    footer.add(todayBtn);

    monthPicker = new El(DOM.createDiv());
    monthPicker.setStyleName("x-date-mp");

    el.appendChild(header.getElement());
    el.appendChild(days.getElement());
    el.appendChild(grid.getElement());
    el.appendChild(footer.getElement());
    el.appendChild(monthPicker.dom);

    el.setWidth(175);

    cells = el.select("table.x-date-inner tbody td");
    textNodes = el.query("table.x-date-inner tbody span");

    activeDate = value != null ? value : new DateWrapper();
    update(activeDate);

    el.addEventsSunk(Event.ONCLICK);
    el.makePositionable();

  }

  private void createMonthPicker() {
    String ok = okText != null ? okText : messages.datePicker_okText();
    String cancel = cancelText != null ? cancelText : messages.datePicker_cancelText();

    StringBuffer buf = new StringBuffer();
    buf.append("<table border=0 cellspacing=0>");
    String[] monthNames = constants.shortMonths();
    for (int i = 0; i < 6; i++) {
      buf.append("<tr><td class=x-date-mp-month><a href=#>" + monthNames[i] + "</a></td>");
      buf.append("<td class='x-date-mp-month x-date-mp-sep'><a href=#>"
          + monthNames[i + 6] + "</a></td>");
      if (i == 0) {
        buf.append("<td class=x-date-mp-ybtn align=center><a class=x-date-mp-prev href=#></a></td><td class='x-date-mp-ybtn' align=center><a class='x-date-mp-next'></a></td></tr>");
      } else {
        buf.append("<td class='x-date-mp-year'><a href='#'></a></td><td class='x-date-mp-year'><a href='#'></a></td></tr>");
      }
    }
    buf.append("<tr class=x-date-mp-btns><td colspan='4'><button type='button' class='x-date-mp-ok'>");
    buf.append(ok);
    buf.append("</button><button type=button class=x-date-mp-cancel>");
    buf.append(cancel);
    buf.append("</button></td></tr></table>");

    monthPicker.update(buf.toString());

    mpMonths = new CompositeElement(monthPicker.select("td.x-date-mp-month"));
    mpYears = new CompositeElement(monthPicker.select("td.x-date-mp-year"));

    mpMonths.each(new CompositeFunction() {

      public void doFunction(Element elem, CompositeElement ce, int index) {
        index += 1;
        if (index % 2 == 0) {
          fly(elem).setElementAttribute("xmonth", (int) (5 + (Math.round(index * .5))));
        } else {
          fly(elem).setElementAttribute("xmonth", (int) (Math.round((index - 1) * .5)));
        }
      }

    });

  }

  private void handleDateClick(El target, String dt) {
    Date d = new Date(Long.valueOf(dt));
    if (d != null && !target.getParent().hasStyleName("x-date-disabled")) {
      setValue(d);
    }
  }

  private void hideMonthPicker() {
    monthPicker.slideOut(Direction.UP, 300, new Listener<FxEvent>() {
      public void handleEvent(FxEvent ce) {
        monthPicker.setVisible(false);
      }
    });
  }

  private void selectToday() {
    setValue(new DateWrapper().asDate());
  }

  private void setCellStyle(Element cell, Date d, long sel, long min, long max) {
    long t = d.getTime();
    El cellEl = new El(cell);
    cellEl.firstChild().setElementAttribute("dateValue", "" + t);
    if (t == today) {
      cellEl.addStyleName("x-date-today");
      cellEl.setTitle(todayText);
    }
    if (t == sel) {
      cellEl.addStyleName("x-date-selected");
    }
    if (t < min) {
      cellEl.addStyleName("x-date-disabled");
      cellEl.setTitle(minText);
    }
    if (t > max) {
      cellEl.addStyleName("x-date-disabled");
      cellEl.setTitle(maxText);
    }
  }

  private void showMonthPicker() {

    createMonthPicker();

    Size s = el.getSize(true);
    s.height -= 2;
    monthPicker.setTop(1);
    monthPicker.setSize(s.width, s.height);
    monthPicker.firstChild().setSize(s.width, s.height, true);

    mpSelMonth = (activeDate != null ? activeDate : value).getMonth();

    updateMPMonth(mpSelMonth);
    mpSelYear = (activeDate != null ? activeDate : value).getFullYear();
    updateMPYear(mpSelYear);

    monthPicker.enableDisplayMode("block");
    monthPicker.makePositionable(true);
    monthPicker.slideIn(Direction.DOWN);

  }

  private void showNextMonth() {
    update(activeDate.addMonths(1));
  }

  private void showPrevMonth() {
    update(activeDate.addMonths(-1));
  }

  private void update(DateWrapper date) {
    DateWrapper vd = activeDate;
    activeDate = date;
    if (vd != null && el != null) {
      if (vd.getMonth() == activeDate.getMonth()
          && vd.getFullYear() == activeDate.getFullYear()) {

      }
      int days = date.getDaysInMonth();
      DateWrapper firstOfMonth = date.getFirstDayOfMonth();
      int startingPos = firstOfMonth.getDayInWeek() - startDay - firstDOW;

      if (startingPos <= startDay) {
        startingPos += 7;
      }

      DateWrapper pm = activeDate.addMonths(-1);
      int prevStart = pm.getDaysInMonth() - startingPos;

      days += startingPos;

      DateWrapper d = new DateWrapper(pm.getFullYear(), pm.getMonth(), prevStart).clearTime();
      today = new DateWrapper().clearTime().getTime();
      long sel = activeDate.clearTime().getTime();
      long min = minDate != null ? new DateWrapper(minDate).getTime() : Long.MIN_VALUE;
      long max = maxDate != null ? new DateWrapper(maxDate).getTime() : Long.MAX_VALUE;

      int i = 0;
      for (; i < startingPos; i++) {
        fly(textNodes[i]).update("" + ++prevStart);
        d = d.addDays(1);
        fly(cells[i]).setStyleName("x-date-prevday");
        setCellStyle(cells[i], d.asDate(), sel, min, max);
      }
      for (; i < days; i++) {
        int intDay = i - startingPos + 1;
        fly(textNodes[i]).update("" + intDay);
        d = d.addDays(1);
        fly(cells[i]).setStyleName("x-date-active");
        setCellStyle(cells[i], d.asDate(), sel, min, max);
      }
      int extraDays = 0;
      for (; i < 42; i++) {
        fly(textNodes[i]).update("" + ++extraDays);
        d = d.addDays(1);
        fly(cells[i]).setStyleName("x-date-nextday");
        setCellStyle(cells[i], d.asDate(), sel, min, max);
      }

      int month = activeDate.getMonth();
      monthBtn.setText(constants.standaloneMonths()[month] + " " + activeDate.getFullYear());
    }
  }

  private void updateMPMonth(int month) {
    for (int i = 0; i < mpMonths.getCount(); i++) {
      Element elem = mpMonths.item(i);
      int xmonth = fly(elem).getIntElementAttribute("xmonth");
      fly(elem).setStyleName("x-date-mp-sel", xmonth == month);
    }
  }

  private void updateMPYear(int year) {
    mpyear = year;

    for (int i = 1; i <= 10; i++) {
      El td = new El(mpYears.item(i - 1));
      int y2;
      if (i % 2 == 0) {
        y2 = (int) (year + (Math.round(i * .5)));
        td.firstChild().update("" + y2);
        td.setElementAttribute("xyear", y2);
      } else {
        y2 = (int) (year - (5 - Math.round(i * .5)));
        td.firstChild().update("" + y2);
        td.setElementAttribute("xyear", y2);
      }
      fly(mpYears.item(i - 1)).setStyleName("x-date-mp-sel", y2 == year);
    }
  }
}
