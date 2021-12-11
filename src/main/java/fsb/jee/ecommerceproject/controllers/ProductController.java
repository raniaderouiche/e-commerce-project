package fsb.jee.ecommerceproject.controllers;

import fsb.jee.ecommerceproject.entities.Product;
import fsb.jee.ecommerceproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

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

}
