package edu.pw.serverexample.repositories;

import edu.pw.serverexample.entities.Form;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends CrudRepository<Form, Long> {
}
