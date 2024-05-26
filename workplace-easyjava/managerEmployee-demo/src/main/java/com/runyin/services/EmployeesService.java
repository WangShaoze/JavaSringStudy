package com.runyin.services;

import com.runyin.config.PaginationResultVO;
import com.runyin.entity.Employee;
import com.runyin.query.EmployeesQuery;

import java.util.List;

public interface EmployeesService {

    /**
     * 更具条件查询数量 findCountByParam
     * */

    Integer findCountByParam(EmployeesQuery query);

    /**
     * 更具条件查询记录 findBeanListByParam
     * */
    List<Employee> findBeanListByParam(EmployeesQuery query);

    /**
     * 分页查询
     * 根据条件查询员工的信息
     * */
    PaginationResultVO<Employee> findEmployeeListByPage(EmployeesQuery query);

}
