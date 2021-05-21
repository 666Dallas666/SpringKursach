package serv.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serv.models.Deposit;
import serv.models.Purchase;
import serv.repositories.PurchaseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository reps;

    public List<Purchase> getPurchases() {
        return reps.findAll();
    }

    public void createPurchase(Purchase a) {
        reps.save(a);
    }

    public List<Purchase> getPurchasesByUser(int userId){
        List<Purchase> a = reps.findAll();
        List<Purchase> c = new ArrayList<Purchase>();
        for (int i = 0; i < a.size(); i++){
            if(a.get(i).getUserId() == userId){
                c.add(a.get(i));
            }
        }
        return c;
    }

    public Purchase getPurchaseById(int purchaseId) {
        return reps.findById(purchaseId).get();
    }
}