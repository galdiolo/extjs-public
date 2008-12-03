/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client;

import com.google.gwt.user.client.Event;

/**
 * Defines the GXT event types.
 * 
 * <p /> The maximum value of all GXT defined events is 2000, see
 * {@value #GXT_MAX_EVENT}. All user defined event constants should be
 * greater than 2000.
 */
public class Events {

  protected Events() {

  }

  /**
   * The maximum value for all GXT defined event constants.
   */
  public static final int GXT_MAX_EVENT = 2000;

  /**
   * DOM ONCLICK event type.
   */
  public static final int OnClick = Event.ONCLICK;

  /**
   * DOM ONDBLCLICK event type.
   */
  public static final int OnDoubleClick = Event.ONDBLCLICK;

  /**
   * DOM ONMOUSEDOWN event type.
   */
  public static final int OnMouseDown = Event.ONMOUSEDOWN;

  /**
   * DOM ONMOUSEUP event type.
   */
  public static final int OnMouseUp = Event.ONMOUSEUP;

  /**
   * DOM ONMOUSEOVER event type.
   */
  public static final int OnMouseOver = Event.ONMOUSEOVER;

  /**
   * DOM ONMOUSEOUT event type.
   */
  public static final int OnMouseOut = Event.ONMOUSEOUT;

  /**
   * DOM ONMOUSEMOVE event type.
   */
  public static final int OnMouseMove = Event.ONMOUSEMOVE;

  /**
   * DOM ONKEYDOWN event type.
   */
  public static final int OnKeyDown = Event.ONKEYDOWN;

  /**
   * DOM ONKEYUP event type.
   */
  public static final int OnKeyUp = Event.ONKEYUP;

  /**
   * Activate event type (value is 10).
   */
  public static final int Activate = 10;

  /**
   * AfterEdit event type (value is 11).
   */
  public static final int AfterEdit = 11;

  /**
   * AfterLayout event type (value is 12).
   */
  public static final int AfterLayout = 12;

  /**
   * Add event type (value is 20).
   */
  public static final int Add = 20;

  /**
   * ArrowClick event type (value is 22).
   */
  public static final int ArrowClick = 22;

  /**
   * Attach event type (value is 30).
   */
  public static final int Attach = 30;

  /**
   * BeforeAdd event type (value is 40).
   */
  public static final int BeforeAdd = 40;

  /**
   * BeforeCheckChange event type (value is 50).
   */
  public static final int BeforeCheckChange = 50;

  /**
   * Close event type (value is 60).
   */
  public static final int BeforeClose = 60;

  /**
   * BeforeCollapse event type (value is 70).
   */
  public static final int BeforeCollapse = 70;

  /**
   * BeforeComplete event type (value is 71).
   */
  public static final int BeforeComplete = 71;

  /**
   * BeforeEdit event type (value is 72).
   */
  public static final int BeforeEdit = 72;

  /**
   * BeforeExpand event type (value is 80).
   */
  public static final int BeforeExpand = 80;

  /**
   * BeforeHide event type (value is 85).
   */
  public static final int BeforeHide = 85;

  /**
   * BeforeOpen event type (value is 90).
   */
  public static final int BeforeOpen = 90;

  /**
   * BeforeRemove event type (value is 100).
   */
  public static final int BeforeRemove = 100;

  /**
   * Render event type (value is 110).
   */
  public static final int BeforeRender = 110;

  /**
   * BeforeStateRestore event type (value is 112).
   */
  public static final int BeforeStateRestore = 112;

  /**
   * BeforeSelect event type (value is 120).
   */
  public static final int BeforeSelect = 120;

  /**
   * BeforeStateSave event type (value is 122).
   */
  public static final int BeforeStateSave = 122;

  /**
   * BeforeSubmit event type (value is 124).
   */
  public static final int BeforeSubmit = 124;

  /**
   * BeforeShow event type (value is 130).
   */
  public static final int BeforeShow = 130;

  /**
   * BeforeStartEdit event type (value is 132).
   */
  public static final int BeforeStartEdit = 132;

