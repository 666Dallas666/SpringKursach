package serv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serv.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
