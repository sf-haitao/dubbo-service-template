package com.sfebiz.demo.service.ots;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.OTSErrorCode;
import com.aliyun.openservices.ots.OTSException;
import com.aliyun.openservices.ots.model.*;

import java.util.Date;
import java.util.List;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/12/14 下午11:09
 */
public class OtsDemo {

    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_DATETIME = "trace_time";
    private static final String COLUMN_CONTENT_NAME = "content";

    public static void main(String args[]) {
        final String endPoint = "http://Orders.cn-hangzhou.ots.aliyuncs.com";
        final String accessId = "0tNtH7eVTLLZFcpU";
        final String accessKey = "xsLm7BtZlbEbCC0j8LCz3LcGIKpAll";
        final String instanceName = "Orders";

        OTSClient client = new OTSClient(endPoint, accessId, accessKey,
                instanceName);
        final String tableName = "trace_log";

        try {
            // 插入多行数据。
            putRows(client, tableName);
            // 再取回来看看。
            getRange(client, tableName);
        } catch (ServiceException e) {
            System.err.println("操作失败，详情：" + e.getMessage());
            // 可以根据错误代码做出处理， OTS的ErrorCode定义在OTSErrorCode中。
            if (OTSErrorCode.QUOTA_EXHAUSTED.equals(e.getErrorCode())) {
                System.err.println("超出存储配额。");
            }
            // Request ID可以用于有问题时联系客服诊断异常。
            System.err.println("Request ID:" + e.getRequestId());
        } catch (ClientException e) {
            // 可能是网络不好或者是返回结果有问题
            System.err.println("请求失败，详情：" + e.getMessage());
        } finally {
            // 不留垃圾。
        }
    }

    private static void putRows(OTSClient client, String tableName)
            throws OTSException, ClientException {
        String orderId = "1000000789-HAITAO";
        final int rowCount = 5;
        for (int i = 0; i < rowCount; ++i) {
            RowPutChange rowChange = new RowPutChange(tableName);
            RowPrimaryKey primaryKey = new RowPrimaryKey();
            primaryKey.addPrimaryKeyColumn(COLUMN_ORDER_ID,
                    PrimaryKeyValue.fromString(orderId));
            primaryKey.addPrimaryKeyColumn(COLUMN_DATETIME,
                    PrimaryKeyValue.fromString(String.valueOf(new Date().getTime())));
            rowChange.setPrimaryKey(primaryKey);
            rowChange.addAttributeColumn(COLUMN_CONTENT_NAME,
                    ColumnValue.fromString("小xxxxxx" + i));

            PutRowRequest request = new PutRowRequest();
            request.setRowChange(rowChange);

            PutRowResult result = client.putRow(request);
            int consumedWriteCU = result.getConsumedCapacity()
                    .getCapacityUnit().getWriteCapacityUnit();

            System.out.println("成功插入数据, 消耗的写CU为：" + consumedWriteCU);
        }

        System.out.println(String.format("成功插入%d行数据。", rowCount));
    }

    private static void getRange(OTSClient client, String tableName)
            throws OTSException, ClientException {

        String orderId = "1000000789-HAITAO";
        // 演示一下如何按主键范围查找，这里查找uid从1-4（左开右闭）的数据
        RangeRowQueryCriteria criteria = new RangeRowQueryCriteria(tableName);
        RowPrimaryKey inclusiveStartKey = new RowPrimaryKey();
        inclusiveStartKey.addPrimaryKeyColumn(COLUMN_ORDER_ID,
                PrimaryKeyValue.fromString(orderId));
        inclusiveStartKey.addPrimaryKeyColumn(COLUMN_DATETIME,
                PrimaryKeyValue.INF_MIN); // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小

        RowPrimaryKey exclusiveEndKey = new RowPrimaryKey();
        exclusiveEndKey.addPrimaryKeyColumn(COLUMN_ORDER_ID,
                PrimaryKeyValue.fromString(orderId));
        exclusiveEndKey.addPrimaryKeyColumn(COLUMN_DATETIME,
                PrimaryKeyValue.INF_MAX); // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小

        criteria.setInclusiveStartPrimaryKey(inclusiveStartKey);
        criteria.setExclusiveEndPrimaryKey(exclusiveEndKey);
        GetRangeRequest request = new GetRangeRequest();
        request.setRangeRowQueryCriteria(criteria);
        GetRangeResult result = client.getRange(request);
        List<Row> rows = result.getRows();
        for (Row row : rows) {
            System.out.println("content信息为：" + row.getColumns());
        }

        int consumedReadCU = result.getConsumedCapacity().getCapacityUnit()
                .getReadCapacityUnit();
        System.out.println("本次读操作消耗的读CapacityUnit为：" + consumedReadCU);
    }
}
