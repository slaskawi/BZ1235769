REPO="/data/Maven/repository"
CLASSPATH="target/ClusteredFileStoreApp.jar"
CLASSPATH="$CLASSPATH:$REPO/org/infinispan/infinispan-core/6.0.3.Final-redhat-3/infinispan-core-6.0.3.Final-redhat-3.jar"
CLASSPATH="$CLASSPATH:$REPO/org/infinispan/infinispan-commons/6.0.3.Final-redhat-3/infinispan-commons-6.0.3.Final-redhat-3.jar"
CLASSPATH="$CLASSPATH:$REPO/org/jboss/logging/jboss-logging/3.1.4.GA-redhat-1/jboss-logging-3.1.4.GA-redhat-1.jar"
CLASSPATH="$CLASSPATH:$REPO/org/jboss/marshalling/jboss-marshalling/1.4.8.Final-redhat-1/jboss-marshalling-1.4.8.Final-redhat-1.jar"
CLASSPATH="$CLASSPATH:$REPO/org/jboss/marshalling/jboss-marshalling-river/1.4.8.Final-redhat-1/jboss-marshalling-river-1.4.8.Final-redhat-1.jar"
CLASSPATH="$CLASSPATH:$REPO/org/jgroups/jgroups/3.4.3.Final-redhat-1/jgroups-3.4.3.Final-redhat-1.jar"
CLASSPATH="$CLASSPATH:$REPO/javax/transaction/transaction-api/1.1/transaction-api-1.1.jar"

java -Djava.net.preferIPv4Stack=true -cp $CLASSPATH org.infinispan.wfink.clustered.filestore.ClusteredFileStore $@
