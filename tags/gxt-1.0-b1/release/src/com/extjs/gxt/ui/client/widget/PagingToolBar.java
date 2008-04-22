/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.DataLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.LoadResult;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.messages.MyMessages;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A specialized toolbar that is bound to a {@link DataLoader} and provides
 * automatic paging controls.
 */
public class PagingToolBar extends Component implements Listener {

  /**
   * The paging status message to display (defaults to "Displaying {0} - {1} of
   * {2}"). Note that this string is formatted using the braced numbers 0-2 as
   * tokens that are replaced by the values for start, end and total
   * respectively. These tokens should be preserved when overriding this string
   * if showing those values is desired.
   */
  public String displayMsg;

  /**
   * Customizable piece of the default paging text (defaults to "Page").
   */
  public String beforePageText;

  /**
   * Customizable piece of the default paging text (defaults to "of {0}").
   */
  public String afterPageText;

  /**
   * Customizable piece of the default paging text (defaults to "First Page").
   */
  public String firstText;

  /**
   * Customizable piece of the default paging text (defaults to "Previous
   * Page").
   */
  public String prevText;

  /**
   * Customizable piece of the default paging text (defaults to "Next Page").
   */
  public String nextText;

  /**
   * Customizable piece of the default paging text (defaults to "Last Page").
   */
  public String lastText;

  /**
   * Customizable piece of the default paging text (defaults to "Refresh").
   */
  public String refreshText;

  /**
   * The message to display when no records are found (defaults to "No data to
   * display").
   */
  public String emptyMsg;

  protected Loader loader;
  protected int start, pageSize, totalLength;
  protected int activePage = -1, pages;
  protected ToolBar toolBar;
  protected TextToolItem first, prev, next, last, refresh;
  protected Label afterText;
  protected Label displayText;
  protected TextBox pageText;

