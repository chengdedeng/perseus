package info.yangguo.perseus.test;

import info.yangguo.perseus.test.domain.User;
import info.yangguo.perseus.test.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;

/**
 * @author:杨果
 * @date:16/7/4 下午4:33
 *
 * Description:
 *
 */
public class SeparationTest {
    UserService userService;

    @Before
    public void before() {
        String[] xmls = new String[]{"classpath:db/applicationContext-db1.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(xmls);
        userService = (UserService) context.getBean("userServiceImpl");
    }

    @Test
    public void select() {
        User user = new User();
        user.setUserName("ttt");
        user.setPassword("1");
        User user1 = userService.select(user);
        System.out.println(user1);
    }

    @Test
    public void insert() {
        User user = new User();
        user.setUserName(UUID.randomUUID().toString());
        user.setPassword("1");
        userService.insert(user);
        System.out.println(user);
    }

    @Test
    public void transacation() {
        User user = new User();
        user.setUserName(UUID.randomUUID().toString());
        user.setPassword("1");
        User user1=userService.transaction(user);
        System.out.println(user1);
    }
}
