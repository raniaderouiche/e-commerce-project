package fsb.jee.ecommerceproject.controllers;

import fsb.jee.ecommerceproject.entities.User;
import fsb.jee.ecommerceproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    Boolean invalidCredentials;

    @GetMapping({"/","/login","/welcome"})
    public ModelAndView loadWelcomePage(){
        ModelAndView mav = new ModelAndView("index");
        User user = new User();
        mav.addObject("user", user);
        invalidCredentials = false;
        mav.addObject("invalidCredentials", invalidCredentials);
        return mav;
    }

    @PostMapping("/checkLogin")
    public Object login(@ModelAttribute User user){
        List<User> users = userRepository.findAll();
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        users.add(admin);
        for (User u : users){
            if(Objects.equals(u.getUsername(), user.getUsername()) && Objects.equals(u.getPassword(), user.getPassword())){
                return "redirect:products";
            }
        }
        ModelAndView mav = new ModelAndView("index");
        invalidCredentials = true;
        mav.addObject("invalidCredentials", invalidCredentials);
        return mav;
    }
}
