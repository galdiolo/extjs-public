/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client;

import java.util.Map;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.messages.MyMessages;
import com.extjs.gxt.ui.client.state.CookieProvider;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.util.CSS;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;

/**
 * GXT core utilities and functions.
 */
public class GXT {

  /**
   * GXT messages.
   */
  public static MyMessages MESSAGES = (MyMessages) GWT.create(MyMessages.class);

  /**
   * <code>true</code> if the browser is safari.
   */
  public static boolean isSafari;
  
  /**
   * <code>true</code> if the browser is safari2.
   */
  public static boolean isSafari2;
  
  /**
   * <code>true</code> if the browser is safari3.
   */
  public static boolean isSafari3;

  /**
   * <code>true</code> if the browser is chrome.
   */
  public static boolean isChrome;
  
  /**
   * <code>true</code> if the browser is opera.
   */
  public static boolean isOpera;

  /**
   * <code>true</code> if the browser is ie.
   */
  public static boolean isIE;

  /**
   * <code>true</code> if the browser is ie6.
   */
  public static boolean isIE6;

  /**
   * <code>true</code> if the browser is ie7.
   */
  public static boolean isIE7;
  
  /**
   * <code>true</code> if the browser is ie8.
   */
  public static boolean isIE8;

  /**
   * <code>true</code> if the browser is gecko.
   */
  public static boolean isGecko;

  /**
   * <code>true</code> if the browser is gecko2.
   */
  public static boolean isGecko2;
  
  /**
   * <code>true</code> if the browser is gecko3.
   */
  public static boolean isGecko3;

  /**
   * <code>true</code> if the browser is in strict mode.
   */
  public static boolean isStrict;

  /**
   * <code>true</code> if using https.
   */
  public static boolean isSecure;

  /**
   * <code>true</code> if mac os.
   */
  public static boolean isMac;

  /**
   * <code>true</code> if linux os.
   */
  public static boolean isLinux;
  
  /**
   * <code>true</code> if windows os.
   */
  public static boolean isWindows;
  
  /**
   * <code>true</code> if is air.
   */
  public static boolean isAir;
  
  /**
   * <code>true</code> if is borderbox.
   */
  public static boolean isBorderBox;

  /**
   * URL to a blank file used by GXT when in secure mode for iframe src to
   * prevent the IE insecure content. Default value is 'blank.html'.
   */
  public static String SSL_SECURE_URL = GWT.getModuleBaseURL() + "blank.html";

  /**
   * URL to a 1x1 transparent gif image used by GXT to create inline icons with
   * CSS background images. Default value is 'images/default/shared/clear.gif';
   */
  public static String BLANK_IMAGE_URL = GWT.getModuleBaseURL()
      + "images/default/shared/clear.gif";

  private static boolean initialized;
  private static Theme defaultTheme;
  private static boolean forceTheme;
  private static Version version;

  /**
   * Returns the current theme id.
   * 
   * @return the theme id
   */
  public static String getThemeId() {
    Map map = StateManager.get().getMap(GWT.getModuleBaseURL() + "theme");
    if (map != null) {
      return map.get("id").toString();
    }
    return null;
  }

  /**
   * Returns the browser's user agent.
   * 
   * @return the user agent
   */
  public native static String getUserAgent() /*-{
     return $wnd.navigator.userAgent.toLowerCase();
   }-*/;

  /**
   * Returns the version information.
   * 
   * @return the version information
   */
  public static Version getVersion() {
    if (version == null) {
      version = new Version();
    }
    return version;
  }

  /**
   * Hides the loading panel.
   * 
   * @param id the loading panel id
   */
  public static void hideLoadingPanel(String id) {
    final Element loading = XDOM.getElementById(id);
    if (loading != null) {
      Timer t = new Timer() {
        @Override
        public void run() {
          loading.getStyle().setProperty("display", "none");
        }
      };
      t.schedule(500);
    }
  }

