Since ExtJS does not provide a public Subversion repository, this project will endeavour to keep an up-to-date version of the latest release available.
This will allow other projects integrating ExtJS to create an external include to this project, thus allowing updates to be automatic.

## News ##
  * **2013-08-22** - Updated to latest releases
    * [Ext JS 4.2.1](http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/)
    * [Ext JS 3.4.1.1](http://extjs-public.googlecode.com/svn/tags/extjs-3.4.1.1/)
    * [GXT 2.3.0](http://extjs-public.googlecode.com/svn/tags/gxt-2.3.0/)
  * **2012-12-06** - Updated to latest releases
    * [Ext JS 4.1.1a](http://extjs-public.googlecode.com/svn/tags/extjs-4.1.1a/)
    * [GXT 3.0.1](http://extjs-public.googlecode.com/svn/tags/gxt-3.0.1/)
      * The new GXT major version has a [separate root](http://extjs-public.googlecode.com/svn/gxt-3.x/).
  * **2012-01-12** - Long-needed updates in the repository:
    * [Ext JS 3.4.0](http://extjs-public.googlecode.com/svn/tags/extjs-3.4.0/)
    * [Ext JS 4.0.7](http://extjs-public.googlecode.com/svn/tags/extjs-4.0.7/)
      * The new 4.x branch has been placed in [its own root folder](http://extjs-public.googlecode.com/svn/extjs-4.x/).
    * [GXT 2.2.5](http://extjs-public.googlecode.com/svn/tags/gxt-2.2.5/)
      * The GXT binaries for different releases of GWT have been unified into one folder, as per the downloadable archive.
  * **2010-12-01** - Updated to [Ext JS 3.3.1](http://extjs-public.googlecode.com/svn/tags/extjs-3.3.1/)
  * **2010-11-11** - Updated to [GXT 2.2.1](http://extjs-public.googlecode.com/svn/tags/gxt-2.2.1/)
  * **2010-10-13** -
    * Updated to [Ext JS 3.3.0](http://extjs-public.googlecode.com/svn/tags/extjs-3.3.0/)
    * Updated to [GXT 2.2.0](http://extjs-public.googlecode.com/svn/tags/gxt-2.2.0/)
  * **2010-05-18** - Updated to [Ext JS 3.2.1](http://extjs-public.googlecode.com/svn/tags/extjs-3.2.1/)
  * **2010-04-08** - Updated to [Ext JS 3.2](http://extjs-public.googlecode.com/svn/tags/extjs-3.2/)
  * **2010-02-09** -
    * Updated to [Ext JS 3.1.1](http://extjs-public.googlecode.com/svn/tags/extjs-3.1.1/)
    * Updated to [GXT 2.1.1](http://extjs-public.googlecode.com/svn/tags/gxt-2.1.1/)
      * **Note:** GXT now has a separate release for GWT 2.0. This can be found in the release-gwt2 directory.
  * **2009-12-17** - Updated to [Ext JS 3.1.0](http://extjs-public.googlecode.com/svn/tags/extjs-3.1.0/)

## License ##
  * [ExtJS 2.0.2 and earlier](http://extjs-public.googlecode.com/svn/tags/ext-2.0.2/release/LICENSE.txt) are licensed under the terms of the [LGPL 3.0](http://www.gnu.org/licenses/lgpl.html) with some exceptions.
  * [ExtJS 2.1 and later](http://extjs-public.googlecode.com/svn/tags/ext-2.1/release/LICENSE.txt) are licensed under the terms of the [GPL 3.0](http://www.gnu.org/licenses/gpl.html)
  * [Gxt](http://extjs-public.googlecode.com/svn/tags/gxt-1.0.2/release/license.txt) is licenced under the terms of the [GPL 3.0](http://www.gnu.org/licenses/gpl.html)
  * [Ext-Core](http://code.google.com/p/extjs-public/source/browse/tags/ext-core-3.0-beta/release/LICENSE.txt) is licenced under the terms of the MIT Licence
## Quick Start ##

### Linking to the Sencha CDN ###
For the latest version of the source code, you are probably best-off linking to the CDN provided by Sencha:
```
<script type="text/javascript" charset="utf-8" src="http://cdn.sencha.io/ext-4.2.0-gpl/ext-all.js"></script>
```

### Linking to ExtJS on the Google Code servers ###
The files needed to use ExtJS are located in the subversion repository. You can link directly to the files from your website like this:
```
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/ext-2.2/release/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/ext-2.2/release/ext-all.js"></script>
```
**Please note this may not have ideal performance. The files will not be served with gzip compression.**
See [this article](http://ajaxian.com/archives/announcing-ajax-libraries-api-speed-up-your-ajax-apps-with-googles-infrastructure) for more information about Google's [AJAX library hosting](http://code.google.com/apis/ajaxlibs/) project - they only provide Sencha Core at present.

### Including ExtJS in your source tree ###
To include ExtJS in your project from Subversion, you can do one of several things.

#### If you use Subversion for your own project ####
If your project **also uses Subversion** for revision control, you can use an svn:externals property to include ExtJS. For example, if you have a project with a directory structure which looks like:
```
/project
  /js
    /extjs
```
you can create an svn:externals property on the js folder with the following content:
```
extjs http://extjs-public.googlecode.com/svn/extjs-4.x/include
```
Now, every time you run `svn update` on your working copy, the ExtJS folder will also be updated.

#### If you use another source code management tool ####
If you **don't use Subversion**, but do have the Subversion client installed, you can check out the ExtJS code using the following command:
```
svn checkout http://extjs-public.googlecode.com/svn/extjs-4.x/include extjs
```
You will have to manually update the working copy when ExtJS is updated.