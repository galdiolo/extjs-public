/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.rebind.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.extjs.gxt.ui.client.core.BaseTemplateFactory;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.core.TemplateFactory;
import com.extjs.gxt.ui.client.core.TemplateFactory.Cache;
import com.extjs.gxt.ui.client.core.TemplateFactory.Resource;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This is the thread-safe Generator for
 * {@link com.extjs.gxt.ui.client.core.TemplateFactory TemplateFactory}
 * subinterfaces.
 */
public class TemplateFactoryGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    return new TemplateFactorySourceWriter(logger, context).generate(typeName);
  }

  /**
   * This class is responsible for creating the implementing class of a
   * {@link com.extjs.gxt.ui.client.core.TemplateFactory TemplateFactory}
   * subinterface.
   */
  public static class TemplateFactorySourceWriter {
    private TreeLogger logger;
    private GeneratorContext context;
    private JClassType type;
    private ClassSourceFileComposerFactory composer;
    private SourceWriter sw;
    private Class<? extends TemplateFactory> templateFactoryInterface;
    private JType templateType;
    private JType stringType;

    public TemplateFactorySourceWriter(TreeLogger logger, GeneratorContext context) {
      this.logger = logger;
      this.context = context;
    }

    public String generate(String typeName) throws UnableToCompleteException {
      TypeOracle oracle = context.getTypeOracle();

      try {
        type = oracle.getType(typeName);
        templateType = oracle.getType("com.extjs.gxt.ui.client.core.Template");
        stringType = oracle.getType("java.lang.String");
        templateFactoryInterface = (Class<? extends TemplateFactory>) Class.forName(typeName);
      } catch (NotFoundException e) {
        logger.log(TreeLogger.ERROR, "Class " + typeName + " not found.", e);
        throw new UnableToCompleteException();
      } catch (ClassNotFoundException e) {
        logger.log(TreeLogger.ERROR, "Class " + typeName + " not found.", e);
        throw new UnableToCompleteException();
      }

      composer = new ClassSourceFileComposerFactory(type.getPackage().getName(),
          type.getName() + "Impl");
      composer.addImplementedInterface(type.getQualifiedSourceName());
      composer.addImport(Template.class.getName());
      composer.setSuperclass(BaseTemplateFactory.class.getName());

      PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
          type.getName() + "Impl");

      // check for duplicate method names
      Set<String> methodNames = new HashSet<String>();
      for (JMethod method : type.getMethods()) {
        if (methodNames.contains(method.getName())) {
          logger.log(TreeLogger.ERROR, "Class " + typeName
              + " contains multiple methods of the same name '" + method.getName() + "'");
          throw new UnableToCompleteException();
        }
        methodNames.add(method.getName());
      }

      if (pw != null) {
        sw = composer.createSourceWriter(context, pw);

        for (JMethod method : type.getMethods()) {
          createMethod(method);
        }

        sw.commit(logger);
      }

      return typeName + "Impl";
    }

    private void createMethod(JMethod method) throws UnableToCompleteException {
      if (!(method.getReturnType() == templateType || method.getReturnType() == stringType)) {
        logger.log(TreeLogger.ERROR, "Method " + method.getName() + " does not return "
            + templateType + " or " + stringType);
        throw new UnableToCompleteException();
      }

      String resource;
      Resource resourceAnn = method.getAnnotation(Resource.class);
      if (resourceAnn != null && !resourceAnn.value().equals("")) {
        resource = resourceAnn.value();
      } else {
        resource = type.getName() + "#" + method.getName() + ".html";
      }

      boolean cache = false;
      Cache cacheAnn = method.getAnnotation(Cache.class);
      cache = (cacheAnn != null);

      String content = readResource(method, resource);

      // generate the source expression for the cache key
      String cacheKeyExpression = null;
      if (cache) {
        cacheKeyExpression = "\"" + method.getName() + "\"";
        for (JParameter parameter : method.getParameters()) {
          cacheKeyExpression += "," + parameter.getName();
        }
      }

      // Begin method
      sw.indent();
      sw.print(method.getReadableDeclaration(false, false, false, false, true));
      sw.println(" {");
      sw.indent();

      if (method.getReturnType() == stringType) {
        if (cache) {
          sw.print("String cached = (String)cache.get(" + cacheKeyExpression + ");");
          sw.println("if (cached != null) return cached;");
        }

        sw.println("StringBuilder sb = new StringBuilder();");
        // put the template code in a nested block to limit the scope of its
        // variables
        sw.println("{");
        new TemplateToJavaSourceConverter(logger, sw, content).convert();
        sw.println("}");
        sw.println("String result = sb.toString();");

        if (cache) {
          sw.print("cache.put(result, " + cacheKeyExpression + ");");
        }

        sw.println("return result;");
      } else {

        // if Cache enabled, generate source to lookup the cached value
        if (cache) {
          sw.print("Template cached = (Template)cache.get(" + cacheKeyExpression + ");");
          sw.println("if (cached != null) return cached;");
        }

        sw.println("StringBuilder sb = new StringBuilder();");
        // put the template code in a nested block to limit the scope of its
        // variables
        sw.println("{");
        new TemplateToJavaSourceConverter(logger, sw, content).convert();
        sw.println("}");
        sw.println("Template template = new Template(sb.toString());");

        // if cache enabled, compile the template and store it in the cache
        if (cache) {
          sw.print("template.compile();");
          sw.print("cache.put(template, " + cacheKeyExpression + ");");
        }

        sw.println("return template;");
      }

      sw.outdent();
      sw.println("}");
      sw.outdent();
    }

    private String readResource(JMethod method, String source)
        throws UnableToCompleteException {
      InputStream is = templateFactoryInterface.getResourceAsStream(source);
      if (is == null) {
        logger.log(TreeLogger.ERROR, "Unable to find source template file " + source
            + " for method " + method.getName() + ".");
        throw new UnableToCompleteException();
      }
      StringBuilder sb = new StringBuilder();

      BufferedReader br = new BufferedReader(new InputStreamReader(is,
          Charset.forName("UTF-8")));

      try {
        String buffer = br.readLine();
        while (buffer != null) {
          sb.append(buffer);
          buffer = br.readLine();
          if (buffer != null) {
            sb.append('\n');
          }
        }
      } catch (IOException e) {
        throw new UnableToCompleteException();
      }
      return sb.toString();
    }

  }

  /**
   * <p/>this class encapsulates all of the code to transform a single template
   * into java source
   * 
   * <p/>When the {@link TemplateToJavaSourceConverter#convert()} method is
   * called the generated source already contains a variable "StringBuilder sb"
   * initialized to a new instance
   * 
   * <p/>The {@link TemplateToJavaSourceConverter#convert()} method should
   * generate source of the form
   * 
   * <pre>
   *  sb.append("some text");
   *  if (true) {
   *    sb.append("some more text");
   *  }
   *  </pre>
   */
  public static class TemplateToJavaSourceConverter {

    private final TreeLogger logger;
    private final SourceWriter sw;
    private final String content;

    private static Pattern codeStartPattern = Pattern.compile(
        "(.*?)((?:<%)|(?:<#)|(?:\\$\\{))", Pattern.DOTALL);
    private static Pattern fmListPattern = Pattern.compile("(.+?)\\s+as\\s+(?:(.+?):)?([^:]+)");
    private static Pattern iterableRangePattern = Pattern.compile("\\[?(.+)\\.\\.([^\\]]+)");
    private static Pattern iterableTablePattern = Pattern.compile(
        "\\[([^(?:\\.\\.)]*)\\]", Pattern.DOTALL);

    private char codeType;
    private String codeEnd;
    private String iterableName;
    private String variableName;
    private String variableType;

    private TemplateToJavaSourceConverter(TreeLogger logger, SourceWriter sw,
        String content) {
      this.logger = logger;
      this.sw = sw;
      this.content = content;
    }

    private void convert() throws UnableToCompleteException {
      int currentPosition = 0;
      int contentLength = content.length();

      while (currentPosition < contentLength) {
        // Find next code start
        int nextCodeStartPosition = findNextCodeStart(content, currentPosition);

        // If there is text then add it
        if (nextCodeStartPosition > currentPosition || nextCodeStartPosition == -1) {
          sw.print("sb.append(\"");
          if (nextCodeStartPosition == -1) {
            sw.print(Generator.escape(content.substring(currentPosition)));
          } else {
            sw.print(Generator.escape(content.substring(currentPosition,
                nextCodeStartPosition)));
          }
          sw.println("\");");
        }

        if (nextCodeStartPosition == -1) {
          break;
        }

        currentPosition = nextCodeStartPosition;

        int codeEndPosition = findCodeEndPosition(content, currentPosition);
        if (codeEndPosition == -1) {
          logger.log(TreeLogger.ERROR, "Code end not found.");
          throw new UnableToCompleteException();
        }

        generateCode(content.substring(currentPosition + 2, codeEndPosition));

        currentPosition = codeEndPosition + codeEnd.length();
      }
    }

    private void generateCode(String code) throws UnableToCompleteException {
      switch (codeType) {
        case '%':
        case '{':
          generateJspCode(code);
          break;
        case '#':
          generateFMCode(code);
          break;
      }
    }

    private void generateFMCode(String code) throws UnableToCompleteException {
      int firstWordEnd = code.indexOf(' ');
      if (firstWordEnd == -1) {
        firstWordEnd = code.length();
      }

      String firstWord = code.substring(0, firstWordEnd);
      String parameters = code.substring(firstWordEnd).trim();

      if ("if".equals(firstWord)) {
        fmIf(parameters);
      } else if ("end".equals(firstWord)) {
        fmEnd(parameters);
      } else if ("else".equals(firstWord)) {
        fmElse(parameters);
      } else if ("elseif".equals(firstWord)) {
        fmElseif(parameters);
      } else if ("list".equals(firstWord)) {
        fmList(parameters);
      } else {
        logger.log(TreeLogger.ERROR, "Unknown FM code " + firstWord + ".");
      }
    }

    private void fmElseif(String parameters) {
      sw.outdent();
      sw.print("} else if (");
      sw.print(parameters);
      sw.println(") {");
      sw.indent();
    }

    private void fmElse(String parameters) {
      sw.println("} else {");
    }

    private void fmList(String parameters) throws UnableToCompleteException {
      Matcher matcher = fmListPattern.matcher(parameters);

      if (!matcher.matches()) {
        logger.log(TreeLogger.ERROR, "Incorrect parameters for list function.");
        throw new UnableToCompleteException();
      }

      iterableName = matcher.group(1);
      variableType = matcher.group(2);
      variableName = matcher.group(3);

      analyseIterable();

      sw.print("for(");
      sw.print(variableType == null ? "Object" : variableType);
      sw.print(" ");
      sw.print(variableName);
      sw.print(" : ");
      sw.print(iterableName);
      sw.println(") {");
      sw.indent();
    }

    private void analyseIterable() {
      // Is it a range?
      Matcher matcher = iterableRangePattern.matcher(iterableName);
      if (matcher.matches()) {
        iterableName = "(new IterableRange<Integer>(" + matcher.group(1) + ","
            + matcher.group(2) + "))";
        variableType = "int";
        return;
      }

      // Is it a table?
      matcher = iterableTablePattern.matcher(iterableName);
      if (matcher.matches()) {
        String type = variableType == null ? "Object" : variableType;
        iterableName = "new " + type + "[] {" + matcher.group(1) + "}";
        return;
      }
    }

    private void fmEnd(String parameters) {
      sw.println("}");
      sw.outdent();
    }

    private void fmIf(String parameters) {
      sw.print("if (");
      sw.print(parameters);
      sw.println(") {");
      sw.indent();
    }

    private void generateJspCode(String code) {
      if (codeType == '{' || code.charAt(0) == '=') {
        sw.print("sb.append(");
        if (codeType == '{') {
          sw.print(code);
        } else {
          sw.print(code.substring(1));
        }
        sw.println(");");
      } else {
        sw.println(code);
      }
    }

    private int findCodeEndPosition(String content, int currentPosition) {
      switch (codeType) {
        case '%':
          codeEnd = "%>";
          break;
        case '#':
          codeEnd = "#>";
          break;
        case '{':
          codeEnd = "}";
          break;
      }
      return content.indexOf(codeEnd, currentPosition);
    }

    private int findNextCodeStart(String content, int currentPosition) {
      Matcher matcher = codeStartPattern.matcher(content);
      if (!matcher.find(currentPosition)) {
        return -1;
      } else {
        codeType = matcher.group(2).charAt(1);
        return matcher.start(2);
      }
    }
  }
}