package fsb.jee.ecommerceproject.controllers;

import fsb.jee.ecommerceproject.entities.Product;
import fsb.jee.ecommerceproject.entities.User;
import fsb.jee.ecommerceproject.repositories.ProductRepository;
import fsb.jee.ecommerceproject.repositories.UserRepository;
import fsb.jee.ecommerceproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    private User loggedInUser;

    @GetMapping("/loadProducts")
    public ModelAndView initProducts(@ModelAttribute User user){
        ModelAndView mav = new ModelAndView("products-list");
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
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("price") Double price){

        productService.saveProductToDB(file,name,description,price);
        return "redirect:/products";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute() Product product, @RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            product.setPicture(productRepository.findById(product.getId()).get().getPicture());
        }else{
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            if (filename.contains("..")){
                System.out.println("not a valid file");
            }
            try{
                product.setPicture(Base64.getEncoder().encodeToString(file.getBytes()));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/UpdateProductForm")
    public ModelAndView UpdateProductForm(@RequestParam Long productId){
        ModelAndView mav = new ModelAndView("update-product");
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
        }
        return "redirect:/cart";
    }

}
