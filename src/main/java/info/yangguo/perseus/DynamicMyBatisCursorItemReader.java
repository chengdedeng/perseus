package info.yangguo.perseus;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ClassUtils.getShortName;

/**
 * @author:杨果
 * @date:2017/7/16 下午3:56
 *
 *@see org.mybatis.spring.batch.MyBatisCursorItemReader
 */
public class DynamicMyBatisCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(DynamicMyBatisCursorItemReader.class);
    private static final ThreadLocal<Boolean> isSet = new ThreadLocal<>();

    private String queryId;

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    private Map<String, Object> parameterValues;

    private Cursor<T> cursor;
    private Iterator<T> cursorIterator;

    public DynamicMyBatisCursorItemReader() {
        setName(getShortName(DynamicMyBatisCursorItemReader.class));
    }

    @Override
    protected T doRead() throws Exception {
        T next = null;
        if (cursorIterator.hasNext()) {
            next = cursorIterator.next();
        }
        return next;
    }

    @Override
    public void doOpen() throws Exception {
        if (!DataSourceHolder.haveValue()) {
            DataSourceHolder.setSlave();
            isSet.set(true);
            logger.debug("Cursor's reading choose slave");
        }
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (parameterValues != null) {
            parameters.putAll(parameterValues);
        }

        sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
        cursor = sqlSession.selectCursor(queryId, parameters);
        cursorIterator = cursor.iterator();
    }

    @Override
    public void doClose() throws Exception {
        cursor.close();
        sqlSession.close();
        cursorIterator = null;
        if (isSet.get() != null) {
            DataSourceHolder.clearDataSource();
            logger.debug("Cursor read's threadlocal have been clean up");
        }
    }

    /**
     * Check mandatory properties.
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(sqlSessionFactory);
        notNull(queryId);
    }

    /**
     * Public setter for {@link SqlSessionFactory} for injection purposes.
     *
     * @param sqlSessionFactory a factory object for the {@link SqlSession}.
     */
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
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
}

