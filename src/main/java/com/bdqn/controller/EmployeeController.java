package com.bdqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.common.R;
import com.bdqn.pojo.Employee;
import com.bdqn.service.EmployeeService;
import com.bdqn.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null){
            return R.error("用户名输入错误！");
        }

        if (emp.getPassword().equals(password)){
            return R.error("密码输入错误！");
        }

        if (emp.getStatus() == 0){
            return R.error("账号已经被封禁！");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    @PostMapping("/save")
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(new Date());
//        employee.setUpdateTime(new Date());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        System.out.println(id);
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);
        employeeService.save(employee);
        return R.success("保存成功！");
    }

    @RequestMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping("/update")
    public R<String> update(@RequestBody Employee employee){
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(new Date());
        employeeService.updateById(employee);
        return R.success("更新信息成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
