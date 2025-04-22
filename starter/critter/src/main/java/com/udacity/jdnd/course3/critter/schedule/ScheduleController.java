package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        Schedule saved = scheduleService.createSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());

        ScheduleDTO result = new ScheduleDTO();
        result.setId(saved.getId());
        result.setDate(saved.getDate());
        result.setActivities(saved.getActivities());

        if (saved.getEmployees() != null) {
            List<Long> employeeIds = new ArrayList<>();
            for (Employee e : saved.getEmployees()) {
                employeeIds.add(e.getId());
            }
            result.setEmployeeIds(employeeIds);
        }

        if (saved.getPets() != null) {
            List<Long> petIds = new ArrayList<>();
            for (Pet p : saved.getPets()) {
                petIds.add(p.getId());
            }
            result.setPetIds(petIds);
        }

        return result;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return buildScheduleDTOs(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return buildScheduleDTOs(scheduleService.getScheduleForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return buildScheduleDTOs(scheduleService.getScheduleForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return buildScheduleDTOs(scheduleService.getScheduleForCustomer(customerId));
    }

    private List<ScheduleDTO> buildScheduleDTOs(List<Schedule> schedules) {
        List<ScheduleDTO> results = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO dto = new ScheduleDTO();
            dto.setId(schedule.getId());
            dto.setDate(schedule.getDate());
            dto.setActivities(schedule.getActivities());

            if (schedule.getEmployees() != null) {
                List<Long> employeeIds = new ArrayList<>();
                for (Employee e : schedule.getEmployees()) {
                    employeeIds.add(e.getId());
                }
                dto.setEmployeeIds(employeeIds);
            }

            if (schedule.getPets() != null) {
                List<Long> petIds = new ArrayList<>();
                for (Pet p : schedule.getPets()) {
                    petIds.add(p.getId());
                }
                dto.setPetIds(petIds);
            }

            results.add(dto);
        }
        return results;
    }
}
