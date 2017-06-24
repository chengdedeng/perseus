package info.yangguo.perseus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 事务的读写分离逻辑
 * <p/>
 * 注意事项:如果readonly中不小心有了insert,update操作,非常危险,所以我们需要将读库设置为read only.
 */
public class DynamicDataSourceTransactionManager extends
        DataSourceTransactionManager {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceTransactionManager.class);
    private static final long serialVersionUID = 7160082287881717832L;

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        logger.info("~~~~~~~~~~~~~~~~~~~Transaction begin~~~~~~~~~~~~~~~~~~~");
        boolean readOnly = definition.isReadOnly();
        if (readOnly) {
            DataSourceHolder.setSlave();
            logger.info("Slaver database is selected");
        } else {
            DataSourceHolder.setMaster();
            logger.info("Master database is selected");
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DataSourceHolder.clearDataSource();
        logger.info("~~~~~~~~~~~~~~~~~~~Transaction end~~~~~~~~~~~~~~~~~~~");
    }
}
