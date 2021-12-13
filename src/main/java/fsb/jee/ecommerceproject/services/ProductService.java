package fsb.jee.ecommerceproject.services;

import fsb.jee.ecommerceproject.entities.Product;
import fsb.jee.ecommerceproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void saveProductToDB(MultipartFile file, String name, String description, Double price){
        Product product = new Product();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")){
            System.out.println("not a valid file");
        }
        try{
            product.setPicture(Base64.getEncoder().encodeToString(file.getBytes()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepository.save(product);
    }
}
