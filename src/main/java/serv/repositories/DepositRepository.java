package serv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serv.models.Deposit;
import serv.models.User;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer>{
    @Query("select u from User u  where u.id=:id")
    User getUser(int id);
}
