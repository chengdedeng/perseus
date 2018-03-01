[![Join the chat at https://gitter.im/chengdedeng/perseus](https://badges.gitter.im/chengdedeng/perseus.svg)](https://gitter.im/chengdedeng/perseus?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
## [English document](/README-ZH.md)

### 项目介绍
数据库读写分离是再基础不过的需求了,读写分离通常有三种方案实现:
1. 多数据源,通过代码硬编码实现.
2. 修改ORM框架实现.
3. 实现数据库协议来实现.

方案一最简单,但是开发人员工作量最大,并且容易犯错;虽然方案三开发人员来说是透明的且不限制编程语言,但是开发难度最大且数据库的支持范围
较窄.本项目基于方案二,选择了Java中最流行的Mybatis和Spring来实现,所以只适用于基于Mybatis+Spring实现的Java项目.


### 功能
1. 事务一律到主库,不区分transaction是否是readonly.由于readonly并不真正的启动事务,只是激活transaction synchronization,因此并不会被[DynamicDataSourceTransactionManager](/src/main/java/info/yangguo/perseus/DynamicDataSourceTransactionManager.java)截获,
所以就被default(master database)设置命中,并且一个read only transaction中的所有语句的执行都会复用同一个JDBCConnection(SqlSession).
2. select到读库,insert/update/delete到主库.
3. 支持select强制路由到主库(尽量避免,通过业务逻辑优化来绕过).
4. 支持mybatis-spring中的batch操作.


### 稳定度
该项目在笔者公司,上百个项目中广泛应用,已经很成熟,测试代码中有详细的配置和测试代码.


### 核心配置

#### 常规配置

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

    <!-- ibatis3 工厂类 -->
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

    <!-- 定义单个jdbc数据源的事务管理器 -->
    <bean id="transactionManager"
          class="info.yangguo.perseus.DynamicDataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!-- 以 @Transactional 标注来定义事务  -->
    <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>
```


#### batch操作配置

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
