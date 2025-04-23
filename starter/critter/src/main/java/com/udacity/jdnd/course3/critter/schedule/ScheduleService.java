package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        List<Pet> pets = petRepository.findAllById(petIds);

        schedule.setEmployees(employees);
        schedule.setPets(pets);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        return (pet != null) ? scheduleRepository.findAllByPetsContains(pet) : java.util.Collections.emptyList();
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return (employee != null) ? scheduleRepository.findAllByEmployeesContains(employee) : java.util.Collections.emptyList();
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null || customer.getPets() == null) return java.util.Collections.emptyList();

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule ->
                        schedule.getPets().stream().anyMatch(pet -> customer.getPets().contains(pet)))
                .collect(Collectors.toList());
    }
}
