package info.yangguo.perseus.test.dao;

import info.yangguo.perseus.test.domain.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeesMapper1 {
    void delete(@Param(value = "name") String name);

    int update(Employee employee);
}
