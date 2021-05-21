package serv.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serv.models.Deposit;
import serv.models.User;
import serv.repositories.DepositRepository;
import serv.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepositService {
    @Autowired
    private DepositRepository reps;

    @Autowired
    private UserRepository usr;

    public List<Deposit> getDeposits() {
        return reps.findAll();
    }

    public void createDeposit(Deposit a) {
       reps.save(a);
    }

    public Deposit getDepositById(int depositId) {
        return reps.findById(depositId).get();
    }

    public List<Deposit> getDepositsByUser(int userId){
        List<Deposit> a = reps.findAll();
        List<Deposit> c = new ArrayList<Deposit>();
        for (int i = 0; i < a.size(); i++){
            if(a.get(i).getUserId() == userId){
                c.add(a.get(i));
            }
        }
        return c;
    }

    public void deleteDeposit(int depositId) {
        if (reps.findById(depositId).isPresent()) {
            reps.deleteById(depositId);
        }
    }
}
