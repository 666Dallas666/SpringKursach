package serv.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serv.models.Deposit;
import serv.models.Product;
import serv.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository reps;

    public List<Product> getProducts() {
        return reps.findAll();
    }

    public void createProduct(Deposit a) {
        Product b = new Product();
        b.setPrice(a.getPrice());
        b.setName(a.getName());
        reps.save(b);
    }

    public Product getProductById(int productId) {
        return reps.findById(productId).get();
    }

    public void deleteProduct(int productId) {
        if (reps.findById(productId).isPresent()) {
            reps.deleteById(productId);
        }
    }
}
