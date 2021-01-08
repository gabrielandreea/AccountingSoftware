package com.company.service;

import com.company.exception.InvalidUserInteractionException;
import com.company.model.Department;
import com.company.model.Employee;

import java.util.Scanner;

import static com.company.util.Constants.*;

public class UserInteractionServiceImpl implements UserInteractionService {
    private Scanner scanner = new Scanner(System.in);
    EmployeeInteractionService employeeInteractionService = new EmployeeInteractionServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();


    @Override
    public void initInteraction() {
        switch (chooseInitialAction()) {
            case ACCESS_DATABASE:
                switch (chooseAccessDatabaseAction()) {
                    case LIST_EMPLOYEES:
                        employeeService.listEmployees();
                        break;
                    case ADD_EMPLOYEE:
                        //Preluam datele de la tastatura
                       Employee employee = employeeInteractionService.addEmployeeAction();
                        // Salvam informatiile in baza de date
                        // layerul Service, acceseaza direct baza de date.
                        // UserInteractionService delega responsabilitatea catra EmployeeService
                        // care la randul saui transmite datele catre EmployeeRepository care face salvarea propriu-zisa
                        employeeService.addEmployee(employee);
                        break;
                    case DELETE_EMPLOYEE:
                        int idToBeDeleted = employeeInteractionService.deleteEmployeeAction();
                        employeeService.deleteEmployee(idToBeDeleted);
                        break;
                    case UPDATE_EMPLOYEES:
                        Employee employeeToBeUpdated = employeeInteractionService.updateEmployeeAction();
                        employeeService.updateEmployee(employeeToBeUpdated);
                        break;
                }
                break;
                case VIEW_REPORTS:
                switch (chooseViewReportsAction()){
                    case AVERAGE_SALARY_BY_COMPANY:
                        System.out.println("Show average salary" + employeeService.calculateAverageSalary());
                        break;
                    case AVERAGE_SALARY_BY_DEPARTMENT:
                        Department department = employeeInteractionService.chooseDepartmentAction();
                        //cere de la tastatura departamentul
                        //apoi calculeaza media salariilor diltrand by department
                        Double averageByDepartment = employeeService.calculateAverageSalaryByDepartment(department);
                        System.out.println("Average salary for : " + department + " is " + averageByDepartment);
                        break;
                }
                break;
        }
        initInteraction();
    }

    private Integer chooseViewReportsAction() {
        System.out.println("Average salary by company - pres " + AVERAGE_SALARY_BY_COMPANY);
        System.out.println("Average salary by department - pres " + AVERAGE_SALARY_BY_DEPARTMENT);

        try {
            Integer action = Integer.parseInt(scanner.nextLine());
            if (action != AVERAGE_SALARY_BY_COMPANY && action != AVERAGE_SALARY_BY_DEPARTMENT) {
                throw new InvalidUserInteractionException();
            }
            return action;
        }catch (Exception e){
            System.out.println("Please enter a valid number for your action");
        }
        return chooseViewReportsAction();

    }

    private Integer chooseInitialAction() {
        System.out.println("Choose action : ");
        System.out.println("Access database - press " + ACCESS_DATABASE);
        System.out.println("View Reports - press " + VIEW_REPORTS);

        try {
            Integer action = Integer.parseInt(scanner.nextLine());
            if (action != ACCESS_DATABASE && action != VIEW_REPORTS) {
                throw new InvalidUserInteractionException();
            }
            return action;
        } catch (Exception ex) {
            System.out.println("Please enter a valid number : " + ACCESS_DATABASE + " (access database) or " + VIEW_REPORTS + " (view reports) !");
        }
        return chooseInitialAction();
    }

    private Integer chooseAccessDatabaseAction() {
        System.out.println("List employees - press " + LIST_EMPLOYEES);
        System.out.println("Add employee - press " + ADD_EMPLOYEE);
        System.out.println("Delete employee - press " + DELETE_EMPLOYEE);
        System.out.println("Update employees - press " + UPDATE_EMPLOYEES);

        try {
            Integer action = Integer.parseInt(scanner.nextLine());
            if (action != LIST_EMPLOYEES && action != ADD_EMPLOYEE && action != DELETE_EMPLOYEE && action != UPDATE_EMPLOYEES ){
                throw new InvalidUserInteractionException();
            }
            return action;
        }catch (Exception e){
            System.out.println("Please enter a valid number for action !");
        }
        return chooseAccessDatabaseAction();
    }
}
