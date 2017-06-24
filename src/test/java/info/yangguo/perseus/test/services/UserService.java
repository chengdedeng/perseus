package info.yangguo.perseus.test.services;

import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by li.rui on 2016/4/15.
 */
public interface UserService {
    User selectOne_statement();

    User selectOne_statement_parameter(User user);

    List<User> selectList_statement();

    List<User> selectList_statement_parameter(User user);

    List<User> selectList_statement_parameter_rowBounds(User user, RowBounds rowBounds);

    Map<Integer, User> selectMap_statement_mapKey();

    Map<Integer, User> selectMap_statement_parameter_mapKey(User user);

    Map<Integer, User> selectMap_statement_parameter_mapKey_rowBounds(User user, RowBounds rowBounds);

    User select(User user);

    void insert(User user);

    User transaction(User user);
}
