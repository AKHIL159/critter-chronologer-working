package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet savePet(Pet pet, Long ownerId) {
        Customer owner = customerRepository.findById(ownerId).orElse(null);
        pet.setOwner(owner);
        Pet saved = petRepository.save(pet);

        if (owner != null) {
            List<Pet> pets = owner.getPets();
            if (pets == null) {
                pets = new java.util.ArrayList<>();
            }
            if (!pets.contains(saved)) {
                pets.add(saved);
            }
            owner.setPets(pets);
            customerRepository.save(owner);
        }

        return saved;
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findAllByOwnerId(ownerId);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
