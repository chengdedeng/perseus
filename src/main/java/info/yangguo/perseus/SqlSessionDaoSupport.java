package info.yangguo.perseus;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

import static org.springframework.util.Assert.notNull;

/**
 * Convenient super class for MyBatis SqlSession data access objects. It gives you access to the
 * template which can then be used to execute SQL methods.
 * <p>
 * This class needs a SqlSessionTemplate or a SqlSessionFactory. If both are set the
 * SqlSessionFactory will be ignored.
 * <p>
 * {code Autowired} was removed from setSqlSessionTemplate and setSqlSessionFactory in version
 * 1.2.0.
 *
 * @author Putthibong Boonbong
 * @version $Id$
 * @see #setSqlSessionFactory
 * @see #setSqlSessionTemplate
 * @see SqlSessionTemplate
 */
public abstract class SqlSessionDaoSupport extends DaoSupport {

    private SqlSession sqlSession;
    private boolean externalSqlSession;

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (!this.externalSqlSession) {
            //使用DynamicSqlSessionTemplate 实现读写分离
            this.sqlSession = new DynamicSqlSessionTemplate(new SqlSessionTemplate(sqlSessionFactory));
        }
    }

    /**
     * 使用DynamicSqlSessionTemplate 实现读写分离
     *
     * @param sqlSessionTemplate DynamicSqlSessionTemplate
     */
    public void setSqlSessionTemplate(DynamicSqlSessionTemplate sqlSessionTemplate) {
        this.sqlSession = sqlSessionTemplate;
        this.externalSqlSession = true;
    }

    /**
     * Users should use this method to get a SqlSession to call its statement methods This is
     * SqlSession is managed by spring. Users should not commit/rollback/close it because it will be
     * automatically done.
     *
     * @return Spring managed thread safe SqlSession
     */
    public SqlSession getSqlSession() {
        return this.sqlSession;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
    }

}