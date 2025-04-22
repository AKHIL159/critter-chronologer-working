package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());

        Customer saved = userService.saveCustomer(customer);
        CustomerDTO result = new CustomerDTO();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setPhoneNumber(saved.getPhoneNumber());
        result.setNotes(saved.getNotes());

        if (saved.getPets() != null) {
            List<Long> petIds = new ArrayList<>();
            saved.getPets().forEach(pet -> petIds.add(pet.getId()));
            result.setPetIds(petIds);
        }

        return result;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = userService.getAllCustomers();
        List<CustomerDTO> result = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setPhoneNumber(customer.getPhoneNumber());
            dto.setNotes(customer.getNotes());

            if (customer.getPets() != null) {
                List<Long> petIds = new ArrayList<>();
                customer.getPets().forEach(pet -> petIds.add(pet.getId()));
                dto.setPetIds(petIds);
            }

            result.add(dto);
        }
        return result;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = userService.getOwnerByPetId(petId);
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setNotes(customer.getNotes());

        if (customer.getPets() != null) {
            List<Long> petIds = new ArrayList<>();
            customer.getPets().forEach(pet -> petIds.add(pet.getId()));
            dto.setPetIds(petIds);
        }

        return dto;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());

        Employee saved = userService.addNewEmployee(employee);

        EmployeeDTO result = new EmployeeDTO();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setSkills(saved.getSkills());
        result.setDaysAvailable(saved.getDaysAvailable());
        return result;
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.getEmployeeById(employeeId);
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setSkills(employee.getSkills());
        dto.setDaysAvailable(employee.getDaysAvailable());
        return dto;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.updateAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = userService.findMatchingEmployees(employeeDTO);
        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setId(employee.getId());
            dto.setName(employee.getName());
            dto.setSkills(employee.getSkills());
            dto.setDaysAvailable(employee.getDaysAvailable());
            result.add(dto);
        }
        return result;
    }
}
