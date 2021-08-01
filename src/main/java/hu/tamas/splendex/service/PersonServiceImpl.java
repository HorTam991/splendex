package hu.tamas.splendex.service;

import hu.tamas.splendex.model.Person;
import hu.tamas.splendex.model.core.ListResponse;
import hu.tamas.splendex.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PersonRepository personRepository;

    @Override
    public Person save(Person Person) {
        return this.personRepository.save(Person);
    }

    @Override
    public Person update(Person Person) {
        return this.personRepository.save(Person);
    }

    @Override
    public ListResponse findAll(Map<String, String> filterParams, int pageIndex, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);

        Root<Person> fromPatient = query.from(Person.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filterParams.containsKey("firstName")) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fromPatient.get("firstName")), "%" + filterParams.get("firstName").toLowerCase() + "%"));
        }

        if (filterParams.containsKey("lastName")) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fromPatient.get("lastName")), "%" + filterParams.get("lastName").toLowerCase() + "%"));
        }

        query.distinct(true).where(predicates.toArray(new Predicate[0]));

        List<Person> personList;
        if (pageIndex != -1 && pageSize != -1) {
            personList = entityManager.createQuery(query).setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).getResultList();
            CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
            countCriteria.select(criteriaBuilder.count(fromPatient));
            countCriteria.from(query.getResultType());
            countCriteria.where(predicates.toArray(new Predicate[0]));
            Long count = entityManager.createQuery(countCriteria).getSingleResult();
            return new ListResponse(count, personList);
        } else {
            personList = entityManager.createQuery(query).getResultList();
            return new ListResponse((long) personList.size(), personList);
        }
    }

    @Override
    public Person findById(Long id) {
        Optional<Person> result = this.personRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        this.personRepository.deleteById(id);
    }

}
