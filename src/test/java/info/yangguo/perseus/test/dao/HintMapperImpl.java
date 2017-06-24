package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.DynamicSqlSessionTemplate;
import info.yangguo.perseus.test.domain.User;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author:杨果
 * @date:16/7/1 上午9:09
 *
 * Description:
 *
 */
@Component
public class HintMapperImpl implements HintMapper {
    @Autowired
    private DynamicSqlSessionTemplate sqlSessionTemplate;

    @Override
    public User selectOne_statement() {
        return sqlSessionTemplate.selectOne("info.yangguo.perseus.test.dao.HintMapper.selectOne_statement");
    }

    @Override
    public User selectOne_statement_parameter(User user) {
        return sqlSessionTemplate.selectOne("info.yangguo.perseus.test.dao.HintMapper.selectOne_statement_parameter", user);
    }

    @Override
    public List<User> selectList_statement() {
        return sqlSessionTemplate.selectList("info.yangguo.perseus.test.dao.HintMapper.selectList_statement");
    }

    @Override
    public List<User> selectList_statement_parameter(User user) {
        return sqlSessionTemplate.selectList("info.yangguo.perseus.test.dao.HintMapper.selectList_statement_parameter", user);
    }

    @Override
    public List<User> selectList_statement_parameter_rowBounds(User user, RowBounds rowBounds) {
        return sqlSessionTemplate.selectList("info.yangguo.perseus.test.dao.HintMapper.selectList_statement_parameter_rowBounds", user, rowBounds);
    }

    @Override
    public Map<Integer, User> selectMap_statement_mapKey() {
        return sqlSessionTemplate.selectMap("info.yangguo.perseus.test.dao.HintMapper.selectMap_statement_mapKey", "userId");
    }

    @Override
    public Map<Integer, User> selectMap_statement_parameter_mapKey(User user) {
        return sqlSessionTemplate.selectMap("info.yangguo.perseus.test.dao.HintMapper.selectMap_statement_parameter_mapKey", user, "userId");
    }

    @Override
    public Map<Integer, User> selectMap_statement_parameter_mapKey_rowBounds(User user, RowBounds rowBounds) {
        return sqlSessionTemplate.selectMap("info.yangguo.perseus.test.dao.HintMapper.selectMap_statement_parameter_mapKey_rowBounds", user, "userId");
    }
}
