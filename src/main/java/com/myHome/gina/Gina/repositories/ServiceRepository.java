package com.myHome.gina.Gina.repositories;

import com.myHome.gina.Gina.models.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<Service, String> {
    Optional<Service> findByEmail(String string);
}