package info.yangguo.perseus.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import info.yangguo.perseus.test.dao.CountryMapper;
import info.yangguo.perseus.test.domain.Country;

import org.junit.Assert;
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
 * <p>
 * Description:
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
    public void testMapperWithStartPage1() {
        //获取第20页，2条内容
        //分页插件会自动改为查询最后一页
        PageHelper.startPage(20, 50);
        List<Country> list = countryMapper.selectAll();
        PageInfo<Country> page = new PageInfo<Country>(list);
        assertEquals(38, list.size());
        assertEquals(151, page.getStartRow());
        assertEquals(4, page.getPageNum());
        assertEquals(188, page.getTotal());

        //获取第-3页，2条内容
        //由于只有7天数据，分页插件会自动改为查询最后一页
        PageHelper.startPage(-3, 50);
        list = countryMapper.selectAll();
        page = new PageInfo<Country>(list);
        assertEquals(50, list.size());
        assertEquals(1, page.getStartRow());
        assertEquals(1, page.getPageNum());
        assertEquals(188, page.getTotal());
    }


    /**
     * 使用Mapper接口调用时，使用PageHelper.startPage效果更好，不需要添加Mapper接口参数
     */
    @Test
    public void testMapperWithStartPage2() {
        //获取第20页，2条内容
        //分页插件会自动改为查询最后一页
        Country country = new Country();
        country.setCountryname("China");


        PageHelper.startPage(1, 1);
        List<Country> list1 = countryMapper.selectByCountryname(country);
        Assert.assertEquals(1, list1.size());
        PageInfo<Country> page1 = new PageInfo<Country>(list1);
        Assert.assertEquals(1, page1.getPageNum());
        Assert.assertEquals(1, page1.getPageSize());
        PageHelper.startPage(2, 2);
        List<Country> list2 = countryMapper.selectByCountryname(country);
        Assert.assertEquals(2, list2.size());
        PageInfo<Country> page2 = new PageInfo<Country>(list2);
        Assert.assertEquals(2, page2.getPageNum());
        Assert.assertEquals(2, page2.getPageNum());
    }
}
