package fsb.jee.ecommerceproject.controllers;

import fsb.jee.ecommerceproject.entities.Product;
import fsb.jee.ecommerceproject.entities.User;
import fsb.jee.ecommerceproject.repositories.ProductRepository;
import fsb.jee.ecommerceproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    private User loggedInUser;

    @GetMapping("/loadProducts")
    public ModelAndView initProducts(@ModelAttribute User user){
        ModelAndView mav = new ModelAndView("products-list");
        System.out.println(user);
        loggedInUser = userRepository.findById(user.getId()).get();
        List<Product> productList = productRepository.findAll();
        mav.addObject("products", productList);
        //mav.addObject("loggedInUser", loggedInUser);
        return mav;
    }

    @GetMapping("/products")
    public ModelAndView getProducts(){
        ModelAndView mav = new ModelAndView("products-list");
        List<Product> productList = productRepository.findAll();
        mav.addObject("products", productList);
        return mav;
    }

    @GetMapping("/addProduct")
    public ModelAndView addProductForm(){
        ModelAndView mav = new ModelAndView("add-product");
        Product product = new Product();
        mav.addObject("product", product);
        return mav;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product){
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/UpdateProductForm")
    public ModelAndView UpdateProductForm(@RequestParam Long productId){
        ModelAndView mav = new ModelAndView("add-product");
        Product product = productRepository.findById(productId).get();
        mav.addObject("product", product);
        return mav;
    }

    @GetMapping("/DeleteProduct")
    public String deleteProduct(@RequestParam Long productId) {
        productRepository.deleteById(productId);
        return "redirect:/products";
    }

    @GetMapping("/AddProductToCart")
    public String addToCart(@RequestParam Long productId){
        Product product = productRepository.findById(productId).get();
        System.out.println(loggedInUser);
        List<Product> cart = loggedInUser.getCart();
        Boolean found = false;
        for(Product p : cart){
            if (Objects.equals(p.getId(), productId)) {
                found = true;
                break;
            }
        }
        if(!found){
            cart.add(product);
            loggedInUser.setCart(cart);
            userRepository.save(loggedInUser);
            System.out.println(loggedInUser);
        }
        return "redirect:/products";
    }
/*----------------------------------------------cart---------------------------------------------*/
    //load cart
    @GetMapping("/cart")
    public ModelAndView getCart(){
        ModelAndView mav = new ModelAndView("cart-list");
        List<Product> productList = loggedInUser.getCart();
        Double total = 0.0;
        for (Product p : productList) {
            total = total + p.getPrice();
        }
        mav.addObject("products", productList);
        mav.addObject("total", total);
        return mav;
    }

    //remove item from cart
    @GetMapping("/RemoveProductFromCart")
    public String removeProduct(@RequestParam Long productId) {
        Product product = productRepository.findById(productId).get();
        List<Product> cart = loggedInUser.getCart();
        Boolean found = false;
        for(Product p : cart){
            if (Objects.equals(p.getId(), productId)) {
                found = true;
                break;
            }
        }
        if(found){
            cart.remove(product);
            loggedInUser.setCart(cart);
            userRepository.save(loggedInUser);
            System.out.println(loggedInUser);
        }
        return "redirect:/cart";
    }

}