  /**
   * Creates a new paging tool bar with the given page size.
   * 
   * @param pageSize the page size
   */
  public PagingToolBar(final int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Binds the toolbar to the loader.
   * 
   * @param loader the loader
   */
  public void bind(Loader loader) {
    if (this.loader != null) {
      this.loader.removeListener(Loader.BeforeLoad, this);
      this.loader.removeListener(Loader.Load, this);
      this.loader.removeListener(Loader.LoadException, this);
    }
    this.loader = loader;
    if (loader != null) {
      loader.addListener(Loader.BeforeLoad, this);
      loader.addListener(Loader.Load, this);
      loader.addListener(Loader.LoadException, this);
    }
  }

  /**
   * Moves to the first page.
   */
  public void first() {
    loader.load(0, pageSize);
  }

  /**
   * Returns the active page.
   * 
   * @return the active page
   */
  public int getActivePage() {
    return activePage;
  }

  /**
   * Returns the current page size.
   * 
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the total number of pages.
   * 
   * @return the
   */
  public int getTotalPages() {
    return pages;
  }

  public void handleEvent(BaseEvent be) {
    switch (be.type) {
      case DataLoader.BeforeLoad:
        disable();
        break;
      case DataLoader.Load:
        onLoad((LoadEvent) be);
        enable();
        break;
      case DataLoader.LoadException:
        enable();
        break;
    }
  }

  /**
   * Moves to the last page.
   */
  public void last() {
    int extra = totalLength % pageSize;
    int lastStart = extra > 0 ? (totalLength - extra) : totalLength - pageSize;
    loader.load(lastStart, pageSize);
  }

  /**
   * Moves to the last page.
   */
  public void next() {
    loader.load(start + pageSize, pageSize);
  }

  /**
   * Moves the the previos page.
   */
  public void previous() {
    loader.load(Math.max(0, start - pageSize), pageSize);
  }

  /**
   * Refreshes the data using the current configuration.
   */
  public void refresh() {
    loader.load(start, pageSize);
  }

  /**
   * Sets the active page.
   * 
   * @param page the page
   */
  public void setActivePage(int page) {
    if (page > pages) {
      last();
      return;
    }
    if (page != activePage && page > 0 && page <= pages) {
      loader.load(--page * pageSize, pageSize);
    } else {
      pageText.setText(String.valueOf((int) activePage));
    }
  }

  /**
   * Sets the current page size. This method does not effect the data currently
   * being displayed. The new page size will not be used until the next load
   * request.
   * 
   * @param pageSize the new page size
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(toolBar);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(toolBar);
  }

  protected void onLoad(LoadEvent event) {
    LoadResult result = event.result;
    start = event.config.getOffset();
    totalLength = result.getTotalLength();
    activePage = (int) Math.ceil((double) (start + pageSize) / pageSize);
    pageText.setText(String.valueOf((int) activePage));
    pages = totalLength < pageSize ? 1 : (int) Math.ceil((double) totalLength / pageSize);

    String after = null, display = null;
    if (afterPageText != null) {
      after = Format.substitute(afterPageText, "" + pages);
    } else {
      after = GXT.MESSAGES.pagingToolBar_afterPageText((int) pages);
    }

    afterText.setText(after);
    first.setEnabled(activePage != 1);
    prev.setEnabled(activePage != 1);
    next.setEnabled(activePage != pages);
    last.setEnabled(activePage != pages);
    int temp = activePage == pages ? totalLength : start + pageSize;

    if (display != null) {
      String[] params = new String[] {"" + (start + 1), "" + temp, "" + totalLength};
      display = Format.substitute(afterPageText, (Object[]) params);
    } else {
      display = GXT.MESSAGES.pagingToolBar_displayMsg(start + 1, (int) temp,
          (int) totalLength);
    }

    String msg = display;
    if (totalLength == 0) {
      msg = emptyMsg;
    }
    displayText.setText(msg);
  }

  protected void onPageChange() {
    String value = pageText.getText();
    if (value.equals("") || !Util.isInteger(value)) {
      pageText.setText(String.valueOf((int) activePage));
      return;
    }
    int p = Integer.parseInt(value);
    setActivePage(p);
  }

  @Override
  protected void onRender(Element target, int index) {
    MyMessages msg = GXT.MESSAGES;

    refreshText = refreshText == null ? msg.pagingToolBar_refreshText() : refreshText;
    nextText = nextText == null ? msg.pagingToolBar_nextText() : nextText;
    prevText = prevText == null ? msg.pagingToolBar_prevText() : prevText;
    firstText = firstText == null ? msg.pagingToolBar_firstText() : firstText;
    lastText = lastText == null ? msg.pagingToolBar_lastText() : lastText;
    beforePageText = beforePageText == null ? msg.pagingToolBar_beforePageText()
        : beforePageText;
    emptyMsg = emptyMsg == null ? msg.pagingToolBar_emptyMsg() : emptyMsg;

    toolBar = new ToolBar();
    toolBar.setHeight(26);

    first = new TextToolItem();
    first.setIconStyle("x-tbar-page-first");
    first.setToolTip(firstText);
    first.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        first();
      }
    });

    prev = new TextToolItem();
    prev.setIconStyle("x-tbar-page-prev");
    prev.setToolTip(prevText);
    prev.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        previous();
      }
    });

    next = new TextToolItem();
    next.setIconStyle("x-tbar-page-next");
    next.setToolTip(nextText);
    next.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        next();
      }
    });

    last = new TextToolItem();
    last.setIconStyle("x-tbar-page-last");
    last.setToolTip(lastText);
    last.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        last();
      }
    });

    refresh = new TextToolItem();
    refresh.setIconStyle("x-tbar-loading");
    refresh.setToolTip(refreshText);
    refresh.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        refresh();
      }
    });

    Label beforePage = new Label(beforePageText);
    beforePage.setStyleName("my-paging-text");
    afterText = new Label();
    afterText.setStyleName("my-paging-text");
    pageText = new TextBox();
    if (!GXT.isGecko && !GXT.isSafari) {
      pageText.addKeyboardListener(new KeyboardListenerAdapter() {
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
          if (keyCode == KeyboardListener.KEY_ENTER) {
            onPageChange();
          }
        }
      });
    }
    pageText.setWidth("30px");

    pageText.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        onPageChange();
      }
    });

    displayText = new Label();
    displayText.setStyleName("my-paging-display");

    toolBar.add(first);
    toolBar.add(prev);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(new AdapterToolItem(beforePage));
    toolBar.add(new AdapterToolItem(pageText));
    toolBar.add(new AdapterToolItem(afterText));
    toolBar.add(new SeparatorToolItem());
    toolBar.add(next);
    toolBar.add(last);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(refresh);
    toolBar.add(new FillToolItem());
    toolBar.add(new AdapterToolItem(displayText));

    toolBar.render(target, index);
    setElement(toolBar.getElement());
  }

}
