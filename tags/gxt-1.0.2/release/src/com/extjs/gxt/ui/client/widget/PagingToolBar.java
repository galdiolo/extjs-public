/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
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
 * A specialized toolbar that is bound to a {@link ListLoader} and provides
 * automatic paging controls.
 */
public class PagingToolBar extends Component implements Listener {

  public class PagingToolBarMessages {
    private String afterPageText;
    private String beforePageText;
    private String displayMsg;
    private String emptyMsg;
    private String firstText;
    private String lastText;
    private String nextText;
    private String prevText;
    private String refreshText;

    /**
     * Returns the after page text.
     * 
     * @return the after page text
     */
    public String getAfterPageText() {
      return afterPageText;
    }

    /**
     * Returns the before page text.
     * 
     * @return the before page text
     */
    public String getBeforePageText() {
      return beforePageText;
    }

    /**
     * Returns the display message.
     * 
     * @return the display message.
     */
    public String getDisplayMsg() {
      return displayMsg;
    }

    /**
     * Returns the empty message.
     * 
     * @return the empty message
     */
    public String getEmptyMsg() {
      return emptyMsg;
    }

    public String getFirstText() {
      return firstText;
    }

    /**
     * Returns the last text.
     * 
     * @return the last text
     */
    public String getLastText() {
      return lastText;
    }

    /**
     * Returns the next text.
     * 
     * @return the next ext
     */
    public String getNextText() {
      return nextText;
    }

    /**
     * Returns the previous text.
     * 
     * @return the previous text
     */
    public String getPrevText() {
      return prevText;
    }

    /**
     * Returns the refresh text.
     * 
     * @return the refresh text
     */
    public String getRefreshText() {
      return refreshText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "of {0}").
     * 
     * @param afterPageText the after page text
     */
    public void setAfterPageText(String afterPageText) {
      this.afterPageText = afterPageText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "Page").
     * 
     * @param beforePageText the before page text
     */
    public void setBeforePageText(String beforePageText) {
      this.beforePageText = beforePageText;
    }

    /**
     * The paging status message to display (defaults to "Displaying {0} - {1}
     * of {2}"). Note that this string is formatted using the braced numbers 0-2
     * as tokens that are replaced by the values for start, end and total
     * respectively. These tokens should be preserved when overriding this
     * string if showing those values is desired.
     * 
     * @param displayMsg the display message
     */
    public void setDisplayMsg(String displayMsg) {
      this.displayMsg = displayMsg;
    }

    /**
     * The message to display when no records are found (defaults to "No data to
     * display").
     * 
     * @param emptyMsg the empty message
     */
    public void setEmptyMsg(String emptyMsg) {
      this.emptyMsg = emptyMsg;
    }

    /**
     * Customizable piece of the default paging text (defaults to "First Page").
     * 
     * @param firstText the first text
     */
    public void setFirstText(String firstText) {
      this.firstText = firstText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "Last Page").
     * 
     * @param lastText the last text
     */
    public void setLastText(String lastText) {
      this.lastText = lastText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "Next Page").
     * 
     * @param nextText the next text
     */
    public void setNextText(String nextText) {
      this.nextText = nextText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "Previous
     * Page").
     * 
     * @param prevText the prev text
     */
    public void setPrevText(String prevText) {
      this.prevText = prevText;
    }

    /**
     * Customizable piece of the default paging text (defaults to "Refresh").
     * 
     * @param refreshText the refresh text
     */
    public void setRefreshText(String refreshText) {
      this.refreshText = refreshText;
    }

  }

  protected PagingLoader loader;
  protected int start, pageSize, totalLength;
  protected int activePage = -1, pages;
  protected ToolBar toolBar;
  protected TextToolItem first, prev, next, last, refresh;
  protected Label afterText;
  protected Label displayText;
  protected TextBox pageText;
  protected PagingToolBarMessages msgs;
  
  private LoadEvent<PagingLoadConfig, PagingLoadResult> renderEvent;

  /**
   * Creates a new paging tool bar with the given page size.
   * 
   * @param pageSize the page size
   */
  public PagingToolBar(final int pageSize) {
    this.pageSize = pageSize;
    msgs = new PagingToolBarMessages();
  }

