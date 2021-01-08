package com.company.service;

import com.company.mapper.EmployeeMapper;
import com.company.model.Department;
import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import com.company.repository.EmployeeRepositoryImpl;

import java.util.ArrayList;
import java.util.List;


public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
    private EmployeeMapper employeeMapper = new EmployeeMapper();

    @Override
    public void listEmployees() {
        List<String> csvLines = employeeRepository.readCsv();
        for (String csvLine : csvLines) {
            Employee employee = employeeMapper.getEmployeeFromCsvLine(csvLine);
            if (employee != null) {
                System.out.println(employee.toString());
            }
        }
    }
    @Override
    public void addEmployee(Employee employee) {
        String employeeString = employeeMapper.getCsvLineFromEmployee(employee);
        employeeRepository.insertLine(employeeString);
    }

    @Override
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteLine(id);
    }

    @Override
    public void updateEmployee(Employee employee) {
        String employeeString = employeeMapper.getCsvLineFromEmployee(employee);
        employeeRepository.updateLine(employee.getId(), employeeString);

    }

    @Override
    public Double calculateAverageSalary() {
        List<Employee> employees = employeeMapper.getEmployeeList(employeeRepository.readCsv());
        return calculateAverageSalary(employees);

    }

    @Override
    public Double calculateAverageSalaryByDepartment(Department department) {
        //avem toti angajatii
        List<Employee> employees = employeeMapper.getEmployeeList(employeeRepository.readCsv());
        List<Employee> filteredEmployees = new ArrayList<>();
        //luam fiecare angajat unul cate unul si verificam daca departamentul este cel dorit
        for (Employee employee : employees){
            if (employee.getDepartment().equals(department)){
                filteredEmployees.add(employee);
            }
        }
        //avem lista cu toti angajatii cutati
        //daca avem lista cu toti angajatii cautati, acum putem calcula edia salarilor lor
        return  calculateAverageSalary(filteredEmployees);
    }

    private Double calculateAverageSalary(List<Employee> employees){
        Double salarySum = 0.0;
        for (Employee employee : employees){
            salarySum += employee.getSalary();
        }
        if (employees.size() < 1){
            return 0.0;
        }
        return salarySum / employees.size();
    }

}