  /**
   * Initializes GXT. Statically called by
   * {@link com.extjs.gxt.ui.client.widget.Component} when instantiated
   */
  public static void init() {
    if (initialized) {
      return;
    }
    initialized = true;

    String ua = getUserAgent();   
    
    isOpera = ua.indexOf("opera") > -1;
    isChrome = ua.indexOf("chrome") > -1;
    isSafari = !isChrome && (ua.indexOf("webkit") > -1 || ua.indexOf("khtml") > -1);
    isSafari3 = isSafari && ua.indexOf("webkit/5") != -1;
    isSafari2 = isSafari && !isSafari3;
    isIE = !isOpera && ua.indexOf("msie") > -1;
    isIE7 = !isOpera && ua.indexOf("msie 7") > -1;
    isIE8 = !isOpera && ua.indexOf("msie 8") > -1;
    isIE6 = isIE && !isIE7 && !isIE8;
    isGecko = !isSafari && !isChrome && ua.indexOf("gecko") > -1;
    isGecko3 = isGecko && ua.indexOf("rv:1.9") > -1;
    isGecko2 = isGecko && !isGecko3;
    isBorderBox = isIE && !isStrict;
    isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1);
    isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1);
    isAir = (ua.indexOf("adobeair") != -1);
    isLinux = (ua.indexOf("linux") != -1);

    String mode = DOM.getElementProperty(XDOM.getDocument(), "compatMode");
    isStrict = mode != null ? mode.equals("CSS1Compat") : false;

    isSecure = isSecure();

    String cls = "";
    if (isIE) {
      cls = "ext-ie";
      cls += " "+ (isIE6 ? "ext-ie6" : (isIE7 ? "ext-ie7" : "ext-ie8"));
    } else if (isGecko) {
      cls = "ext-gecko";
      cls +=" "+ (isGecko2 ? "ext-gecko2" : "ext-gecko3");
    } else if (isOpera) {
      cls = "ext-opera";
    } else if (isSafari) {
      cls = "ext-safari";
    }

    if (isMac) {
      cls += " ext-mac";
    }

    if (isLinux) {
      cls += " ext-linux";
    }
   
    if(isBorderBox)
      cls +=" ext-border-box";

    CookieProvider provider = new CookieProvider("/", null, null, false);
    StateManager.get().setProvider(provider);

    Map theme = StateManager.get().getMap(GWT.getModuleBaseURL() + "theme");
    if ((defaultTheme != null && forceTheme) || (theme == null && defaultTheme != null)) {
      theme = defaultTheme.asMap();
    }
    if (theme != null) {
      String themeId = theme.get("id").toString();
      String fileName = theme.get("file").toString();
      if (!fileName.equalsIgnoreCase("ext-all.css")) {
        CSS.addStyleSheet(themeId, GWT.getModuleBaseURL() + "css/" + fileName);
      }
      cls += " x-theme-" + themeId;
      StateManager.get().set(GWT.getModuleBaseURL() + "theme", theme);
    }

    XDOM.getBody().setClassName(cls);

    initInternal();
    
    if(isStrict){ // add to the parent to allow for selectors like ".ext-strict .ext-ie"
      Element p = (Element) XDOM.getBody().getParentElement();
      if(p != null){
          El.fly(p).addStyleName("ext-strict");
      }
    }
  }

  /**
   * Sets the default theme which will be used if the user does not have a theme
   * selected with the state provider.
   * <p/>
   * Note : {@link com.extjs.gxt.ui.client.widget.Component} statically calls
   * {@link#init()} when instantiated, so ensure default theme is set before GXT
   * widgets are constructed.
   * 
   * @param theme the default theme
   * @param force true to force the theme, ignoring the the theme saved with the
   *          state manager
   */
  public static void setDefaultTheme(Theme theme, boolean force) {
    defaultTheme = theme;
    forceTheme = force;
  }

  /**
   * Changes the theme. A theme's stylehseets should be given a class = to the
   * theme id. Any stylesheets that have a class that do not match the id be
   * removed (stylesheets with no class specified are ignored). The method will
   * reload the application after changing themes.
   * 
   * @param theme the new theme name.
   */
  public static void switchTheme(Theme theme) {
    StateManager.get().set(GWT.getModuleBaseURL() + "theme", theme.asMap());
    XDOM.reload();
  }

  private static native void initInternal() /*-{
     $wnd.GXT = {};
     $wnd.GXT.Ext = {};
     @com.extjs.gxt.ui.client.core.Ext::load()();
   }-*/;

  private static native boolean isSecure() /*-{
     return $wnd.location.href.toLowerCase().indexOf("https") === 0;
   }-*/;

}
