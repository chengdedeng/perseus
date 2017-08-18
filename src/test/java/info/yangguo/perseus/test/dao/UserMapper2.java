package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.decorators.LruCache;

import java.util.Map;

/**
 * @author:杨果
 * @date:16/7/4 下午3:25
 *
 * Description:
 *
 */
@CacheNamespace
public interface UserMapper2 {
    @Select("/*master*/select\n" +
            "        *\n" +
            "        from\n" +
            "        user\n" +
            "        where\n" +
            "        user_name = #{userName}")
    @Results(
            {
                    @Result(id = true, column = "user_name", property = "userName")
            })
    @MapKey("userName")
    Map<String, User> selectMaster(User user);

    @Select("select\n" +
            "        *\n" +
            "        from\n" +
            "        user\n" +
            "        where\n" +
            "        user_name = #{userName}")
    @Results(
            {
                    @Result(id = true, column = "user_name", property = "userName")
            })
    @MapKey("userName")
    Map<String, User> selectSlave(User user);

    @Insert("insert into user(\n" +
            "        user_name,type)\n" +
            "        VALUES (#{userName},#{type})")
    void insert(User user);
}
