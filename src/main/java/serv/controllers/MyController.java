package serv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import serv.models.*;
import serv.sevices.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class MyController {


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private DepositService depositService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model, Principal principal) {
        model.addAttribute("deposits", depositService.getDeposits());
        model.addAttribute("deposit", new Deposit());
        model.addAttribute("purchases", purchaseService.getPurchases());
        model.addAttribute("purchase", new Purchase());
        model.addAttribute("user", principal.getName());
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin() {
        return "admin";
    }

    @RequestMapping(value = "/admin/registration", method = RequestMethod.GET)
    public String registration(User user) {
        return "registration";
    }

    @RequestMapping(value = "/admin/registration", method = RequestMethod.POST)
    String signUp(@ModelAttribute User user) {
        userService.signUpUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/isusername", method = RequestMethod.GET)
    public ResponseEntity<Boolean> isUsernameExist(@RequestParam String username) {
        try {
            userService.loadUserByUsername(username);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }catch (UsernameNotFoundException e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admin/deleteuser", method = RequestMethod.GET)
    public String windowDeleteUser(Model model) {
        model.addAttribute("users", userService.getUsers().listIterator(1));
        return "deleteuser";
    }

    @RequestMapping(value = "/admin/deleteuser", method = RequestMethod.POST)
    public Object deleteUser(@RequestParam(value = "user", required = true) int[] id) {
        for (int i : id) {
            userService.deleteUser(i);

        }
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String windowUsers(Model model) {
        model.addAttribute("users", userService.getUsers().listIterator(1));
        return "users";
    }

    @RequestMapping(value = "/admin/users/{userId}/deposits", method = RequestMethod.GET)
    public String windowUsers(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("deposits", depositService.getDepositsByUser(userId).listIterator(0));
        return "deposits";
    }

    @RequestMapping(value = "/admin/users/{userId}/deposits", method = RequestMethod.POST)
    public Object sellDeposit(@RequestParam(value = "deposit", required = true) int[] id) {
        for (int i : id) {
            productService.createProduct(depositService.getDepositById(i));

        }
        for (int i : id) {
            depositService.deleteDeposit(i);

        }
        return "redirect:/admin";
    }

    @RequestMapping(value = "/createdeposit", method = RequestMethod.POST)
    public String createDeposit(@RequestParam(value = "name", required = true) String depositName,
                              @RequestParam(value = "price", required = true) int depositPrice,
                                Authentication authentication) {
        Deposit a = new Deposit();
        a.setName(depositName);
        a.setPrice(depositPrice);
        a.setUserId(userService.getIdbyName(authentication.getName()));
        depositService.createDeposit(a);
        return "redirect:/";
    }

    @RequestMapping(value = "/userdeposits", method = RequestMethod.GET)
    public String windowUsers(Model model, Authentication authentication) {
        model.addAttribute("deposits", depositService.getDepositsByUser(userService.getIdbyName(authentication.getName())).listIterator(0));
        return "userdeposits";
    }

    @RequestMapping(value = "/userdeposits", method = RequestMethod.POST)
    public Object clearDeposit(@RequestParam(value = "deposit", required = true) int[] id) {
        for (int i : id) {
            depositService.deleteDeposit(i);

        }
        return "userdeposits";
    }

    @RequestMapping(value = "/market", method = RequestMethod.GET)
    public String windowProducts(Model model, Authentication authentication) {
        model.addAttribute("products", productService.getProducts().listIterator(0));
        return "products";
    }

    @RequestMapping(value = "/market", method = RequestMethod.POST)
    public Object buyProduct(@RequestParam(value = "product", required = true) Product product,
                             Authentication authentication) {
        Purchase a = new Purchase();
        a.setName(product.getName());
        a.setPrice(product.getPrice());
        a.setUserId(userService.getIdbyName(authentication.getName()));
        purchaseService.createPurchase(a);
        productService.deleteProduct(product.getId());

        return "redirect:/";
    }

    @RequestMapping(value = "/userpurchases", method = RequestMethod.GET)
    public String windowPurchases(Model model, Authentication authentication) {
       model.addAttribute("purchases", purchaseService.getPurchasesByUser(userService.getIdbyName(authentication.getName())).listIterator(0));
        return "purchases";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    public String windowChangePassword() {
        return "password";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam(value = "oldPassword", required = true) String oldPassword,
                                 @RequestParam(value = "newPassword", required = true) String newPassword,
                                 Principal principal,
                                 Model model) throws Exception {
        try {
            userService.changePassword(principal.getName(), oldPassword, newPassword);
            return "redirect:/";
        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());

            return "password";

        }

    }

}