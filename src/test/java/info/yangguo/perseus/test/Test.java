package info.yangguo.perseus.test;

import info.yangguo.perseus.test.dao.UserMapper1;
import info.yangguo.perseus.test.dao.UserMapper2;
import info.yangguo.perseus.test.domain.User;
import info.yangguo.perseus.test.service.UserService;
import info.yangguo.perseus.test.util.SqliteUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @author:杨果
 * @date:16/7/4 下午1:42
 *
 * Description:
 *
 */
public class Test {
    UserMapper1 userMapper1;
    UserMapper2 userMapper2;
    UserService userService;

    @Before
    public void before() {
        String[] dbs = new String[]{"master", "slave1", "slave2"};
        for (String db : dbs) {
            String url = SqliteUtil.createNewDatabase(db + ".db");
            String sql1 = "DROP TABLE IF EXISTS \"user\";";
            SqliteUtil.executeSql(url, sql1);
            String sql2 = "CREATE TABLE \"user\" (\"user_name\" text NOT NULL,\"type\" text,PRIMARY KEY(\"user_name\"));";
            SqliteUtil.executeSql(url, sql2);
            if (db.startsWith("slave")) {
                String sql3 = "INSERT INTO \"user\" VALUES ('yangguo','" + db + "');";
                SqliteUtil.executeSql(url, sql3);
            }
        }

        String[] xmls = new String[]{"classpath:applicationContext.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(xmls);
        userMapper1 = (UserMapper1) context.getBean("userMapper1");
        userMapper2 = (UserMapper2) context.getBean("userMapper2");
        userService = (UserService) context.getBean("userServiceImpl");
    }

    @org.junit.Test
    public void transaction() {
        userService.testTransaction();
        User user = new User();
        user.setUserName("yangguo");
        Map<String, User> map1 = userMapper2.selectMaster(user);
        Assert.assertEquals(0, map1.size());
        Map<String, User> map2 = userMapper2.selectSlave(user);
        Assert.assertEquals(1, map2.size());
    }

    @org.junit.Test
    public void selectSlave() {
        User user = new User();
        user.setUserName("yangguo");
        Map<String, User> map = userMapper2.selectSlave(user);
        Assert.assertEquals(true, map.get("yangguo").getType().startsWith("slave"));
    }

    @org.junit.Test
    public void combination() {
        User user = new User();
        user.setUserName("yangguo");
        user.setType("master1");
        userMapper2.insert(user);
        Map<String, User> map1 = userMapper2.selectMaster(user);
        Assert.assertEquals("master1", map1.get("yangguo").getType());
        user.setType("master2");
        userMapper1.update(user);
        Map<String, User> map2 = userMapper2.selectMaster(user);
        Assert.assertEquals("master2", map2.get("yangguo").getType());
        userMapper1.delete("yangguo");
        Map<String, User> map3 = userMapper2.selectMaster(user);
        Assert.assertEquals(0, map3.size());
    }

    @org.junit.Test
    public void readonlyTransaction() {
        userService.testReadonlyTransaction();
    }

    @After
    public void after() {
        String[] dbs = new String[]{"master.db", "slave1.db", "slave2.db"};
        for (String db : dbs) {
            SqliteUtil.dropDatabase(db);
        }
    }
}
