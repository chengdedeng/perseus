package info.yangguo.perseus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * Read and write separation logic for transactions.
 */
public class DynamicDataSourceTransactionManager extends
        DataSourceTransactionManager {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceTransactionManager.class);
    private static final long serialVersionUID = 7160082287881717832L;

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        logger.info("~~~~~~~~~~~~~~~~~~~Transaction begin~~~~~~~~~~~~~~~~~~~");
        DataSourceHolder.setMaster();
        logger.info("Master database is selected");
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DataSourceHolder.clearDataSource();
        logger.info("~~~~~~~~~~~~~~~~~~~Transaction end~~~~~~~~~~~~~~~~~~~");
    }
}
