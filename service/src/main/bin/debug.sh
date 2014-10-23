JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=7880"
java $JAVA_OPTS -Djava.ext.dirs=./lib com.alibaba.dubbo.container.Main