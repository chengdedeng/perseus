package info.yangguo.perseus.test.services.impl;

import info.yangguo.perseus.test.dao.HintMapper;
import info.yangguo.perseus.test.dao.SeparationMapper;
import info.yangguo.perseus.test.domain.User;
import info.yangguo.perseus.test.services.UserService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by li.rui on 2016/4/15.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private HintMapper hintMapper;
    @Autowired
    private SeparationMapper separationMapper;


    @Override
    public User selectOne_statement() {
        return hintMapper.selectOne_statement();
    }

    @Override
    public User selectOne_statement_parameter(User user) {
        return hintMapper.selectOne_statement_parameter(user);
    }

    @Override
    public List<User> selectList_statement() {
        return hintMapper.selectList_statement();
    }

    @Override
    public List<User> selectList_statement_parameter(User user) {
        return hintMapper.selectList_statement_parameter(user);
    }

    @Override
    public List<User> selectList_statement_parameter_rowBounds(User user, RowBounds rowBounds) {
        return hintMapper.selectList_statement_parameter_rowBounds(user, rowBounds);
    }

    @Override
    public Map<Integer, User> selectMap_statement_mapKey() {
        return hintMapper.selectMap_statement_mapKey();
    }

    @Override
    public Map<Integer, User> selectMap_statement_parameter_mapKey(User user) {
        return hintMapper.selectMap_statement_parameter_mapKey(user);
    }

    @Override
    public Map<Integer, User> selectMap_statement_parameter_mapKey_rowBounds(User user, RowBounds rowBounds) {
        return hintMapper.selectMap_statement_parameter_mapKey_rowBounds(user, rowBounds);
    }

    @Override
    public User select(User user) {
        return separationMapper.select(user);
    }

    @Override
    public void insert(User user) {
        separationMapper.insert(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User transaction(User user) {
        insert(user);
        return select(user);
    }
}