  /**
   * Blur event type (value is 140).
   */
  public static final int Blur = 140;

  /**
   * Blur event type (value is 141).
   */
  public static final int BodyScroll = 141;

  /**
   * BrowserEvent event type (value is 142).
   */
  public static final int BrowserEvent = 142;

  /**
   * CellClick event type (value is 150).
   */
  public static final int CellClick = 150;

  /**
   * CellDoubleClick event type (value is 160).
   */
  public static final int CellDoubleClick = 160;

  /**
   * CellMouseDown event type (value is 164).
   */
  public static final int CellMouseDown = 164;

  /**
   * Change event type (value is 170).
   */
  public static final int Change = 170;

  /**
   * CheckChange event type (value is 180).
   */
  public static final int CheckChange = 180;

  /**
   * Clear event type (value is 180).
   */
  public static final int Clear = 182;

  /**
   * Close event type (value is 200).
   */
  public static final int Close = 200;

  /**
   * Collapse event type (value is 210).
   */
  public static final int Collapse = 210;

  /**
   * ColumnClick event type (value is 220).
   */
  public static final int ColumnClick = 220;

  /**
   * ColumnResize event type (value is 222).
   */
  public static final int ColumnResize = 222;

  /**
   * Complete event type (value is 224).
   */
  public static final int Complete = 224;

  /**
   * ContextMenu event type (value is 230).
   */
  public static final int ContextMenu = 230;

  /**
   * Deactivate event type (value is 240).
   */
  public static final int Deactivate = 240;

  /**
   * Detach event type (value is 260).
   */
  public static final int Detach = 260;

  /**
   * Disable event type (value is 270).
   */
  public static final int Disable = 270;

  /**
   * DragCancel event type (value is 290).
   */
  public static final int DragCancel = 290;

  /**
   * DragEnd event type (value is 300).
   */
  public static final int DragEnd = 300;

  /**
   * DragEnter event type (value is 302).
   */
  public static final int DragEnter = 302;

  /**
   * DragLeave event type (value is 304).
   */
  public static final int DragLeave = 304;

  /**
   * DragMove event type (value is 310).
   */
  public static final int DragMove = 310;

  /**
   * DragStart event type (value is 320).
   */
  public static final int DragStart = 320;

  /**
   * Drop event type (value is 322).
   */
  public static final int Drop = 322;

  /**
   * EffectCancel event type (value is 330).
   */
  public static final int EffectCancel = 330;

  /**
   * EffectComplete event type (value is 340).
   */
  public static final int EffectComplete = 340;

  /**
   * EffectStart event type (value is 350).
   */
  public static final int EffectStart = 350;

  /**
   * Enable event type (value is 360).
   */
  public static final int Enable = 360;

  /**
   * Expand event type (value is 370).
   */
  public static final int Expand = 370;

  /**
   * Focus event type (value is 380).
   */
  public static final int Focus = 380;

  /**
   * HeaderChange event type (value is 382).
   */
  public static final int HeaderChange = 382;

  /**
   * HeaderClick event type (value is 383).
   */
  public static final int HeaderClick = 383;

  /**
   * HeaderContextMenu event type (value is 385).
   */
  public static final int HeaderContextMenu = 385;

  /**
   * HeaderDoubleClick event type (value is 384).
   */
  public static final int HeaderDoubleClick = 384;

  /**
   * HeaderMouseDown event type (value is 386).
   */
  public static final int HeaderMouseDown = 386;

  /**
   * HiddenChange event type (value is 388).
   */
  public static final int HiddenChange = 388;

  /**
   * Hide event type (value is 390).
   */
  public static final int Hide = 390;

  /**
   * Invalid event type (value is 400).
   */
  public static final int Invalid = 400;

  /**
   * KeyPress event type (value is 420).
   */
  public static final int KeyPress = 420;

  /**
   * KeyUp event type (value is 425).
   */
  public static final int KeyUp = 425;

  /**
   * KeyDown event type (value is 430).
   */
  public static final int KeyDown = 430;

