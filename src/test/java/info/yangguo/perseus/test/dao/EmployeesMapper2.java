package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.Employee;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author:杨果
 * @date:16/7/4 下午3:25
 * <p>
 * Description:
 */
@CacheNamespace
public interface EmployeesMapper2 {
    @Select("/*master*/select\n" +
            "        *\n" +
            "        from\n" +
            "        employees\n" +
            "        where\n" +
            "        name = #{name}")
    @Results(
            {
                    @Result(id = true, column = "name", property = "name")
            })
    @MapKey("name")
    Map<String, Employee> selectMaster(Employee employee);

    @Select("select\n" +
            "        *\n" +
            "        from\n" +
            "        employees\n" +
            "        where\n" +
            "        id = #{id}")
    @Results(
            {
                    @Result(id = true, column = "id", property = "id")
            })
    List<Employee> selectSlave(Employee employee);

    @Insert({"insert into employees(\n" +
            "        id,name,salary,skill)\n" +
            "        VALUES (#{id},#{name},#{salary},#{skill})"})
    void insert(Employee employee);
}
