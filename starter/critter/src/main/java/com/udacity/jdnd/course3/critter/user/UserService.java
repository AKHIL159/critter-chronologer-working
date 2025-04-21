package com.udacity.jdnd.course3.critter.user;


import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public CustomerDTO saveCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setNotes(dto.getNotes());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setNotes(customer.getNotes());

        if (customer.getPets() != null) {
            dto.setPetIds(
                    customer.getPets()
                            .stream()
                            .map(pet -> pet.getId())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO addNewEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setSkills(dto.getSkills());
        employee.setDaysAvailable(dto.getDaysAvailable());

        Employee stored = employeeRepository.save(employee);

        EmployeeDTO result = new EmployeeDTO();
        result.setId(stored.getId());
        result.setName(stored.getName());
        result.setSkills(stored.getSkills());
        result.setDaysAvailable(stored.getDaysAvailable());
        return result;
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return null; // Or throw an exception depending on your preference
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setSkills(employee.getSkills());
        dto.setDaysAvailable(employee.getDaysAvailable());

        return dto;
    }

    public void updateAvailability(Long employeeId, Set<DayOfWeek> availableDays) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee != null) {
            employee.setDaysAvailable(availableDays);
            employeeRepository.save(employee);
        }
    }

    public List<EmployeeDTO> findMatchingEmployees(EmployeeRequestDTO request) {
        Set<DayOfWeek> targetDay = Set.of(request.getDate().getDayOfWeek());

        return employeeRepository.findAll().stream()
                .filter(emp -> emp.getDaysAvailable() != null &&
                        emp.getDaysAvailable().containsAll(targetDay))
                .filter(emp -> emp.getSkills() != null &&
                        emp.getSkills().containsAll(request.getSkills()))
                .map(emp -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setId(emp.getId());
                    dto.setName(emp.getName());
                    dto.setSkills(emp.getSkills());
                    dto.setDaysAvailable(emp.getDaysAvailable());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CustomerDTO getOwnerByPetId(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null || pet.getOwner() == null) return null;

        Customer owner = pet.getOwner();

        CustomerDTO dto = new CustomerDTO();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setPhoneNumber(owner.getPhoneNumber());
        dto.setNotes(owner.getNotes());

        if (owner.getPets() != null) {
            dto.setPetIds(owner.getPets()
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList()));
        }

        return dto;
    }


}
