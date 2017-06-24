package info.yangguo.perseus.test;

import info.yangguo.perseus.test.domain.User;
import info.yangguo.perseus.test.services.UserService;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;


public class Hint1Test {

    UserService userService;

    @Before
    public void before() {
        String[] xmls = new String[]{"classpath:applicationContext-db1.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(xmls);
        userService = (UserService) context.getBean("userServiceImpl");
    }


    @Test
    public void selectOne_statement() {
        User tmp = userService.selectOne_statement();
        System.out.println(tmp);
    }

    @Test
    public void selectOne_statement_parameter() {
        User user = new User();
        user.setUserName("ttt");
        user.setPassword("1");
        User tmp = userService.selectOne_statement_parameter(user);
        System.out.println(tmp);
    }

    @Test
    public void selectList_statement() {
        List<User> list = userService.selectList_statement();
        System.out.println(list);
    }

    @Test
    public void selectList_statement_parameter() {
        User user = new User();
        user.setUserName("ttt");
        user.setPassword("1");
        List<User> list = userService.selectList_statement_parameter(user);
        System.out.println(list);
    }

    @Test
    public void selectList_statement_parameter_rowBounds() {
        User user = new User();
        user.setUserName("ttt");
        RowBounds rowBounds = new RowBounds(0, 2);
        List<User> list = userService.selectList_statement_parameter_rowBounds(user, rowBounds);
        System.out.println(list);
    }

    @Test
    public void selectMap_statement_mapKey() {
        Map map = userService.selectMap_statement_mapKey();
        System.out.println(map);
    }

    @Test
    public void selectMap_statement_parameter_mapKey() {
        User user = new User();
        user.setUserId(19);
        Map map = userService.selectMap_statement_parameter_mapKey(user);
        System.out.println(map);
    }

    @Test
    public void selectMap_statement_parameter_mapKey_rowBounds() {
        User user = new User();
        user.setUserId(19);
        RowBounds rowBounds = new RowBounds(0, 2);
        Map map = userService.selectMap_statement_parameter_mapKey_rowBounds(user, rowBounds);
        System.out.println(map);
    }
}
