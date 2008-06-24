/*
 * This theme was created by J.C. Bize. 
 * For more infomation see:
 * http://extjs.com/forum/showthread.php?t=15989
 */
package com.extjs.gxt.themes.client;

import com.extjs.gxt.ui.client.util.Theme;

/**
 * Slate theme by J.C. Bize.
 * 
 * <p/>http://extjs.com/forum/showthread.php?t=15989
 */
public class Slate extends Theme {

  public static Theme SLATE = new Slate();

  public Slate() {
    super("slate", ThemeNames.NAMES.slate(), "xtheme-slate.css");
  }

  public Slate(String name) {
    super("slate", name, "xtheme-slate.css");
  }

}
