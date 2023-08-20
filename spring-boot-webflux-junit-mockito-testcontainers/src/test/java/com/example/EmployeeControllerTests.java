package com.example;

import com.example.controller.EmployeeController;
import com.example.dto.EmployeeDto;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_returnListOfEmployees() {
        List<EmployeeDto> list = new ArrayList<>();
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Ramesh");
        employeeDto1.setLastName("Fadatare");
        employeeDto1.setEmail("ramesh@gmail.com");
        list.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Tony");
        employeeDto2.setLastName("Starck");
        employeeDto2.setEmail("tony@gmail.com");
        list.add(employeeDto2);

        Flux<EmployeeDto> employeeFlux = Flux.fromIterable(list);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeFlux);

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class),
                        ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        WebTestClient.ResponseSpec response = webTestClient
                .delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}