
CLASSPATH="target/SingleFileStoreReader.jar"

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

#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=y"
java $JAVA_OPTS -cp $CLASSPATH org.infinispan.wfink.filestore.SingleFileStoreReader $@

