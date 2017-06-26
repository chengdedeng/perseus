package info.yangguo.perseus.test.service;

import info.yangguo.perseus.test.dao.UserMapper1;
import info.yangguo.perseus.test.dao.UserMapper2;
import info.yangguo.perseus.test.domain.User;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author:杨果
 * @date:2017/6/25 上午11:06
 *
 * Description:
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper1 userMapper1;
    @Autowired
    private UserMapper2 userMapper2;

    @Override
    @Transactional
    public void testTransaction() {
        User u1 = new User();
        u1.setUserName("yangguo");
        u1.setType("master1");
        userMapper2.insert(u1);
        //即使查从库也要路由到主库
        Map<String, User> map1 = userMapper2.selectSlave(u1);
        Assert.assertEquals("master1", map1.get("yangguo").getType());
        u1.setType("master2");
        userMapper1.update(u1);
        u1.setType("master1");
        Map<String, User> map2 = userMapper2.selectSlave(u1);
        Assert.assertEquals("master2", map2.get("yangguo").getType());
        userMapper1.delete("yangguo");
        Map<String, User> map3 = userMapper2.selectSlave(u1);
        Assert.assertEquals(0, map3.size());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void testReadonlyTransaction() {
        User user = new User();
        user.setUserName("yangguo");
        user.setType("master");
        userMapper2.insert(user);
        Map<String, User> map1 = userMapper2.selectSlave(user);
        Assert.assertEquals("master", map1.get("yangguo").getType());
        userMapper1.delete("yangguo");
        Map<String, User> map2 = userMapper2.selectSlave(user);
        Assert.assertEquals(0, map2.size());
    }
}
