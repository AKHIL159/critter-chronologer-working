package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());

        Pet savedPet = petService.savePet(pet, petDTO.getOwnerId());

        PetDTO result = new PetDTO();
        result.setId(savedPet.getId());
        result.setName(savedPet.getName());
        result.setType(savedPet.getType());
        result.setBirthDate(savedPet.getBirthDate());
        result.setNotes(savedPet.getNotes());

        if (savedPet.getOwner() != null) {
            result.setOwnerId(savedPet.getOwner().getId());
        }

        return result;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());

        if (pet.getOwner() != null) {
            dto.setOwnerId(pet.getOwner().getId());
        }

        return dto;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        List<PetDTO> results = new ArrayList<>();

        for (Pet pet : pets) {
            PetDTO dto = new PetDTO();
            dto.setId(pet.getId());
            dto.setName(pet.getName());
            dto.setType(pet.getType());
            dto.setBirthDate(pet.getBirthDate());
            dto.setNotes(pet.getNotes());
            if (pet.getOwner() != null) {
                dto.setOwnerId(pet.getOwner().getId());
            }
            results.add(dto);
        }

        return results;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        List<PetDTO> result = new ArrayList<>();

        for (Pet pet : pets) {
            PetDTO dto = new PetDTO();
            dto.setId(pet.getId());
            dto.setName(pet.getName());
            dto.setType(pet.getType());
            dto.setBirthDate(pet.getBirthDate());
            dto.setNotes(pet.getNotes());
            if (pet.getOwner() != null) {
                dto.setOwnerId(pet.getOwner().getId());
            }
            result.add(dto);
        }

        return result;
    }
}
