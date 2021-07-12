package com.engulf.controller;

import com.engulf.dao.DepartmentDao;
import com.engulf.dao.EmpolyeeDao;
import com.engulf.pojo.Department;
import com.engulf.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {
    @Autowired
    private EmpolyeeDao empolyeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    //查询所有员工信息
    @GetMapping("/selectAllEmploy")
    public String selectAllEmploy(Model model){
        model.addAttribute("employees",empolyeeDao.getAllEmployees());
        return "dynamic_table";
    }


    @GetMapping("/toAddEmploy")
    public String addEmploy(Model model){
        //向前端传递部门的信息
        model.addAttribute("department",departmentDao.getDepartments());
        return "emp/toAddEmploy";
    }

    @PostMapping("/toAddEmploy")
    public String addEmployee(Employee employee, @RequestParam("departID") Integer departId){
        employee.setDepartment(departmentDao.getDepartmentById(departId));
        //添加员工
        empolyeeDao.addEmployee(employee);
        System.out.println("Employ---->" + employee);
        return "redirect:/selectAllEmploy";
    }

    @RequestMapping("/toUpdateEmploy/{eid}")
    public String toUpdateEmploy(@PathVariable("eid") Integer eid,Model model){
        //查出原来的数据
        model.addAttribute("theEmploy",empolyeeDao.getEmployeeById(eid));
        model.addAttribute("department",departmentDao.getDepartments());
        return "emp/toUpdateEmploy";
    }

    @PostMapping("/toUpdateEmploy/{eid}")
    public String updateEmploy(@PathVariable("eid") Integer eid,Employee employee){
        empolyeeDao.addEmployee(employee);
        return "redirect:/selectAllEmploy";
    }

    @GetMapping("/toDelEmploy/{eid}/{uid}")
    public String delEmploy(@PathVariable("eid") Integer eid,@PathVariable("uid") Integer uid){
        empolyeeDao.deleteEmployee(eid);
        System.out.println("uid==>" + uid);
        return "redirect:/selectAllEmploy";
    }
}
