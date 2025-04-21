package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public PetDTO savePet(PetDTO dto) {
        Pet pet = new Pet();
        pet.setType(dto.getType());
        pet.setName(dto.getName());
        pet.setBirthDate(dto.getBirthDate());
        pet.setNotes(dto.getNotes());

        Customer owner = customerRepository.findById(dto.getOwnerId()).orElse(null);
        pet.setOwner(owner);

        Pet saved = petRepository.save(pet);

        // Update owner's pets list
        if (owner != null) {
            if (owner.getPets() == null) {
                owner.setPets(new ArrayList<>());
            }
            owner.getPets().add(saved);
            customerRepository.save(owner);
        }

        PetDTO result = new PetDTO();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setType(saved.getType());
        result.setBirthDate(saved.getBirthDate());
        result.setNotes(saved.getNotes());
        result.setOwnerId(owner != null ? owner.getId() : null);

        return result;
    }

    public List<PetDTO> getPetsByOwner(Long ownerId) {
        List<Pet> pets = petRepository.findAllByOwnerId(ownerId);
        return pets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PetDTO convertToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());
        dto.setOwnerId(pet.getOwner() != null ? pet.getOwner().getId() : null);
        return dto;
    }

    public PetDTO getPetById(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) return null;

        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());
        dto.setOwnerId(pet.getOwner() != null ? pet.getOwner().getId() : null);

        return dto;
    }


}
