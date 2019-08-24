package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.ProductClient;
import com.company.adminapiservice.util.messages.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductClient productClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpProductClientMock();
        
    }

    // tests addProduct()
    @Test
    public void addProduct() {
        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product= productService.addProduct(product);

        Product product1 = productService.getProduct(product.getProductId());

        assertEquals(product, product1);
    }

    // tests getProduct()
    @Test
    public void getProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product1 = productService.getProduct(product.getProductId());

        assertEquals(product, product1);
    }

    // tests getAllProducts()
    @Test
    public void findAllProducts() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productService.addProduct(product);

        product = new Product();
        product.setProductName("The Legend of Zelda: Link's Awakening");
        product.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));


        productService.addProduct(product);

        List<Product> fromService = productService.getAllProducts();

        assertEquals(2, fromService.size());

    }

    // tests deleteProduct()
    @Test
    public void deleteProduct() {
        Product product = productService.getProduct(1);
        productService.deleteProduct(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(productClient).deleteProduct(postCaptor.capture());
        assertEquals(product.getProductId(), postCaptor.getValue().intValue());
    }

    // tests updateProduct()
    @Test
    public void updateProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(30.00).setScale(2, RoundingMode.HALF_UP));


        productService.updateProduct(product.getProductId(), product);
        ArgumentCaptor<Product> postCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productClient).updateProduct(any(Integer.class), postCaptor.capture());
        assertEquals(product.getUnitCost(), postCaptor.getValue().getUnitCost());

    }

    // tests if will return null if try to get product with non-existent id
    @Test
    public void getProductWithNonExistentId() {
        Product product = productService.getProduct(500);
        assertNull(product);
    }

    // tests default constructor for test coverage
    // so developers know something went wrong if less than 100%
    @Test
    public void createADefaultProduct() {

        Object productObj = new ProductService();

        assertEquals(true , productObj instanceof ProductService);

    }

    // create mocks

    public void setUpProductClientMock() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(1);
        product2.setProductName("Mario Kart 8 Deluxe");
        product2.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product2.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product3 = new Product();
        product3.setProductName("The Legend of Zelda: Link's Awakening");
        product3.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product3.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product3.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        Product product4 = new Product();
        product4.setProductId(2);
        product4.setProductName("The Legend of Zelda: Link's Awakening");
        product4.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product4.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product4.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        doReturn(product2).when(productClient).addProduct(product);
        doReturn(product4).when(productClient).addProduct(product3);
        doReturn(product2).when(productClient).getProduct(1);
        doReturn(product4).when(productClient).getProduct(2);

        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product4);

        doReturn(productList).when(productClient).getAllProducts();

    }

}

