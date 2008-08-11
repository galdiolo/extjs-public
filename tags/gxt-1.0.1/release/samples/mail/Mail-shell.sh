#! /bin/sh

GWT_HOME=/home/edu/java/gwt-linux-1.4.60

java -cp "../resources/src:src:.:../bin:$GWT_HOME/gwt-user.jar:$GWT_HOME/gwt-dev-linux.jar:../../gxt.jar" com.google.gwt.dev.GWTShell -port 8080 -out "www" com.extjs.gxt.samples.mail.Mail/index.html
