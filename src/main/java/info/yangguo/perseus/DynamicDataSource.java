package info.yangguo.perseus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

/**
 * 根据线程上下文来选择合适的数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AtomicInteger counter = new AtomicInteger();
    private DataSource master;
    private List<DataSource> slaves;


    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource returnDataSource = null;
        if (DataSourceHolder.isMaster()) {
            returnDataSource = master;
        } else if (DataSourceHolder.isSlave()) {
            int count = counter.incrementAndGet();
            if (count > 1000000) {
                counter.set(0);
            }
            int n = slaves.size();
            int index = count % n;
            returnDataSource = slaves.get(index);
            log.info("No.{} slave datasource have been chose", index);
        } else {
            //如果最外层transaction为readonly,且传播级别为Propagation.SUPPORTS,DataSourceHolder既不会被DynamicDataSourceTransactionManager也不会被DynamicSqlSessionTemplate
            //所设置,当然还有别的可能不会被拦截的情况,我们都设置默认的数据库为master.
            returnDataSource = master;
            log.info("Master datasource have been chose by default");
        }
        if (returnDataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            org.apache.tomcat.jdbc.pool.DataSource source = (org.apache.tomcat.jdbc.pool.DataSource) returnDataSource;
            String jdbcUrl = source.getUrl();
            log.info("JdbcUrl:{}", jdbcUrl);
        }
        return returnDataSource;
    }

    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public List<DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
    }
}
