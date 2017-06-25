package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.annotations.Param;


public interface UserMapper1 {
    void delete(@Param(value = "userName") String userName);

    int update(User user);
}
