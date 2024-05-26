package com.runyin.query;


import com.runyin.config.BaseQuery;

import java.math.BigDecimal;
import java.util.Date;

public class EmployeesQuery extends BaseQuery {
    private Integer employeeId;

    private String lastName;
    private String lastNameFuzzy;

    private String firstName;
    private String firstNameFuzzy;

    private String phoneNumber;
    private String phoneNumberFuzzy;

    private String email;
    private String emailFuzzy;

    private Date hireDate;
    private Date hireDateStart;
    private Date hireDateEnd;

    private String jobId;
    private String jobIdFuzzy;

    private BigDecimal salary;
    private BigDecimal commissionPct;
    private Integer managerId;
    private Integer departmentId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameFuzzy() {
        return lastNameFuzzy;
    }

    public void setLastNameFuzzy(String lastNameFuzzy) {
        this.lastNameFuzzy = lastNameFuzzy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstNameFuzzy() {
        return firstNameFuzzy;
    }

    public void setFirstNameFuzzy(String firstNameFuzzy) {
        this.firstNameFuzzy = firstNameFuzzy;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberFuzzy() {
        return phoneNumberFuzzy;
    }

    public void setPhoneNumberFuzzy(String phoneNumberFuzzy) {
        this.phoneNumberFuzzy = phoneNumberFuzzy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailFuzzy() {
        return emailFuzzy;
    }

    public void setEmailFuzzy(String emailFuzzy) {
        this.emailFuzzy = emailFuzzy;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getHireDateStart() {
        return hireDateStart;
    }

    public void setHireDateStart(Date hireDateStart) {
        this.hireDateStart = hireDateStart;
    }

    public Date getHireDateEnd() {
        return hireDateEnd;
    }

    public void setHireDateEnd(Date hireDateEnd) {
        this.hireDateEnd = hireDateEnd;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobIdFuzzy() {
        return jobIdFuzzy;
    }

    public void setJobIdFuzzy(String jobIdFuzzy) {
        this.jobIdFuzzy = jobIdFuzzy;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(BigDecimal commissionPct) {
        this.commissionPct = commissionPct;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
