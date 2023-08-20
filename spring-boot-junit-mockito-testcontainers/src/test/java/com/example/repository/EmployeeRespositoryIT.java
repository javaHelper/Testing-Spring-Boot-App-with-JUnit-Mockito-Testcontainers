package com.example.repository;

import com.example.entity.Employee;
import com.example.integration.AbstractContainerBaseTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRespositoryIT extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@springframework.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

        assertThat(savedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }


    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@springframework.com")
                .build();

        employeeRepository.saveAll(Arrays.asList(employee, employee1));

        List<Employee> employeeList = employeeRepository.findAll();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

        // Best way is
        assertThat(employeeList)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder(Tuple.tuple("Mike", "Doe", "mike.doe@springframework.com"), Tuple.tuple("Jane", "Doe", "jane.doe@springframework.com"));
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }

    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        employeeRepository.save(employee);

        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("jane.doe@springframework.com");
        savedEmployee.setFirstName("Jane");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("jane.doe@springframework.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");

        assertThat(updatedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Jane", "Doe", "jane.doe@springframework.com");
    }

    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        employeeRepository.save(employee);
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastName("Mike", "Doe");
        assertThat(savedEmployee).isNotNull();

        assertThat(savedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }

    @DisplayName("JUnit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        String firstName = "Mike";
        String lastName = "Doe";

        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNew(firstName, lastName);
        assertThat(savedEmployee).isNotNull();

        assertThat(savedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }

    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNative(employee.getFirstName(), employee.getLastName());
        assertThat(savedEmployee).isNotNull();

        assertThat(savedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }

    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNew(employee.getFirstName(), employee.getLastName());
        assertThat(savedEmployee).isNotNull();

        assertThat(savedEmployee)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder("Mike", "Doe", "mike.doe@springframework.com");
    }
}