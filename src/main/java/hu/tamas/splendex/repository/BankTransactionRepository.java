package hu.tamas.splendex.repository;

import hu.tamas.splendex.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findAllByFromPersonIdOrToPersonIdOrderByTransactionDate(Long fromPersonId, Long toPersonId);

}
