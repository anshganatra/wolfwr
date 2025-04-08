package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.ProductDAO;
import com.csc540.wolfwr.dto.ProductDTO;
import com.csc540.wolfwr.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductDTO createProduct(ProductDTO productDTO){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productDAO.save(product);
        // TODO return the actual created product with product ID instead of the same object
        return productDTO;
    }

    public ProductDTO getProductById(Integer productId){
        Product product = productDAO.getProductById(productId);
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        return products.stream().map(product -> {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(product, productDTO);
            return productDTO;
        }).collect(Collectors.toList());
    }

    public int deleteProduct(Integer productId) {
        return productDAO.delete(productId);
    }

}
