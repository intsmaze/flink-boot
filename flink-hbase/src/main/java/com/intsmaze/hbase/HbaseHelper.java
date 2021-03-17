package com.intsmaze.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/3/17 19:36
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class HbaseHelper {

    private Connection hbaseConnection;

    public HbaseHelper(Connection hbaseConnection) {
        this.hbaseConnection = hbaseConnection;
    }

    public void putHbase(String tableName, String rowKey, String columnFaily, String[] columnNames, String[] values) throws IOException {
        TableName tablename = TableName.valueOf(tableName);
        Table table = hbaseConnection.getTable(tablename);
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            String value = values[i];
            put.addColumn(Bytes.toBytes(columnFaily), Bytes.toBytes(columnName), Bytes.toBytes(value));
        }
        table.put(put);
        table.close();
    }


    public String getHbase(String tableName, String rowKey) throws IOException {
        TableName tablename = TableName.valueOf(tableName);
        Table table = hbaseConnection.getTable(tablename);
        Get get = new Get(rowKey.getBytes());
        Result result = table.get(get);
        List<Cell> cells = result.listCells();
        String str = "";
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            String familyName = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            String qualifierName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String valueName = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            str = familyName + ":" + qualifierName + ":" + valueName;
            break;//仅做示例，一般来说就一个列族
        }
        table.close();
        return str;
    }

    public Connection getHbaseConnection() {
        return hbaseConnection;
    }

    public void setHbaseConnection(Connection hbaseConnection) {
        this.hbaseConnection = hbaseConnection;
    }
}
