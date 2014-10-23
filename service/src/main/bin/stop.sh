pid=`ps ax | grep 'com.alibaba.dubbo.container.Main'| grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
       echo "No user-service running."
       exit -1;
fi
echo "pid(${pid})"
kill ${pid}

echo "shut down ok"