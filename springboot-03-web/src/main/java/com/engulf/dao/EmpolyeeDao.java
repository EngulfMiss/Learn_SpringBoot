package com.engulf.dao;

import com.engulf.pojo.Department;
import com.engulf.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
@SuppressWarnings("all")
public class EmpolyeeDao {
    //模拟数据库中的数据
    private static Map<Integer, Employee> employees = null;

    @Autowired
    private DepartmentDao departmentDao;
    static {
        employees = new HashMap<Integer, Employee>();
        employees.put(1000,new Employee(1000,"Kindred","kindred@lol.com",0,new Department(101,"教学部"),new Date()));
        employees.put(1001,new Employee(1001,"Gnar","gnar@lol.com",1,new Department(105,"后勤部"),new Date()));
        employees.put(1002,new Employee(1002,"Neeko","neeko@lol.com",0,new Department(104,"运营部"),new Date()));
    }


    //主键自增
    private static Integer initId = 1006;
    //增加一个员工
    public void addEmployee(Employee employee){
        if (employee.getId()==null){
            employee.setId(initId++);
        }

        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
        employees.put(employee.getId(),employee);
    }

    //查询所有员工信息
    public Collection<Employee> getAllEmployees(){
        return employees.values();
    }

    //通过id查询员工
    public Employee getEmployeeById(Integer id){
        return employees.get(id);
    }

    //修改员工信息
//    public Employee updateEmployeeById(Integer id){
//        Employee employeeById = getEmployeeById(id);
//
//        return
//    }

    //删除员工
    public void deleteEmployee(Integer id){
        employees.remove(id);
    }

}
