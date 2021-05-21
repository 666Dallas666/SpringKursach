package serv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serv.models.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}
