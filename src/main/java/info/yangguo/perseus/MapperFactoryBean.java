package info.yangguo.perseus;


import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.FactoryBean;

import static org.springframework.util.Assert.notNull;

/**
 * BeanFactory that enables injection of MyBatis mapper interfaces. It can be set up with a
 * SqlSessionFactory or a pre-configured SqlSessionTemplate.
 * <p>
 * Sample configuration:
 * <p>
 * <pre class="code"> {@code <bean id="baseMapper" class="org.mybatis.spring.mapper.MapperFactoryBean"
 * abstract="true" lazy-init="true"> <property name="sqlSessionFactory" ref="sqlSessionFactory" />
 * </bean>
 * <p>
 * <bean id="oneMapper" parent="baseMapper"> <property name="mapperInterface"
 * value="my.package.MyMapperInterface" /> </bean>
 * <p>
 * <bean id="anotherMapper" parent="baseMapper"> <property name="mapperInterface"
 * value="my.package.MyAnotherMapperInterface" /> </bean> } </pre>
 * <p>
 * Note that this factory can only inject <em>interfaces</em>, not concrete classes.
 *
 * @author Eduardo Macarron
 * @version $Id$
 * @see info.yangguo.perseus.DynamicSqlSessionTemplate
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    private Class<T> mapperInterface;

    private boolean addToConfig = true;


    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public MapperFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();

        notNull(this.mapperInterface, "Property 'mapperInterface' is required");

        Configuration configuration = getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    //------------- mutators --------------

    /**
     * Sets the mapper interface of the MyBatis mapper
     *
     * @param mapperInterface class of the interface
     */
    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * Return the mapper interface of the MyBatis mapper
     *
     * @return class of the interface
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * If addToConfig is false the mapper will not be added to MyBatis. This means it must have been
     * included in mybatis-config.xml.
     * <p/>
     * If it is true, the mapper will be added to MyBatis in the case it is not already registered.
     * <p/>
     * By default addToCofig is true.
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * Return the flag for addition into MyBatis config.
     *
     * @return true if the mapper will be added to MyBatis in the case it is not already registered.
     */
    public boolean isAddToConfig() {
        return addToConfig;
    }
}