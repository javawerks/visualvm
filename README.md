# VisualVM (master) sources repository

VisualVM is a visual tool integrating commandline JDK tools and lightweight profiling capabilities. See https://visualvm.github.io for details, downloads and documentation.

## Get the tools

Use Apache Ant 1.9.9 or above and Oracle JDK 8 to build VisualVM from this repository.

## Get the sources

First download or clone this repository into directory `visualvm`. There are two project suites included:
  * visualvm (`visualvm/visualvm`) - suite for the core VisualVM tool
  * plugins (`visualvm/plugins`) - suite for the VisualVM plugins available in Plugins Center

## Configure the dependencies

Then extract the [NetBeans Platform 11.1](visualvm/nb111_platform_08102019.zip) into directory `visualvm/visualvm` (should create `visualvm/visualvm/netbeans`).

## Build and run VisualVM tool

To build VisualVM, use `ant build-zip` command in the `visualvm/visualvm` directory. To run VisualVM, use `ant run` command in the `visualvm/visualvm` directory.

## Build and run VisualVM plugins

To build or run the plugins suite, use `ant build` or `ant run` in the `visualvm/plugins` directory. This will automatically build the zip distribution of the core VisualVM tool into `visualvm/visualvm/dist/visualvm.zip` and extract it into the `visualvm/plugins/visualvm` directory. After that the build of the plugins suite continues to build each of the individual plugins. Running the plugins suite means starting VisualVM with all the plugins installed.

## Generate the Maven artifacts

You need to prepare the binaries:

  1. Build visualvm as described above
  2. Generate the NBMs by running: `ant nbms`. This will generate a folder `build/updates` containing all the NBMs.
  3. Expand/decompress the generated visualvm.zip file in `visualvm/dist`.

It is time to generate the artifacts by using `org.apache.netbeans.utilities:nb-repository-plugin`. Make sure your are still located in `visualvm/visualvm`.

If you want to install the artifacts into your local repository use the following command:

```
mvn \
-DnetbeansInstallDirectory=dist/visualvm   \
-DnetbeansNbmDirectory=build/updates   \
-DgroupIdPrefix=com.sun.tools.visualvm  \
-DforcedVersion=RELEASE200   \
org.apache.netbeans.utilities:nb-repository-plugin:populate
```

If you want to publish into a remote repository, Use

```
mvn
-DnetbeansInstallDirectory=dist/visualvm   \
-DnetbeansNbmDirectory=build/updates   \
-DgroupIdPrefix=com.sun.tools.visualvm  \
-DforcedVersion=RELEASE200   \
-DdeployUrl=<URL to the remote repo> \
-DdeployId=<repository id referenced in your settings.xml>   \
-DskipInstall=true  \
org.apache.netbeans.utilities:nb-repository-plugin:populate
```
For more details about `nb-repository-plugin` see https://bits.netbeans.org/mavenutilities/nb-repository-plugin/index.html

## Contribute

We highly appreciate any feedback! Please let us know your ideas, missing features, or bugs found. Either [file a RFE/bug](https://github.com/oracle/visualvm/issues/new/choose) or [leave us a message](https://visualvm.github.io/feedback.html). For legal reasons, we cannot accept external pull requests.