  /**
   * Binds the toolbar to the loader.
   * 
   * @param loader the loader
   */
  public void bind(PagingLoader loader) {
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
   * Clears the current toolbar text.
   */
  public void clear() {
    if (rendered) {
      pageText.setText("");
      afterText.setText("");
      displayText.setText("");
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
   * Returns the tool bar's messages.
   * 
   * @return the messages
   */
  public PagingToolBarMessages getMessages() {
    return msgs;
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
      case Loader.BeforeLoad:
        disable();
        break;
      case Loader.Load:
        onLoad((LoadEvent) be);
        enable();
        break;
      case Loader.LoadException:
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
   * Sets the active page (1 to page count inclusive).
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
   * Sets the tool bar's messages.
   * 
   * @param messages the messages
   */
  public void setMessages(PagingToolBarMessages messages) {
    msgs = messages;
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

  protected void onLoad(LoadEvent<PagingLoadConfig, PagingLoadResult> event) {
    if (!rendered) {
      renderEvent = event;
      return;
    }
    PagingLoadResult result = event.data;
    start = event.data.getOffset();
    totalLength = result.getTotalLength();
    activePage = (int) Math.ceil((double) (start + pageSize) / pageSize);
    pageText.setText(String.valueOf((int) activePage));
    pages = totalLength < pageSize ? 1 : (int) Math.ceil((double) totalLength / pageSize);

    String after = null, display = null;
    if (msgs.getAfterPageText() != null) {
      after = Format.substitute(msgs.getAfterPageText(), "" + pages);
    } else {
      after = GXT.MESSAGES.pagingToolBar_afterPageText((int) pages);
    }

    afterText.setText(after);
    first.setEnabled(activePage != 1);
    prev.setEnabled(activePage != 1);
    next.setEnabled(activePage != pages);
    last.setEnabled(activePage != pages);
    int temp = activePage == pages ? totalLength : start + pageSize;

    if (msgs.getDisplayMsg() != null) {
      String[] params = new String[] {"" + (start + 1), "" + temp, "" + totalLength};
      display = Format.substitute(msgs.getDisplayMsg(), (Object[]) params);
    } else {
      display = GXT.MESSAGES.pagingToolBar_displayMsg(start + 1, (int) temp, (int) totalLength);
    }

    String msg = display;
    if (totalLength == 0) {
      msg = msgs.getEmptyMsg();
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

    msgs.setRefreshText(msgs.getRefreshText() == null ? msg.pagingToolBar_refreshText()
        : msgs.getRefreshText());
    msgs.setNextText(msgs.getNextText() == null ? msg.pagingToolBar_nextText() : msgs.getNextText());
    msgs.setPrevText(msgs.getPrevText() == null ? msg.pagingToolBar_prevText() : msgs.getPrevText());
    msgs.setFirstText(msgs.getFirstText() == null ? msg.pagingToolBar_firstText()
        : msgs.getFirstText());
    msgs.setLastText(msgs.getLastText() == null ? msg.pagingToolBar_lastText() : msgs.getLastText());
    msgs.setBeforePageText(msgs.getBeforePageText() == null ? msg.pagingToolBar_beforePageText()
        : msgs.getBeforePageText());
    msgs.setEmptyMsg(msgs.getEmptyMsg() == null ? msg.pagingToolBar_emptyMsg() : msgs.getEmptyMsg());

    toolBar = new ToolBar();

    first = new TextToolItem();
    first.setIconStyle("x-tbar-page-first");
    first.setToolTip(msgs.getFirstText());
    first.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        first();
      }
    });

    prev = new TextToolItem();
    prev.setIconStyle("x-tbar-page-prev");
    prev.setToolTip(msgs.getPrevText());
    prev.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        previous();
      }
    });

    next = new TextToolItem();
    next.setIconStyle("x-tbar-page-next");
    next.setToolTip(msgs.getNextText());
    next.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        next();
      }
    });

    last = new TextToolItem();
    last.setIconStyle("x-tbar-page-last");
    last.setToolTip(msgs.getLastText());
    last.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        last();
      }
    });

    refresh = new TextToolItem();
    refresh.setIconStyle("x-tbar-loading");
    refresh.setToolTip(msgs.getRefreshText());
    refresh.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        refresh();
      }
    });

    Label beforePage = new Label(msgs.getBeforePageText());
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

    if (XDOM.isVisibleBox) {
      toolBar.setHeight(25);
    }
    
    if (renderEvent != null) {
      onLoad(renderEvent);
      renderEvent = null;
    }
  }

}
