package info.yangguo.perseus.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import info.yangguo.perseus.test.dao.CountryMapper;
import info.yangguo.perseus.test.domain.Country;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author:杨果
 * @date:2017/8/18 上午11:18
 *
 * Description:
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext3.xml"})
public class PageHelperTest {
    @Autowired
    private CountryMapper countryMapper;

    /**
     * 使用Mapper接口调用时，使用PageHelper.startPage效果更好，不需要添加Mapper接口参数
     */
    @Test
    public void testMapperWithStartPage() {
        //获取第20页，2条内容
        //分页插件会自动改为查询最后一页
        PageHelper.startPage(20, 50);
        List<Country> list = countryMapper.selectAll();
        PageInfo<Country> page = new PageInfo<Country>(list);
        assertEquals(33, list.size());
        assertEquals(151, page.getStartRow());
        assertEquals(4, page.getPageNum());
        assertEquals(183, page.getTotal());

        //获取第-3页，2条内容
        //由于只有7天数据，分页插件会自动改为查询最后一页
        PageHelper.startPage(-3, 50);
        list = countryMapper.selectAll();
        page = new PageInfo<Country>(list);
        assertEquals(50, list.size());
        assertEquals(1, page.getStartRow());
        assertEquals(1, page.getPageNum());
        assertEquals(183, page.getTotal());
    }
}
