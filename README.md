[![Join the chat at https://gitter.im/chengdedeng/perseus](https://badges.gitter.im/chengdedeng/perseus.svg)](https://gitter.im/chengdedeng/perseus?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
## [中文文档](/README-ZH.md)

### Project description
The read/write separation of databases is a basic requirement, it is usually comes in three ways:
1. multi-data source,hard-code.
2. Extended ORM.
3. Implement protocol of database by middleware.


First is simplest,but developer need to do a lot of work and easy to make mistakes;although the third program is transparent
for developer and don't restrict the programming language, but it's most difficult of development and the scope of the database support is fewer.
This project is based on the program II, select  [Mybatis](http://www.mybatis.org/mybatis-3/) and [Spring](https://spring.io/) of most popular framework in java, so it is only applicable to the Mybatis + Spring implementation of the Java project.

### Function
1. The transaction is routed to the master database and does't distinguish whether transaction is readonly. Due to readonly don't really start the transaction, 
just activate the transaction synchronization, so don't be [DynamicDataSourceTransactionManager](/src/main/java/info/yangguo/perseus/DynamicDataSourceTransactionManager.java) to intercept, 
so setting to be hit by the default(master database), and the execution of all queries in a read only transaction will reuse the same JDBCConnection(SqlSession).
2. The select query is routed to the slave database, insert/update/delete SQL routing to the main master database.
3. Support select query to force routing to master database (as far as possible, bypassing business logic optimization).
4. Support batch operations in mybatis-spring.


### Stability
The project is widely used in the author's company, hundreds of projects, and has been well developed, with detailed configuration and test code in the test code.


### Core configuration

#### General configuration

```
    <bean id="dataSource" class="info.yangguo.perseus.DynamicDataSource">
        <property name="master" ref="master"/>
        <property name="slaves">
            <list>
                <ref bean="slave1"/>
                <ref bean="slave2"/>
            </list>
        </property>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="info.yangguo.perseus.test.domain"/>
        <property name="configLocation" value="classpath:sqlMapConfig.xml"/>
        <property name="mapperLocations" value="classpath:info/yangguo/perseus/test/dao/*.xml"/>
    </bean>

    <bean class="info.yangguo.perseus.MapperScannerConfigurer">
        <property name="basePackage" value="info.yangguo.perseus.test.dao"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <bean id="transactionManager"
          class="info.yangguo.perseus.DynamicDataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>
```


#### Batch operation configuration

```
    <bean id="dataSource" class="info.yangguo.perseus.DynamicDataSource">
        <property name="master" ref="master"/>
        <property name="slaves">
            <list>
                <ref bean="slave1"/>
                <ref bean="slave2"/>
            </list>
        </property>
    </bean>

    <!-- transaction manager, use JtaTransactionManager for global tx -->
    <bean id="transactionManager"
          class="info.yangguo.perseus.DynamicDataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- enable transaction demarcation with annotations -->
    <tx:annotation-driven/>

    <!-- simplest possible SqlSessionFactory configuration -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath:EmployeeMapper.xml"/>
    </bean>
    <!-- item reader  -->
    <bean id="pagingNoNestedItemReader" class="info.yangguo.perseus.DynamicMyBatisPagingItemReader">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="sqlSessionTemplate" ref="dynamicSqlSession"/>
        <property name="queryId" value="getEmployeeNoNestedPaging"/>
        <property name="pageSize" value="5"/>
    </bean>

    <bean id="pagingNestedItemReader" class="info.yangguo.perseus.DynamicMyBatisPagingItemReader">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="sqlSessionTemplate" ref="dynamicSqlSession"/>
        <property name="queryId" value="getEmployeeNestedPaging"/>
        <property name="pageSize" value="5"/>
    </bean>

    <bean id="cursorNoNestedItemReader" class="info.yangguo.perseus.DynamicMyBatisCursorItemReader">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="queryId" value="getEmployeeNoNestedCursor"/>
    </bean>

    <bean id="cursorNestedItemReader" class="info.yangguo.perseus.DynamicMyBatisCursorItemReader">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="queryId" value="getEmployeeNestedCursor"/>
    </bean>

    <bean id="writer" class="org.mybatis.spring.batch.MyBatisBatchItemWriter">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="statementId" value="updateEmployee"/>
    </bean>

    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg index="0" ref="sqlSessionFactory"/>
        <constructor-arg index="1" value="BATCH"/>
    </bean>
    <bean id="dynamicSqlSession" class="info.yangguo.perseus.DynamicSqlSessionTemplate">
        <constructor-arg index="0" ref="sqlSession"/>
    </bean>
```
