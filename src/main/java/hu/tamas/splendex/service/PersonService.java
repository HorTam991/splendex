package hu.tamas.splendex.service;

import hu.tamas.splendex.model.Person;
import hu.tamas.splendex.model.core.ListResponse;

import java.util.Map;

public interface PersonService {

    Person save(Person Person);

    Person update(Person Person);

    ListResponse findAll(Map<String, String> filterParams, int pageIndex, int pageSize);

    Person findById(Long id);

    void deleteById(Long id);
    
}
