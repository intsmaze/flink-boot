//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//
///**
// * @author ：intsmaze
// * @date ：Created in 2021/3/11 17:38
// * @description： https://www.cnblogs.com/intsmaze/
// * @modified By：
// */
//public class Client {
//
//    private static final String HBASE_QUORUM = "hbase.zookeeper.quorum";
//    private static final String HBASE_ROOTDIR = "hbase.rootdir";
//    private static final String HBASE_ZNODE_PARENT = "zookeeper.znode.parent";
//
//    public static void main(String[] args) {
//        Configuration configuration = HBaseConfiguration.create();
//        configuration.set(HBASE_QUORUM, "");
//        configuration.set(HBASE_ROOTDIR, "");
//        configuration.set(HBASE_ZNODE_PARENT, "");
//        HbaseTemplate hbaseTemplate = new HbaseTemplate(configuration);
//
//        hbaseTemplate.find("","",null);
//    }
//
//
//}
