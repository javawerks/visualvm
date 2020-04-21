# run:
# ant build nbms build-zip
# expand the dist/visualvm.zip -> dist/visualvm

# export MAVEN_CENTRAL_REPO_URL=https://repo1.maven.org/maven2/
# export MAVEN_NEXUS_LOCAL_DIR=/tmp/visualvm-nexus
export MAVEN_REPO=~/.m2/repository
export MAVEN_REPO_URL=file://$MAVEN_REPO
export MAVEN_VERSION=RELEASE200
export PROJECT_ROOT=/Users/jeanmarc/code/oss/GitHub/visualvm

# /Users/thurka/Projects/Source/more-crippled-netbeans/main/nbbuild/netbeans/java/maven/bin/mvn \
#  -DrepositoryUrl=$MAVEN_CENTRAL_REPO_URL \
#  -DdeployUrl=$MAVEN_REPO_URL \
#  -DforcedVersion=$MAVEN_VERSION \
#  -DnetbeansInstallDirectory=/Users/thurka/Projects/Source/visualvm.src/visualvm/dist/visualvm \
#  -DnetbeansNbmDirectory=/Users/thurka/Projects/Source/netbeans-releases/nbbuild/nbms \
#  -DnexusIndexDirectory=$MAVEN_NEXUS_LOCAL_DIR \
#  -DskipInstall=true \
#  -DgroupIdPrefix=org.graalvm.visualvm \
#  org.codehaus.mojo:nb-repository-plugin:1.2:download org.codehaus.mojo:nb-repository-plugin:1.2:populate

# mvn -X -DdeployUrl=file:///tmp/maven/repo \
# -DdeployId=thirdparty-releases   \
# -DforcedVersion=$MAVEN_VERSION   \
# -DnetbeansInstallDirectory=$PROJECT_ROOT/visualvm/dist/visualvm   \
# -DnetbeansNbmDirectory=$PROJECT_ROOT/visualvm/build/updates   \
# -DskipInstall=true  \
# -DgroupIdPrefix=com.sun.tools.visualvm  \
# org.apache.netbeans.utilities:nb-repository-plugin:populate

# for local installation
mvn -X \
-DnetbeansInstallDirectory=$PROJECT_ROOT/visualvm/dist/visualvm   \
-DnetbeansNbmDirectory=$PROJECT_ROOT/visualvm/build/updates   \
-DgroupIdPrefix=com.sun.tools.visualvm  \
-DforcedVersion=$MAVEN_VERSION   \
org.apache.netbeans.utilities:nb-repository-plugin:populate
