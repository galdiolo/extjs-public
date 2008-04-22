/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.rebind.htmlsource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class HtmlSourceGenerator extends Generator {

  private static final String JAVA_LANG_OBJECT = "java.lang.Object";
  private static final String HTMLSOURCE_QNAME = "com.extjs.gxt.ui.client.widget.HtmlSource";

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {

    TypeOracle typeOracle = context.getTypeOracle();

    // Get metadata describing the user's class.
    JClassType userType = getValidUserType(logger, typeName, typeOracle);

    // Write the new class.
    String resultName = generateImpl(logger, context, userType, typeOracle);

    // Return the complete name of the generated class.
    return resultName;
  }

  private JClassType getValidUserType(TreeLogger logger, String typeName,
      TypeOracle typeOracle) throws UnableToCompleteException {
    try {
      // Get the type that the user is introducing.
      JClassType userType = typeOracle.getType(typeName);

      // Get the type this generator is designed to support.
      JClassType magicType = typeOracle.findType(HTMLSOURCE_QNAME);

      // Ensure it's an interface.
      if (userType.isInterface() == null) {
        logger.log(TreeLogger.ERROR, userType.getQualifiedSourceName()
            + " must be an interface", null);
        throw new UnableToCompleteException();
      }

      // Ensure proper derivation.
      if (!userType.isAssignableTo(magicType)) {
        logger.log(TreeLogger.ERROR, userType.getQualifiedSourceName()
            + " must be assignable to " + magicType.getQualifiedSourceName(), null);
        throw new UnableToCompleteException();
      }

      return userType;

    } catch (NotFoundException e) {
      logger.log(TreeLogger.ERROR, "Unable to find required type(s)", e);
      throw new UnableToCompleteException();
    }
  }

  private String generateImpl(TreeLogger logger, GeneratorContext context,
      JClassType userType, TypeOracle typeOracle) throws UnableToCompleteException {

    // Compute the package and class names of the generated class.
    String pkgName = userType.getPackage().getName();
    String subName = computeSubclassName(userType);

    // Begin writing the generated source.
    ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(pkgName,
        subName);
    f.addImplementedInterface(userType.getQualifiedSourceName());
    f.setSuperclass(JAVA_LANG_OBJECT);

    String html = readHtml(logger, userType);
    html = compressHtml(html);

    PrintWriter pw = context.tryCreate(logger, pkgName, subName);
    if (pw != null) {
      SourceWriter sw = f.createSourceWriter(context, pw);

      sw.println("public String getHtml() {");
      sw.indent();
      sw.println("return \"" + escape(html) + "\";");
      sw.outdent();
      sw.println("}");

      // Finish.
      sw.commit(logger);
    }

    return f.getCreatedClassName();
  }

  protected String compressHtml(String html) {
    if (html == null) throw new NullPointerException("html");
    html = html.replaceAll("(?s)<!--.*?-->", "");
    html = html.replaceAll("(?s)\\t", "");
    html = html.replaceAll("(?s)\\n", "");
    html = html.replaceAll("(?s)(  )+", " ");
    html = html.replaceAll("(?s)> <", "><");
    return html;
  }

  private String computeSubclassName(JClassType userType) {
    String baseName = userType.getName().replace('.', '_');
    return baseName + "_";
  }

  private String readHtml(TreeLogger logger, JClassType userType)
      throws UnableToCompleteException {

    String fileName = userType.getPackage().getName().replace('.', '/') + "/"
        + userType.getName() + ".html";

    URL htmlUrl = getClass().getClassLoader().getResource(fileName);
    if (htmlUrl == null) {
      logger.log(
          TreeLogger.ERROR,
          "Template html "
              + fileName
              + " not found on classpath (is the name specified as Class.getResource() would expect?)",
          null);
      throw new UnableToCompleteException();
    }

    InputStream is = null;

    try {
      is = htmlUrl.openStream();
      StringWriter output = new StringWriter();
      InputStreamReader input = new InputStreamReader(is);
      char[] buffer = new char[1024 * 4];
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
      }
      String html = output.toString();
      return html;
    } catch (Exception e) {
      logger.log(TreeLogger.ERROR, "Unable to read Template html " + fileName, e);
      throw new UnableToCompleteException();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (Exception e) {
        }
      }
    }
  }

}
