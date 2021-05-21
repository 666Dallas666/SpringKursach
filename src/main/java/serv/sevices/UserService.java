package serv.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serv.models.*;
import serv.repositories.DepositRepository;
import serv.repositories.ProductRepository;
import serv.repositories.PurchaseRepository;
import serv.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository reps;

    @Autowired
    private DepositRepository deposit;

    @Autowired
    private ProductService product;

    @Autowired
    private PurchaseRepository purchase;

    @Autowired
    UserService(UserRepository reps, BCryptPasswordEncoder encoder) {
        this.reps = reps;
        this.encoder = encoder;
        User admin = new User();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin"));
        Set<Role> set = new HashSet<Role>();
        set.add(new Role(1, "ROLE_USER"));
        set.add(new Role(2, "ROLE_ADMIN"));
        admin.setRoles(set);
        User user = reps.findByUsername(admin.getUsername());
        if (user == null) {
            reps.save(admin);
        }

    }

    public void signUpUser(User user) {

        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(encoder.encode(user.getPassword()));
        reps.save(user);

    }

    public void deleteUser(int userId) {
        if (reps.findById(userId).isPresent()) {
            reps.deleteById(userId);
        }
        List<Deposit> a = deposit.findAll();
        List<Purchase> b = purchase.findAll();
        for (int i = 0; i < a.size(); i++){
            if(a.get(i).getUserId() == userId){
                product.createProduct(a.get(i));
                deposit.deleteById(a.get(i).getId());
            }
        }
        for (int i = 0; i < b.size(); i++){
            if(b.get(i).getUserId() == userId){
                purchase.deleteById(b.get(i).getId());
            }
        }

    }

    public List<User> getUsers() {
        return reps.findAll();
    }

    public User getUserById(int userId) {
        return reps.findById(userId).get();
    }

    public User getUserByName(String username) {
        return reps.findByUsername(username);
    }

    public int getIdbyName(String username) {
        User a = reps.findByUsername(username);
        return a.getId();
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
        User a = reps.findByUsername(username);
        if (encoder.matches(oldPassword, a.getPassword())){
            if (encoder.matches(newPassword, a.getPassword())){
                throw new Exception("Пароль не должен совпадать с предыдущим");
            }
            a.setPassword(encoder.encode(newPassword));
            reps.changePassword(a.getPassword(), a.getId());
            return;
        }
        throw new Exception("Неверный пароль");

    }

    public List<Deposit> getDeposits(int userId) {
        return reps.getDeposits(userId);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = reps.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}