  /**
   * Minimize event type (value is 440).
   */
  public static final int Maximize = 440;

  /**
   * MenuHide event type (value is 450).
   */
  public static final int MenuHide = 450;

  /**
   * MenuShow event type (value is 460).
   */
  public static final int MenuShow = 460;

  /**
   * Minimize event type (value is 470).
   */
  public static final int Minimize = 470;

  /**
   * Resize event type (value is 530).
   */
  public static final int Move = 530;

  /**
   * Open event type (value is 540).
   */
  public static final int Open = 540;

  /**
   * Refresh event type (value is 548).
   */
  public static final int Refresh = 548;

  /**
   * Register event type (value is 549).
   */
  public static final int Register = 549;

  /**
   * Remove event type (value is 550).
   */
  public static final int Remove = 550;

  /**
   * Render event type (value is 560).
   */
  public static final int Render = 560;

  /**
   * Resize event type (value is 570).
   */
  public static final int Resize = 570;

  /**
   * ResizeEnd event type (value is 580).
   */
  public static final int ResizeEnd = 580;

  /**
   * ResizeStart event type (value is 590).
   */
  public static final int ResizeStart = 590;

  /**
   * Minimize event type (value is 600).
   */
  public static final int Restore = 600;

  /**
   * RowClick event type (value is 610).
   */
  public static final int RowClick = 610;

  /**
   * RowDoubleClick event type (value is 620).
   */
  public static final int RowDoubleClick = 620;

  /**
   * RowMouseDown event type (value is 624).
   */
  public static final int RowMouseDown = 624;

  /**
   * RowUpdated event type (value is 626).
   */
  public static final int RowUpdated = 626;

  /**
   * Scroll event type (value is 630).
   */
  public static final int Scroll = 630;

  /**
   * Select event type (value is 640).
   */
  public static final int Select = 640;

  /**
   * SelectionChange event type (value is 650).
   */
  public static final int SelectionChange = 650;

  /**
   * Show event type (value is 660).
   */
  public static final int Show = 660;

  /**
   * SortChange event type (value is 670).
   */
  public static final int SortChange = 670;

  /**
   * SpecialKey event type (value is 672).
   */
  public static final int SpecialKey = 672;

  /**
   * StartEdit event type value is (674).
   */
  public static final int StartEdit = 674;

  /**
   * StateChange event type (value is 680).
   */
  public static final int StateChange = 680;

  /**
   * StateSave event type (value is 682).
   */
  public static final int StateSave = 682;

  /**
   * StateRestore event type (value is 684).
   */
  public static final int StateRestore = 684;

  /**
   * Submit event type (value is 686).
   */
  public static final int Submit = 686;

  /**
   * Toggle event type (value is 690).
   */
  public static final int Toggle = 690;

  /**
   * TriggerClick event type (value is 692);
   */
  public static final int TriggerClick = 692;

  /**
   * TwinTriggerClick event type (value is 694);
   */
  public static final int TwinTriggerClick = 694;

  /**
   * Change event type (value is 700).
   */
  public static final int Update = 700;

  /**
   * Unregister event type (value is 702).
   */
  public static final int Unregister = 702;

  /**
   * Valid event type (value is 710).
   */
  public static final int Valid = 710;

  /**
   * Validate drop event type (value is 715).
   */
  public static final int ValidateDrop = 715;

  /**
   * ValidateEdit event type (value is 711).
   */
  public static final int ValidateEdit = 711;

  /**
   * WidthChange event type (value is 712).
   */
  public static final int WidthChange = 712;

  /**
   * BeforeAdopt event type (value is 720).
   */
  public static final int BeforeAdopt = 720;

  /**
   * Adopt event type (value is 730).
   */
  public static final int Adopt = 730;

  /**
   * BeforeOrphan event type (value is 740).
   */
  public static final int BeforeOrphan = 740;

  /**
   * BeforeQuery event type (value is 742).
   */
  public static final int BeforeQuery = 742;

  /**
   * Orphan event type (value is 750).
   */
  public static final int Orphan = 750;

}
