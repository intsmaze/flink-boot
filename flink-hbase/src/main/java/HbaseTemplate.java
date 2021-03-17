//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.apache.hadoop.util.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * Central class for accessing the HBase API. Simplifies the use of HBase and helps to avoid common errors.
// * It executes core HBase workflow, leaving application code to invoke actions and extract results.
// *
// * @author Costin Leau
// * @author Shaun Elliott
// */
///**
// * JThink@JThink
// *
// * @author JThink
// * @version 0.0.1
// * desc： copy from spring data hadoop hbase, modified by JThink, use the 1.0.0 api
// * date： 2016-11-15 15:42:46
// */
//public class HbaseTemplate implements HbaseOperations {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseTemplate.class);
//
//    private Configuration configuration;
//
//    private volatile Connection connection;
//
//    public HbaseTemplate(Configuration configuration) {
//        this.setConfiguration(configuration);
//    }
//
//    @Override
//    public <T> T execute(String tableName, TableCallback<T> action) throws Exception {
//
//        StopWatch sw = new StopWatch();
//        sw.start();
//        Table table = null;
//        try {
//            table = this.getConnection().getTable(TableName.valueOf(tableName));
//            return action.doInTable(table);
//        } catch (Throwable throwable) {
//            throw new Exception();
//        } finally {
//            if (null != table) {
//                try {
//                    table.close();
//                    sw.stop();
//                } catch (IOException e) {
//                    LOGGER.error("hbase资源释放失败");
//                }
//            }
//        }
//    }
//
//    @Override
//    public <T> List<T> find(String tableName, String family, final RowMapper<T> action) {
//        Scan scan = new Scan();
//        scan.setCaching(5000);
//        scan.addFamily(Bytes.toBytes(family));
//        return this.find(tableName, scan, action);
//    }
//
//    @Override
//    public <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> action) {
//        Scan scan = new Scan();
//        scan.setCaching(5000);
//        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
//        return this.find(tableName, scan, action);
//    }
//
//    @Override
//    public <T> List<T> find(String tableName, Scan scan, RowMapper<T> mapper) {
//        return null;
//    }
//
//    @Override
//    public <T> T get(String tableName, String rowName, final RowMapper<T> mapper) {
//        return this.get(tableName, rowName, null, null, mapper);
//    }
//
//    @Override
//    public <T> T get(String tableName, String rowName, String familyName, final RowMapper<T> mapper) {
//        return this.get(tableName, rowName, familyName, null, mapper);
//    }
//
//    @Override
//    public <T> T get(String tableName, String rowName, String familyName, String qualifier, RowMapper<T> mapper) {
//        return null;
//    }
//
//    @Override
//    public void execute(String tableName, MutatorCallback action) {
//
//    }
//
//    @Override
//    public void saveOrUpdate(String tableName, final Mutation mutation) {
//        this.execute(tableName, new MutatorCallback() {
//            @Override
//            public void doInMutator(BufferedMutator mutator) throws Throwable {
//                mutator.mutate(mutation);
//            }
//        });
//    }
//
//    @Override
//    public void saveOrUpdates(String tableName, final List<Mutation> mutations) {
//        this.execute(tableName, new MutatorCallback() {
//            @Override
//            public void doInMutator(BufferedMutator mutator) throws Throwable {
//                mutator.mutate(mutations);
//            }
//        });
//    }
//
//    public void setConnection(Connection connection) {
//        this.connection = connection;
//    }
//
//    public Connection getConnection() {
//        if (null == this.connection) {
//            synchronized (this) {
//                if (null == this.connection) {
//                    try {
//                        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(200, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//                        // init pool
//                        poolExecutor.prestartCoreThread();
//                        this.connection = ConnectionFactory.createConnection(configuration, poolExecutor);
//                    } catch (IOException e) {
//                        LOGGER.error("hbase connection资源池创建失败");
//                    }
//                }
//            }
//        }
//        return this.connection;
//    }
//
//    public Configuration getConfiguration() {
//        return configuration;
//    }
//
//    public void setConfiguration(Configuration configuration) {
//        this.configuration = configuration;
//    }
//}
