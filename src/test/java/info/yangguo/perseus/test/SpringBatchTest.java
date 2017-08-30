/**
 * Copyright 2010-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package info.yangguo.perseus.test;

import info.yangguo.perseus.DynamicMyBatisCursorItemReader;
import info.yangguo.perseus.DynamicMyBatisPagingItemReader;
import info.yangguo.perseus.DynamicSqlSessionTemplate;
import info.yangguo.perseus.test.domain.Employee;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext2.xml"})
public class SpringBatchTest {

    @Autowired
    @Qualifier("pagingNoNestedItemReader")
    private DynamicMyBatisPagingItemReader<Employee> pagingNoNestedItemReader;

    @Autowired
    @Qualifier("pagingNestedItemReader")
    private DynamicMyBatisPagingItemReader<Employee> pagingNestedItemReader;

    @Autowired
    @Qualifier("cursorNoNestedItemReader")
    private DynamicMyBatisCursorItemReader<Employee> cursorNoNestedItemReader;

    @Autowired
    @Qualifier("cursorNestedItemReader")
    private DynamicMyBatisCursorItemReader<Employee> cursorNestedItemReader;

    @Autowired
    private MyBatisBatchItemWriter<Employee> writer;

    @Autowired
    @Qualifier("dynamicSqlSession")
    private DynamicSqlSessionTemplate session;

    Pattern pattern1 = Pattern.compile(".*-master$");
    Pattern pattern2 = Pattern.compile(".*-slave(1|2)");

    @Test
    @Transactional
    public void shouldDuplicateSalaryOfAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee = pagingNoNestedItemReader.read();
        while (employee != null) {
            Assert.assertTrue(pattern1.matcher(employee.getName()).matches());
            employee.setSalary(employee.getSalary() * 2);
            employees.add(employee);
            employee = pagingNoNestedItemReader.read();
        }
        writer.write(employees);

        assertThat((Integer) session.selectOne("checkSalarySum")).isEqualTo(20000);
        assertThat((Integer) session.selectOne("checkEmployeeCount")).isEqualTo(employees.size());
    }

    @Test
    public void pagingNoNestedItemReader() throws Exception {
        Employee employee = pagingNoNestedItemReader.read();
        while (employee != null) {
            Assert.assertTrue(pattern2.matcher(employee.getName()).matches());
            employee = pagingNoNestedItemReader.read();
        }
    }

    @Test
    @Transactional
    public void checkPagingReadingWithNestedInResultMap() throws Exception {
        // This test is here to show that PagingReader can return wrong result in case of nested result maps
        List<Employee> employees = new ArrayList<>();
        Employee employee = pagingNestedItemReader.read();
        while (employee != null) {
            Assert.assertTrue(pattern1.matcher(employee.getName()).matches());
            employee.setSalary(employee.getSalary() * 2);
            employees.add(employee);
            employee = pagingNestedItemReader.read();
        }
        writer.write(employees);

        // Assert that we have a WRONG employee count
        assertThat((Integer) session.selectOne("checkEmployeeCount")).isNotEqualTo(employees.size());
    }

    @Test
    public void pagingNestedItemReader() throws Exception {
        // This test is here to show that PagingReader can return wrong result in case of nested result maps
        Employee employee = pagingNestedItemReader.read();
        while (employee != null) {
            Assert.assertTrue(pattern2.matcher(employee.getName()).matches());
            employee = pagingNestedItemReader.read();
        }
    }

    @Test
    @Transactional
    public void checkCursorReadingWithoutNestedInResultMap() throws Exception {
        cursorNoNestedItemReader.doOpen();
        try {
            List<Employee> employees = new ArrayList<>();
            Employee employee = cursorNoNestedItemReader.read();
            while (employee != null) {
                Assert.assertTrue(pattern1.matcher(employee.getName()).matches());
                employee.setSalary(employee.getSalary() * 2);
                employees.add(employee);
                employee = cursorNoNestedItemReader.read();
            }
            writer.write(employees);

            assertThat((Integer) session.selectOne("checkSalarySum")).isEqualTo(20000);
            assertThat((Integer) session.selectOne("checkEmployeeCount")).isEqualTo(employees.size());
        } finally {
            cursorNoNestedItemReader.doClose();
        }
    }

    @Test
    public void cursorNoNestedItemReader() throws Exception {
        cursorNoNestedItemReader.doOpen();
        try {
            Employee employee = cursorNoNestedItemReader.read();
            while (employee != null) {
                Assert.assertTrue(pattern2.matcher(employee.getName()).matches());
                employee = cursorNoNestedItemReader.read();
            }
        } finally {
            cursorNoNestedItemReader.doClose();
        }
    }

    @Test
    @Transactional
    public void checkCursorReadingWithNestedInResultMap() throws Exception {
        cursorNestedItemReader.doOpen();
        try {
            List<Employee> employees = new ArrayList<>();
            Employee employee = cursorNestedItemReader.read();
            while (employee != null) {
                Assert.assertTrue(pattern1.matcher(employee.getName()).matches());
                employee.setSalary(employee.getSalary() * 2);
                employees.add(employee);
                employee = cursorNestedItemReader.read();
            }
            writer.write(employees);

            assertThat((Integer) session.selectOne("checkSalarySum")).isEqualTo(20000);
            assertThat((Integer) session.selectOne("checkEmployeeCount")).isEqualTo(employees.size());
        } finally {
            cursorNestedItemReader.doClose();
        }
    }

    @Test
    public void cursorNestedItemReader() throws Exception {
        cursorNestedItemReader.doOpen();
        try {
            Employee employee = cursorNestedItemReader.read();
            while (employee != null) {
                Assert.assertTrue(pattern2.matcher(employee.getName()).matches());
                employee = cursorNestedItemReader.read();
            }
        } finally {
            cursorNestedItemReader.doClose();
        }
    }
}
