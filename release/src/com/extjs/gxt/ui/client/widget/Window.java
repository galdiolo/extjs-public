/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.DragListenerAdapter;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A specialized content panel intended for use as an application window.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Activate</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been visually activated via
 * {@link #setActive}.</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Deactivate</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been visually deactivated via
 * {@link #setActive}</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Minimize</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been minimized.</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Maximize</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been maximized.</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Restore</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been restored to its original size after
 * being maximized.</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Resize</b> : WindowEvent(window)<br>
 * <div>Fires after the window has been resized.</div>
 * <ul>
 * <li>window : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : WindowEvent(window, buttonClicked)<br>
 * <div>Fires before the window is to be closed.</div>
 * <ul>
 * <li>window : this</li>
 * <li>buttonClicked : the button that triggered the close event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : WindowEvent(window, buttonClicked)<br>
 * <div>Fires after the window has been closed.</div>
 * <ul>
 * <li>window : this</li>
 * <li>buttonClicked : the button that triggered the close event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeHide</b> : WindowEvent(window, buttonClicked)<br>
 * <div>Fires before the window is to be hidden.</div>
 * <ul>
 * <li>window : this</li>
 * <li>buttonClicked : the button that triggered the hide event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : WindowEvent(window, buttonClicked)<br>
 * <div>Fires after the window has been hidden.</div>
 * <ul>
 * <li>window : this</li>
 * <li>buttonClicked : the button that triggered the hide event</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public class Window extends ContentPanel {

  /**
   * True to display the 'close' tool button and allow the user to close the
   * window, false to hide the button and disallow closing the window (default
   * to true).
   */
  private boolean closable = true;

  /**
   * True to constrain the window to the viewport, false to allow it to fall
   * outside of the viewport (defaults to true).
   */
  private boolean constrain = true;

  /**
   * Widget to be given focus when the window is focused (defaults to null).
   */
  private Widget focusWidget;

  /**
   * True to display the 'maximize' tool button and allow the user to maximize
   * the window, false to hide the button and disallow maximizing the window
   * (defaults to false). Note that when a window is maximized, the tool button
   * will automatically change to a 'restore' button with the appropriate
   * behavior already built-in that will restore the window to its previous
   * size.
   */
  private boolean maximizable;

  /**
   * The minimum height in pixels allowed for this window (defaults to 100).
   * Only applies when resizable = true.
   */
  private int minHeight = 100;

  /**
   * True to display the 'minimize' tool button and allow the user to minimize
   * the window, false to hide the button and disallow minimizing the window
   * (defaults to false). Note that this button provides no implementation --
   * the behavior of minimizing a window is implementation-specific, so the
   * minimize event must be handled and a custom minimize behavior implemented
   * for this option to be useful.
   */
  private boolean minimizable;

  /**
   * The minimum width in pixels allowed for this window (defaults to 200). Only
   * applies when resizable = true.
   */
  private int minWidth = 200;

  /**
   * The width of the window if no width has been specified (defaults to 300).
   */
  private int initialWidth = 300;

  /**
   * True to make the window modal and mask everything behind it when displayed,
   * false to display it without restricting access to other UI elements
   * (defaults to false).
   */
  private boolean modal;

  /**
   * True to blink the window when the user clicks outside of the windows bounds
   * (defaults to false). Only applies window model = true.
   */
  private boolean blinkModal = false;

  /**
   * Allows override of the built-in processing for the escape key. Default
   * action is to close the Window.
   */
  private boolean onEsc = true;

  /**
   * True to render the window body with a transparent background so that it
   * will blend into the framing elements, false to add a lighter background
   * color to visually highlight the body element and separate it more
   * distinctly from the surrounding frame (defaults to false).
   */
  private boolean plain;

  /**
   * True to allow user resizing at each edge and corner of the window, false to
   * disable resizing (defaults to true).
   */
  private boolean resizable = true;

  private int height = Style.DEFAULT;
  private int width = Style.DEFAULT;
  private Draggable dragger;
  private BaseEventPreview eventPreview;
  private Layer ghost;
  private boolean hidden = true;
  private WindowManager manager;
  private ToolButton maxBtn, minBtn;
  private boolean maximized;
  private ModalPanel modalPanel;
  private Resizable resizer;
  private ToolButton restoreBtn, closeBtn;
  private Point restorePos;
  private Size restoreSize;
  private boolean draggable = true;

  /**
   * Creates a new window.
   */
  public Window() {
    baseStyle = "x-window";
    frame = true;
    setShadow(true);
    shim = true;
    setDraggable(true);
  }

  /**
   * Adds a listener to receive window events.
   * 
   * @param listener the listener
   */
  public void addWindowListener(WindowListener listener) {
    TypedListener typedListener = new TypedListener(listener);
    addListener(Events.Activate, typedListener);
    addListener(Events.Deactivate, typedListener);
    addListener(Events.Minimize, typedListener);
    addListener(Events.Maximize, typedListener);
    addListener(Events.Restore, typedListener);
  }

  /**
   * Aligns the window to the specified element.
   * 
   * @param elem the element to align to.
   * @param pos the position to align to (see {@link El#alignTo} for more
   *            details)
   * @param offsets the offsets
   */
  public void alignTo(Element elem, String pos, int[] offsets) {
    Point p = el.getAlignToXY(elem, pos, offsets);
    setPagePosition(p.x, p.y);
  }

  /**
   * Centers the window in the viewport.
   */
  public void center() {
    Point p = el.getAlignToXY(XDOM.getBody(), "c-c", null);
    setPagePosition(p.x, p.y);
  }

  /**
   * Closes the window, removes it from its parent and destroys the window
   * object.
   */
  public void close() {
    close(null);
  }

  /**
   * Focuses the window. If a focusWidget is set, it will receive focus,
   * otherwise the window itself will receive focus.
   */
  public void focus() {
    if (getFocusWidget() != null) {
      if (getFocusWidget() instanceof Component) {
        ((Component) getFocusWidget()).focus();
      } else {
        fly(getFocusWidget().getElement()).focus();
      }
    } else {
      super.focus();
    }
  }

  /**
   * Returns true if the window is constrained.
   * 
   * @return the contstrain state
   */
  public boolean getConstrain() {
    return constrain;
  }

  /**
   * Returns the window's draggable instance.
   * 
   * @return the draggable instance
   */
  public Draggable getDraggable() {
    return dragger;
  }

  /**
   * Returns the focus widget.
   * 
   * @return the focus widget
   */
  public Widget getFocusWidget() {
    return focusWidget;
  }

  /**
   * Returns the window's initial width.
   * 
   * @return the width
   */
  public int getInitialWidth() {
    return initialWidth;
  }

  /**
   * Returns the min height.
   * 
   * @return the min height
   */
  public int getMinHeight() {
    return minHeight;
  }

  /**
   * Returns the min width.
   * 
   * @return the min width
   */
  public int getMinWidth() {
    return minWidth;
  }

  @Override
  public void hide() {
    hide(null);
  }

  /**
   * Hides the window.
   * 
   * @param buttonPressed the button that was pressed or null
   */
  public void hide(Button buttonPressed) {
    if (hidden || !fireEvent(Events.BeforeHide, new WindowEvent(this, buttonPressed))) {
      return;
    }
    hidden = true;
    layer.hideShadow();
    RootPanel.get().remove(this);
    if (isModal()) {
      modalPanel.hide();
    }
    fireEvent(Events.Hide, new WindowEvent(this, buttonPressed));
  }

  /**
   * Returns true if modal blinking is enabled.
   * 
   * @return the blink modal state
   */
  public boolean isBlinkModal() {
    return blinkModal;
  }

  /**
   * Returns true if the window is closable.
   * 
   * @return the closable state
   */
  public boolean isClosable() {
    return closable;
  }

  /**
   * Returns true if window miximizing is enabled.
   * 
   * @return the maximizable state
   */
  public boolean isMaximizable() {
    return maximizable;
  }

  /**
   * Returns true if window minimizing is enabled.
   * 
   * @return the minimizable state
   */
  public boolean isMinimizable() {
    return minimizable;
  }

  /**
   * Returns true if modal behavior is enabled.
   * 
   * @return the modal state
   */
  public boolean isModal() {
    return modal;
  }

  /**
   * Returns true if the window is closed when the esc key is pressed.
   * 
   * @return the on esc state
   */
  public boolean isOnEsc() {
    return onEsc;
  }

  /**
   * Returns true if the plain style is enabled.
   * 
   * @return the plain style state
   */
  public boolean isPlain() {
    return plain;
  }

  /**
   * Returns true if window resizing is enabled.
   * 
   * @return the resizable state
   */
  public boolean isResizable() {
    return resizable;
  }

  /**
   * Fits the window within its current container and automatically replaces the
   * 'maximize' tool button with the 'restore' tool button.
   */
  public void maximize() {
    if (!maximized) {
      restoreSize = getSize();
      restorePos = getPosition(true);
      maximized = true;
      addStyleName("x-window-maximized");
      setPosition(0, 0);
      setSize(XDOM.getViewportSize().width, XDOM.getViewportSize().height);

      maxBtn.setVisible(false);
      restoreBtn.setVisible(true);

      fireEvent(Events.Maximize, new WindowEvent(this));
    }
  }

  /**
   * Placeholder method for minimizing the window. By default, this method
   * simply fires the minimize event since the behavior of minimizing a window
   * is application-specific. To implement custom minimize behavior, either the
   * minimize event can be handled or this method can be overridden.
   */
  public void minimize() {
    fireEvent(Events.Minimize, new WindowEvent(this));
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to remove
   */
  public void removeWindowListener(WindowListener listener) {
    removeListener(Events.Activate, listener);
    removeListener(Events.Deactivate, listener);
    removeListener(Events.Minimize, listener);
    removeListener(Events.Maximize, listener);
    removeListener(Events.Restore, listener);
  }

  /**
   * Restores a maximized window back to its original size and position prior to
   * being maximized and also replaces the 'restore' tool button with the
   * 'maximize' tool button.
   */
  public void restore() {
    if (maximized) {
      el.removeStyleName("x-window-maximized");
      restoreBtn.setVisible(false);
      maxBtn.setVisible(true);
      setPosition(restorePos.x, restorePos.y);
      setSize(restoreSize.width, restoreSize.height);
      restorePos = null;
      restoreSize = null;
      maximized = false;
      fireEvent(Events.Restore, new WindowEvent(this));
    }
  }

  /**
   * Makes this the active window by showing its shadow, or deactivates it by
   * hiding its shadow. This method also fires the activate or deactivate event
   * depending on which action occurred.
   */
  public void setActive(boolean active) {
    if (active) {
      if (!maximized) {

      }
      fireEvent(Events.Activate, new WindowEvent(this));
    } else {
      fireEvent(Events.Deactivate, new WindowEvent(this));
    }
  }

  /**
   * True to blink the window when the user clicks outside of the windows bounds
   * (defaults to false). Only applies window model = true.
   * 
   * @param blinkModal true to blink
   */
  public void setBlinkModal(boolean blinkModal) {
    this.blinkModal = blinkModal;
  }

  /**
   * True to display the 'close' tool button and allow the user to close the
   * window, false to hide the button and disallow closing the window (default
   * to true).
   * 
   * @param closable true to enable closing
   */
  public void setClosable(boolean closable) {
    this.closable = closable;
  }

  /**
   * True to constrain the window to the viewport, false to allow it to fall
   * outside of the viewport (defaults to true).
   * 
   * @param constrain true to constrain, otherwise false
   */
  public void setConstrain(boolean constrain) {
    this.constrain = constrain;
  }

  /**
   * Widget to be given focus when the window is focused).
   * 
   * @param focusWidget the focu widget
   */
  public void setFocusWidget(Widget focusWidget) {
    this.focusWidget = focusWidget;
  }

  /**
   * The width of the window if no width has been specified (defaults to 300).
   * 
   * @param initialWidth the initial width
   */
  public void setInitialWidth(int initialWidth) {
    this.initialWidth = initialWidth;
  }

  /**
   * True to display the 'maximize' tool button and allow the user to maximize
   * the window, false to hide the button and disallow maximizing the window
   * (defaults to false). Note that when a window is maximized, the tool button
   * will automatically change to a 'restore' button with the appropriate
   * behavior already built-in that will restore the window to its previous
   * size.
   * 
   * @param maximizable the maximizable state
   */
  public void setMaximizable(boolean maximizable) {
    this.maximizable = maximizable;
  }

  /**
   * The minimum height in pixels allowed for this window (defaults to 100).
   * Only applies when resizable = true.
   * 
   * @param minHeight the min height
   */
  public void setMinHeight(int minHeight) {
    this.minHeight = minHeight;
  }

  /**
   * True to display the 'minimize' tool button and allow the user to minimize
   * the window, false to hide the button and disallow minimizing the window
   * (defaults to false). Note that this button provides no implementation --
   * the behavior of minimizing a window is implementation-specific, so the
   * minimize event must be handled and a custom minimize behavior implemented
   * for this option to be useful.
   * 
   * @param minimizable true to enabled minimizing
   */
  public void setMinimizable(boolean minimizable) {
    this.minimizable = minimizable;
  }

  /**
   * The minimum width in pixels allowed for this window (defaults to 200). Only
   * applies when resizable = true.
   * 
   * @param minWidth the minimum height
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * True to make the window modal and mask everything behind it when displayed,
   * false to display it without restricting access to other UI elements
   * (defaults to false).
   * 
   * @param modal true for modal
   */
  public void setModal(boolean modal) {
    this.modal = modal;
  }

  /**
   * Allows override of the built-in processing for the escape key. Default
   * action is to close the Window.
   * 
   * @param onEsc true to close window on esc key press
   */
  public void setOnEsc(boolean onEsc) {
    this.onEsc = onEsc;
  }

  /**
   * True to render the window body with a transparent background so that it
   * will blend into the framing elements, false to add a lighter background
   * color to visually highlight the body element and separate it more
   * distinctly from the surrounding frame (defaults to false).
   * 
   * @param plain true to enable the plain style
   */
  public void setPlain(boolean plain) {
    this.plain = plain;
  }

  /**
   * True to allow user resizing at each edge and corner of the window, false to
   * disable resizing (defaults to true).
   * 
   * @param resizable true to enabled resizing
   */
  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  /**
   * Shows the window, rendering it first if necessary, or activates it and
   * brings it to front if hidden.
   */
  public void show() {
    RootPanel.get().add(this);
    el.makePositionable(true);

    if (!hidden) {
      toFront();
      return;
    }

    if (!fireEvent(Events.BeforeShow, new WindowEvent(this))) {
      return;
    }

    beforeShow();
    afterShow();
    return;
  }

  /**
   * Sends this window to the back of (lower z-index than) any other visible
   * windows.
   * 
   * @return this
   */
  public Window toBack() {
    manager.sendToBack(this);
    return this;
  }

  /**
   * Brings this window to the front of any other visible windows.
   * 
   * @return this
   */
  public Window toFront() {
    manager.bringToFront(this);
    focus();
    return this;
  }

  protected void afterRender() {
    super.afterRender();
    el.setVisible(false);
  }

  protected void afterShow() {
    el.setVisible(true);

    if (maximized) {
      maximize();
    }

    hidden = false;

    // no width set
    if (isAutoWidth() || attachSize.width == Style.DEFAULT) {
      setWidth(getInitialWidth());
    }

    // workaround for 0 height east, west resizer
    if (GXT.isIE && isResizable()) {
      el.setHeight(getHeight());
    }

    // not positioned then center
    int x = el.getLeft(false);
    int y = el.getLeft(false);
    if (x < 1 || y < 1) {
      el.center();
    }

    layer.sync(true);

    toFront();

    if (getFocusWidget() != null) {
      if (getFocusWidget() instanceof Component) {
        ((Component) getFocusWidget()).focus();
      } else {
        fly(getFocusWidget().getElement()).focus();
      }
    }
    fireEvent(Events.Show, new WindowEvent(this));
  }

  protected void beforeShow() {
    if (isModal()) {
      modalPanel.blink = isBlinkModal();
      modalPanel.show(this);
      el.makePositionable(true);
    }
  }

  protected void close(Button buttonPressed) {
    if (!fireEvent(Events.BeforeClose, new WindowEvent(this, buttonPressed))) {
      return;
    }
    hide(buttonPressed);
    fireEvent(Events.Close, new WindowEvent(this, buttonPressed));
    destroy();
  }

  protected void initTools() {
    super.initTools();
    if (isMinimizable()) {
      minBtn = new ToolButton("x-tool-minimize");
      head.addTool(minBtn);
    }
    if (isMaximizable()) {
      maxBtn = new ToolButton("x-tool-maximize");
      maxBtn.addSelectionListener(new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          maximize();
        }
      });
      head.addTool(maxBtn);

      restoreBtn = new ToolButton("x-tool-restore");
      restoreBtn.setVisible(false);
      restoreBtn.addSelectionListener(new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          restore();
        }
      });
      head.addTool(restoreBtn);
    }

    if (isMinimizable()) {
      minBtn = new ToolButton("x-tool-minimize");
      minBtn.addSelectionListener(new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          minimize();
        }
      });
    }

    if (isClosable()) {
      closeBtn = new ToolButton("x-tool-close");
      closeBtn.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          hide();
        }
      });
      head.addTool(closeBtn);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (layer != null) {
      layer.destroy();
    }
  }

  protected void onKeyPress(PreviewEvent be) {
    int keyCode = be.getKeyCode();
    if (isOnEsc() && keyCode == KeyboardListener.KEY_ESCAPE) {
      close();
    }
  }
  
  /**
   * True to enable dragging of this Panel (defaults to false).
   * 
   * @param draggable the draggable to state
   */
  public void setDraggable(boolean draggable) {
    this.draggable = draggable;
  }
  
  /**
   * Returns true if the panel is draggable.
   * 
   * @return the draggable state
   */
  public boolean isDraggable() {
    return draggable;
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    el.makePositionable(true);

    if (manager == null) {
      manager = WindowManager.get();
    }

    if (isPlain()) {
      addStyleName("x-window-plain");
    }

    if (resizable) {
      resizer = new Resizable(this);
      resizer.minWidth = getMinWidth();
      resizer.minHeight = getMinHeight();
      resizer.addListener(Events.ResizeStart, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          beforeResize();
        }
      });
    }

    if (draggable) {
      dragger = new Draggable(this, head);
      dragger.constrainClient = getConstrain();
      dragger.sizeProxyToSource = false;
      dragger.addDragListener(new DragListenerAdapter() {
        public void dragEnd(DragEvent de) {
          endDrag(de);
        }

        public void dragMove(DragEvent de) {
          moveDrag(de);
        }

        public void dragStart(DragEvent de) {
          startDrag(de);
        }
      });
      head.setStyleAttribute("cursor", "move");
    }

    if (modal) {
      modalPanel = new ModalPanel();
      modalPanel.blink = isBlinkModal();
    }

    initEventPreview();

    if (width != -1) {
      setWidth(Math.max(getMinWidth(), width));
    }
    if (height != -1) {
      setHeight(Math.max(getMinHeight(), height));
    }
  }

  private void beforeResize() {

  }

  private Layer createGhost() {
    Element div = DOM.createDiv();
    Layer l = new Layer(div);

    l.setStyleName("x-panel-ghost");
    if (head != null) {
      DOM.appendChild(div, el.firstChild().cloneNode(true));
    }
    l.appendChild(DOM.createElement("ul"));
    return l;
  }

  private void endDrag(DragEvent de) {
    unghost(de);
    if (layer != null) {
      layer.enableShadow(getShadow());
    }
  }

  private Layer ghost() {
    Layer g = ghost != null ? ghost : createGhost();
    Rectangle box = getBounds(false);
    g.setBounds(box, true);
    int h = bwrap.getHeight();
    g.getChild(1).setHeight(h - 1, true);
    return g;
  }

  private void initEventPreview() {
    if (eventPreview == null) {
      eventPreview = new BaseEventPreview() {

        @Override
        public boolean onPreview(PreviewEvent pe) {
          if (isModal() && modalPanel != null && modalPanel.isVisible()) {
            modalPanel.relayEvent(pe.event);
          }
          return true;
        }

        @Override
        protected void onPreviewKeyPress(PreviewEvent pe) {
          onKeyPress(pe);
        }
      };
    }
  }

  private void moveDrag(DragEvent de) {

  }

  private void startDrag(DragEvent de) {
    if (layer != null) {
      layer.hideShadow();
    }
    ghost = ghost();
    ghost.setVisible(true);
    el.setVisible(false);
    Draggable d = de.draggable;
    d.setProxy(ghost.dom);
  }

  private void unghost(DragEvent de) {
    ghost.setVisible(false);
    el.setVisible(true);
    setPagePosition(de.x, de.y);
  }
}
