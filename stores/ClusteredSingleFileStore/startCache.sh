JDG_LIB="/srv/jdg/6.3.1/jboss-datagrid-6.3.1-library"
BMHOME=/data/app/byteman-2.1.4.1

CLASSPATH="target/ClusteredFileStoreApp.jar"

if [ -n "$JDG_LIB" ];then
  for jar in $JDG_LIB/*.jar
  do
    CLASSPATH="$CLASSPATH:$jar"
  done
  for jar in $JDG_LIB/lib/*.jar
  do
    CLASSPATH="$CLASSPATH:$jar"
  done
fi

$BMHOME/bin/bmcheck.sh -cp $CLASSPATH /home/wfink/examples/byteman/Infinispan-SingleFileStore.btm

JAVA_OPTS="$JAVA_OPTS -javaagent:$BMHOME/lib/byteman.jar=script:/home/wfink/examples/byteman/Infinispan-SingleFileStore.btm"
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=y"
java $JAVA_OPTS -Djava.net.preferIPv4Stack=true -cp $CLASSPATH org.infinispan.wfink.clustered.filestore.ClusteredFileStore $@
