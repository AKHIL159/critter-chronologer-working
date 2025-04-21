package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public ScheduleDTO createSchedule(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setDate(dto.getDate());
        schedule.setActivities(dto.getActivities());

        List<Employee> employees = employeeRepository.findAllById(dto.getEmployeeIds());
        List<Pet> pets = petRepository.findAllById(dto.getPetIds());

        schedule.setEmployees(employees);
        schedule.setPets(pets);

        Schedule saved = scheduleRepository.save(schedule);

        ScheduleDTO result = new ScheduleDTO();
        result.setId(saved.getId());
        result.setDate(saved.getDate());
        result.setActivities(saved.getActivities());
        result.setEmployeeIds(saved.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        result.setPetIds(saved.getPets().stream().map(Pet::getId).collect(Collectors.toList()));

        return result;
    }

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setActivities(schedule.getActivities());
        dto.setEmployeeIds(schedule.getEmployees()
                .stream()
                .map(e -> e.getId())
                .collect(Collectors.toList()));
        dto.setPetIds(schedule.getPets()
                .stream()
                .map(p -> p.getId())
                .collect(Collectors.toList()));
        return dto;
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        return scheduleRepository.findAllByPetsContains(pet).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return scheduleRepository.findAllByEmployeesContains(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null || customer.getPets() == null) return new ArrayList<>();

        return scheduleRepository.findAll().stream()
                .filter(sch -> sch.getPets().stream()
                        .anyMatch(pet -> customer.getPets().contains(pet)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
