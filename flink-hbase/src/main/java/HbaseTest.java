import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/3/12 14:36
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class HbaseTest {
    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "intsmaze-204:2181");
        Connection connection = ConnectionFactory.createConnection(configuration);

        TableName tablename = TableName.valueOf("testhbase");
        Table table = connection.getTable(tablename);

        String rowKey = "0002";
        String columnFaily = "f1";
        String columnName = "age";

        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFaily), Bytes.toBytes(columnName), Bytes.toBytes("28"));
        table.put(put);

        Get get = new Get("0002".getBytes());
        Result result = table.get(get);

        List<Cell> cells = result.listCells();
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            String familyName = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            String qualifierName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String valueName = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            System.out.println(familyName + ":" + qualifierName + ":" + valueName);
        }
        table.close();
        connection.close();
    }
}
