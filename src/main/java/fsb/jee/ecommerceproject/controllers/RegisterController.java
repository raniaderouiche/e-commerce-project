package fsb.jee.ecommerceproject.controllers;

import fsb.jee.ecommerceproject.entities.User;
import fsb.jee.ecommerceproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public ModelAndView loadRegisterPage(){
        ModelAndView mav = new ModelAndView("register");
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/registerUser")
    public RedirectView registerNewUser(@ModelAttribute User user, RedirectAttributes redir){
        userRepository.save(user);
        RedirectView redirectView= new RedirectView("/loadProducts",true);
        redir.addFlashAttribute("user",user);
        return redirectView;
    }
}
