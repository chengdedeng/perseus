package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author:杨果
 * @date:16/7/4 下午3:25
 *
 * Description:
 *
 */
public interface SeparationMapper {
    @Select("select\n" +
            "        *\n" +
            "        from\n" +
            "        info.yangguo.perseus.test.dao.HintMapper\n" +
            "        where\n" +
            "        user_name = #{userName}\n" +
            "        and\n" +
            "        password = #{password}")
    @Results(
            {
                    @Result(id = true, column = "user_id", property = "userId"),
                    @Result(column = "user_name", property = "userName")
            })
    User select(User user);

    @Insert("insert into info.yangguo.perseus.test.dao.HintMapper(\n" +
            "        user_name,password,birth_date,sex,age,type,email,\n" +
            "        inst,address,create_time,modify_time\n" +
            "        )\n" +
            "        VALUES (#{userName}, #{password}, '20141230133350', '1', '1', '1', 'yangguo@outlook.info', 'inst', '上海博物馆', '20141230133350', '20141230133350')")
    void insert(User user);
}
