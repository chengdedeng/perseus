package info.yangguo.perseus.test;

import info.yangguo.perseus.test.dao.EmployeesMapper1;
import info.yangguo.perseus.test.dao.EmployeesMapper2;
import info.yangguo.perseus.test.domain.Employee;
import info.yangguo.perseus.test.service.EmployeesService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:杨果
 * @date:16/7/4 下午1:42
 * <p>
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext1.xml"})
public class RWTest {
    @Autowired
    private EmployeesMapper1 employeesMapper1;
    @Autowired
    private EmployeesMapper2 employeesMapper2;
    @Autowired
    private EmployeesService employeesService;

    @Test
    public void transaction() {
        employeesService.testTransaction();
    }

    @Test
    public void selectMaster() {
        Employee employee = new Employee();
        employee.setName("Valentina-master");
        Map<String, Employee> employeeMap = employeesMapper2.selectMaster(employee);
        Assert.assertEquals(4000, employeeMap.get("Valentina-master").getSalary());

    }


    @Test
    public void readonlyTransaction() {
        employeesService.testReadonlyTransaction();
    }
}
