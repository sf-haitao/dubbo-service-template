package com.sfebiz.demo.service.sls;

import com.aliyun.openservices.sls.*;

import java.util.Date;
import java.util.Vector;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/12/14 下午5:27
 */
public class SlsDemo {

    public static void main(String[] args) {

        String accessId = "0tNtH7eVTLLZFcpU";
        String accessKey = "xsLm7BtZlbEbCC0j8LCz3LcGIKpAll";

        String project = "sfht";
        String host = "http://cn-hangzhou.sls.aliyuncs.com";
        // or create a client with a setting endpoint address
        SLSClient client = new SLSClient(accessId, accessKey, host);
        // generate two logs

        int size = 5;
        for (int i = 0; i < 0; i++) {
            Vector<LogItem> logGroup = new Vector<LogItem>();
            LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
            logItem.PushBack("level", "info");
            logItem.PushBack("name", String.valueOf(i));
            logItem.PushBack("message", "it's a test message");

            logGroup.add(logItem);

            LogItem logItem2 = new LogItem((int) (new Date().getTime() / 1000));
            logItem2.PushBack("level", "error");
            logItem2.PushBack("name", String.valueOf(i));
            logItem2.PushBack("message", "it's a test message");
            logGroup.add(logItem2);

            try {
                client.PutData(project, "order-trace", "sls_topic", logGroup);
                size++;
            } catch (SlsException e) {
                System.out.println("error code :" + e.GetErrorCode());
                System.out.println("error message :" + e.GetErrorMessage());
                return;
            }

        }
        System.out.println("success lines: " + String.valueOf(size));

        // get the category from sls server
        try {
            Vector<String> categorys = client.ListCategory(project);
            System.out.println("categorys: " + categorys.toString());
        } catch (SlsException e) {
            System.out.println("error code :" + e.GetErrorCode());
            System.out.println("error message :" + e.GetErrorMessage());
            System.out.println("error message :" + e.toString());
        }


        // get the topic from sls server
        try {
            Vector<String> topics = client.ListTopic(project, "order-trace", "");
            System.out.println(topics.toString());
        } catch (SlsException e) {
            System.out.println("error code :" + e.GetErrorCode());
            System.out.println("error message :" + e.GetErrorMessage());
        }

        try {
            LogMeta logMeta = client.GetDataMeta(project, "order-trace",
                    "sls_topic", (int) (new Date().getTime() / 1000 - 10000),
                    (int) (new Date().getTime() / 1000 + 10));
            System.out.println("result : " + logMeta.mTotalogNum);
            System.out.println("more_data : " + logMeta.mMoreData);
            for (LogMetaItem metaItem : logMeta.mlogMetaItem) {
                System.out.println("beginTime:" + metaItem.mBeginTime
                        + " endTime:" + metaItem.mEndTime + " logNum:"
                        + metaItem.mLogNum + " moreData:" + metaItem.mMoreData);
            }
        } catch (SlsException e) {
            System.out.println("error code :" + e.GetErrorCode());
            System.out.println("error message :" + e.GetErrorMessage());
        }

        // get the data from sls server, it should return the logs contain
        // "error" key words
        try {
            LogResult result1 = client.GetData(project, "order-trace",
                    "sls_topic", (int) (new Date().getTime() / 1000 - 10000),
                    (int) (new Date().getTime() / 1000 + 10));
            for (LogItem item : result1.logDatas) {
                System.out.println("time : " + item.logTime);
                for (LogContent content : item.contents) {
                    System.out.println(content.key + ":" + content.value);
                }
            }
        } catch (SlsException e) {
            System.out.println("error code :" + e.GetErrorCode());
            System.out.println("error message :" + e.GetErrorMessage());
        }
    }
}
