package com.dimlo.repository;

import com.dimlo.model.User;
import org.springframework.data.repository.CrudRepository;

//import User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

}