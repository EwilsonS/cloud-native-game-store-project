package com.evanco.levelupservice.dao;

import com.evanco.levelupservice.model.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelUpDaoJdcbTemplateImplTest {

    @Autowired
    private LevelUpDao levelUpDao;

    @Before
    public void setUp() throws Exception{
        levelUpDao.getAllLevelUps()
                .stream()
                .forEach(l->levelUpDao.deleteLevelUp(l.getLevelUpId()));
    }

    @Test
    public void addGetDeleteLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));

        // add
        levelUp = levelUpDao.addLevelUp(levelUp);

        // get
        LevelUp levelUp1 = levelUpDao.getLevelUp(levelUp.getLevelUpId());
        assertEquals(levelUp, levelUp1);

        //delete
        levelUpDao.deleteLevelUp(levelUp.getLevelUpId());
        assertEquals(0, levelUpDao.getAllLevelUps().size());
    }

    @Test
    public void updateLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));
        levelUp = levelUpDao.addLevelUp(levelUp);

        levelUp.setPoints(500);
        levelUpDao.updateLevelUp(levelUp);

        assertEquals(500, levelUpDao.getLevelUp(levelUp.getLevelUpId()).getPoints());
    }

    @Test
    public void getAllLevelUps() {
    }

    @Test
    public void getLevelUpPointsByCustomerId() {
    }
}
/*

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoJdbcTemplateImplTest {

    @Autowired
    ProductDao productDao;

    // clear product table in database
    @Before
    public void setUp() throws Exception {
        List<Product> products = productDao.getAllProducts();
        for (Product p : products) {
            productDao.deleteProduct(p.getProductId());
        }
    }

    // tests addProduct(), getProduct() and deleteProduct()
    @Test
    public void addGetDeleteProduct() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product = productDao.addProduct(product);

        Product product1 = productDao.getProduct(product.getProductId());
        assertEquals(product, product1);

        productDao.deleteProduct(product.getProductId());
        product1 = productDao.getProduct(product.getProductId());
        assertNull(product1);
    }

    // tests if will return null if try to get product with non-existent id
    @Test
    public void getProductWithNonExistentId() {
        Product product = productDao.getProduct(500);
        assertNull(product);
    }

    // tests if will throw exception if id provided does not exist when trying to delete product
    @Test(expected  = NotFoundException.class)
    public void deleteProductWithNonExistentId() {

        productDao.deleteProduct(500);

    }

    // tests updateProduct()
    @Test
    public void updateProduct() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product = productDao.addProduct(product);

        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));

        productDao.updateProduct(product);

        Product product1 = productDao.getProduct(product.getProductId());
        assertEquals(product, product1);
    }

    // tests if will throw exception if id provided does not exist when trying to update product
    @Test(expected  = IllegalArgumentException.class)
    public void updateProductWithIllegalArgumentException() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productDao.updateProduct(product);

    }

    // tests getAllProducts()
    @Test
    public void getAllProducts() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productDao.addProduct(product);

        product = new Product();
        product.setProductName("The Legend of Zelda: Link's Awakening");
        product.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        productDao.addProduct(product);

        List<Product> pList = productDao.getAllProducts();
        assertEquals(2, pList.size());
    }

}

 */