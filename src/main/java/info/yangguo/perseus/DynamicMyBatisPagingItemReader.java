package info.yangguo.perseus;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ClassUtils.getShortName;

/**
 * @author:杨果
 * @date:2017/7/16 下午3:38
 *
 *@see org.mybatis.spring.batch.MyBatisPagingItemReader
 */
public class DynamicMyBatisPagingItemReader<T> extends AbstractPagingItemReader<T> {

    private String queryId;

    private SqlSessionFactory sqlSessionFactory;

    private DynamicSqlSessionTemplate sqlSessionTemplate;

    private Map<String, Object> parameterValues;

    public DynamicMyBatisPagingItemReader() {
        setName(getShortName(DynamicMyBatisPagingItemReader.class));
    }

    /**
     * Public setter for {@link SqlSessionFactory} for injection purposes.
     *
     * @param sqlSessionFactory a factory object for the {@link SqlSession}.
     */
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionTemplate(DynamicSqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * Public setter for the statement id identifying the statement in the SqlMap configuration
     * file.
     *
     * @param queryId the id for the statement
     */
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    /**
     * The parameter values to be used for the query execution.
     *
     * @param parameterValues the values keyed by the parameter named used in the query string.
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * Check mandatory properties.
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        notNull(sqlSessionFactory);
        sqlSessionTemplate = new DynamicSqlSessionTemplate(new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH));
        notNull(queryId);
    }

    @Override
    protected void doReadPage() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (parameterValues != null) {
            parameters.putAll(parameterValues);
        }
        parameters.put("_page", getPage());
        parameters.put("_pagesize", getPageSize());
        parameters.put("_skiprows", getPage() * getPageSize());
        if (results == null) {
            results = new CopyOnWriteArrayList<T>();
        } else {
            results.clear();
        }
        results.addAll(sqlSessionTemplate.<T>selectList(queryId, parameters));
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
        // Not Implemented
    }

}

