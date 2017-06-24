package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


public interface HintMapper {
    User selectOne_statement();

    User selectOne_statement_parameter(User user);

    List<User> selectList_statement();

    List<User> selectList_statement_parameter(User user);

    List<User> selectList_statement_parameter_rowBounds(User user, RowBounds rowBounds);

    @MapKey("userId")
    Map<Integer, User> selectMap_statement_mapKey();

    @MapKey("userId")
    Map<Integer, User> selectMap_statement_parameter_mapKey(User user);

    @MapKey("userId")
    Map<Integer, User> selectMap_statement_parameter_mapKey_rowBounds(User user, RowBounds rowBounds);
}
