@ECHO OFF

set GWT_HOME=c:/gwt-windows-1.4.60

@java -cp "%~dp0\../resources/src;%~dp0\src;%~dp0\bin;%~dp0;%GWT_HOME%/gwt-user.jar;%GWT_HOME%/gwt-dev-windows.jar;%~dp0\../../gxt.jar" com.google.gwt.dev.GWTShell -port 8080 -out "%~dp0\www" %* com.extjs.gxt.samples.mail.Mail/index.html
