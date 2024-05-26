package com.runyin.services.impl;

import com.runyin.config.PaginationResultVO;
import com.runyin.config.SimplePage;
import com.runyin.entity.Employee;
import com.runyin.mappers.EmployeeMapper;
import com.runyin.query.EmployeesQuery;
import com.runyin.services.EmployeesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("employeesService")
public class EmployeesServiceImpl implements EmployeesService {
    @Resource
    private EmployeeMapper<Employee,EmployeesQuery> employeeMapper;

    @Override
    public Integer findCountByParam(EmployeesQuery query) {
        return employeeMapper.selectCount(query);
    }

    @Override
    public List<Employee> findBeanListByParam(EmployeesQuery query) {
        return employeeMapper.selectList(query);
    }

    @Override
    public PaginationResultVO<Employee> findEmployeeListByPage(EmployeesQuery query) {
        Integer total = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(), query.getPageSize(), total);
        query.setSimplePage(simplePage);
        List<Employee> list = this.findBeanListByParam(query);

        PaginationResultVO<Employee> resultVO = new PaginationResultVO(
                simplePage.getPageNo(),
                simplePage.getPageSize(),
                simplePage.getTotal(),
                simplePage.getPageTotal(),
                list
        );
        return resultVO;
    }
}
