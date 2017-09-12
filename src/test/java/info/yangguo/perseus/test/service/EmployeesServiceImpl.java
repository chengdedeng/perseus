package info.yangguo.perseus.test.service;

import info.yangguo.perseus.test.dao.EmployeesMapper1;
import info.yangguo.perseus.test.dao.EmployeesMapper2;

import info.yangguo.perseus.test.domain.Employee;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:杨果
 * @date:2017/6/25 上午11:06
 * <p>
 * Description:
 */
@Service
public class EmployeesServiceImpl implements EmployeesService {
    @Autowired
    private EmployeesMapper1 employeesMapper1;
    @Autowired
    private EmployeesMapper2 employeesMapper2;

    @Override
    @Transactional
    public void testTransaction() {
        Employee e1 = new Employee();
        e1.setId(4);
        e1.setName("yangguo");
        e1.setSalary(2000);
        e1.setSkill("s1");
        employeesMapper2.insert(e1);
        //即使查从库也要路由到主库
        List<Employee> employees1 = employeesMapper2.selectSlave(e1);
        Assert.assertEquals(2, employees1.size());
        e1.setSalary(4000);
        employeesMapper1.update(e1);
        List<Employee> employees2 = employeesMapper2.selectSlave(e1);
        for (Employee employee : employees2) {
            if (employee.getName().equals("yangguo")) {
                Assert.assertEquals(4000, employee.getSalary());
            }
        }
        employeesMapper1.delete("yangguo");
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void testReadonlyTransaction() {
        Employee e1 = new Employee();
        e1.setId(3);
        List<Employee> employees = employeesMapper2.selectSlave(e1);
        Assert.assertEquals(3, employees.size());
        for (Employee employee : employees) {
            Assert.assertEquals("Eli-master", employee.getName());
        }
    }
}
