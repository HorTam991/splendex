package hu.tamas.splendex.repository;

import hu.tamas.splendex.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
}
