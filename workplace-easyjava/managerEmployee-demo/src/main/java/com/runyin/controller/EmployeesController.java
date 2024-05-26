package com.runyin.controller;

import com.runyin.config.BaseController;
import com.runyin.config.ResponseVO;
import com.runyin.query.EmployeesQuery;
import com.runyin.services.EmployeesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/employees")
public class EmployeesController extends BaseController {

    @Resource
    private EmployeesService employeesService;
    /**
     * 分页查询员工的信息
     * */
    @GetMapping("/emp_info")
    public ResponseVO searchEmployeesInfo(EmployeesQuery query){
        return getSuccessResponseVO(employeesService.findEmployeeListByPage(query));
    }

}
