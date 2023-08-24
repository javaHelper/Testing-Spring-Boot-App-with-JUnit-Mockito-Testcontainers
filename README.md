# -Testing-Spring-Boot-App-with-JUnit-Mockito-Testcontainers
 Testing Spring Boot App with JUnit, Mockito &amp; Testcontainers

 Done and dusted 

 - Repository Test

```java
import com.example.entity.Employee;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRespositoryTests {

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
```

------

```java
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
```

----

# Service

```java
import com.example.entity.Employee;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();
    }

    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        assertThat(savedEmployee).isNotNull()
                .extracting(Employee::getId, Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder(1L, "Mike", "Doe", "mike.doe@gmail.com");
    }

    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony.stark@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        List<Employee> employeeList = employeeService.getAllEmployees();
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder(tuple("Mike", "Doe", "mike.doe@gmail.com"), tuple("Tony", "Stark", "tony.stark@gmail.com"));
    }

    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isEmpty();
        assertThat(employeeList)
                .isEmpty();
    }

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        assertThat(savedEmployee).isNotNull()
                .extracting(Employee::getId, Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder(1L, "Mike", "Doe", "mike.doe@gmail.com");

    }

    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("ram@gmail.com");
        employee.setFirstName("Ram");
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        assertThat(updatedEmployee).isNotNull()
                .extracting(Employee::getId, Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactlyInAnyOrder(1L, "Ram", "Doe", "ram@gmail.com");
    }

    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        employeeService.deleteEmployee(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
```

-----

# Controller 

```java
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Mike").lastName("Doe").email("mike.doe@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Jane").lastName("Doe").email("jane.doe@gmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        response.andExpect(status().isOk()).andDo(print());
    }
}
```

-------

# Integration

```java
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractContainerBaseTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");

        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }
}
```


```
import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerIT extends AbstractContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
        employeeRepository.deleteAll();
    }


    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Mike").lastName("Doe").email("Mike@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(employee);

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(employee);
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
```

```
import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
        employeeRepository.deleteAll();
    }


    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("mike.doe@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Mike").lastName("Doe").email("Mike@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(employee);

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(employee);
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        Employee savedEmployee = Employee.builder()
                .firstName("Mike")
                .lastName("Doe")
                .email("Mike@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
```
