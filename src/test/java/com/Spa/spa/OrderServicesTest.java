package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.models.Order;
import com.Spa.spa.models.Package;
import com.Spa.spa.models.PackageSnapShot;

@SpringBootTest
@ActiveProfiles("test")

public class OrderServicesTest {
    MongoOperations mongoOperations;
    OrderServices orderServices;

    @BeforeEach
    public void setUp(){
        mongoOperations = mock(MongoOperations.class);
        orderServices = new OrderServices(mongoOperations);
    }

    @Test
    public void testAddOrder() {
        String packageId = "1234";
        Package spaPackage = new Package(packageId,"Relaxation Package", "feeling Good", 500);
        when(mongoOperations.findById(packageId, Package.class)).thenReturn(spaPackage);
        PackageSnapShot packageSnapShot = new PackageSnapShot(spaPackage.getId(),spaPackage.getName(), spaPackage.getDescription(), spaPackage.getPrice(), spaPackage.getDiscountPercentage());
        LocalDate orderDate = LocalDate.now();
        Order order = new Order("packageId", "packageId", 4,  350, orderDate, "packageId", "packageId@hotmail.com", packageSnapShot ,spaPackage.getId());
        String result = orderServices.addOrder(packageId, order);
        assert result.equals("An confirmation email has been sent to " + order.getEmail());
        double expectedCalculatedPrice = orderServices.calculatePrice(packageSnapShot, order.getNumberOfPeople(), order.getStandardPrice());

        System.out.println("********** Expected Price: " + expectedCalculatedPrice + " **********");
        System.out.println("********** Total Price: " + order.getTotalPrice() + " **********");
        assertEquals(3100, order.getTotalPrice() );
    }

    @Test
    public void testAddOrder_Should_Handle_Null_Order() {
        String packageId = "12345";
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(packageId, Package.class)).thenReturn(spaPackage);
        String result = orderServices.addOrder(packageId, null);
        assertEquals("Order cannot be null", result);
    }

    @Test
    public void GetOrderById_Should_Return_SuccessMessage(){
        String orderId= "1234";
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        PackageSnapShot packageSnapShot = new PackageSnapShot(spaPackage.getId(),spaPackage.getName(), spaPackage.getDescription(), spaPackage.getPrice(), spaPackage.getDiscountPercentage());
        LocalDate orderDate = LocalDate.now();
        Order order = new Order("packageId", "packageId", 4,  350, orderDate, "packageId", "packageId@hotmail.com", packageSnapShot ,spaPackage.getId());
        when(mongoOperations.findById(orderId, Order.class)).thenReturn(order);
        Order result = orderServices.getOrderById(orderId);
        assertEquals(order, result);
    }

    @Test
    public void GetOrderById_Should_Handle_Null_Order() {
        String orderId = "12345";
        when(mongoOperations.findById(orderId, Order.class)).thenReturn(null);
        Order result = orderServices.getOrderById(orderId);
        assertNull(result);
    }

    

    @Test
    public void GetOrderById_Should_Return_Null_When_Order_Not_Found() {
        String orderId = "12345";
        when(mongoOperations.findById(orderId, Order.class)).thenReturn(null);
        Order result = orderServices.getOrderById(orderId);
        assertNull(result);
    }

    @Test
    public void GetAllOrders_Should_Return_SuccessMessage(){
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        PackageSnapShot packageSnapShot = new PackageSnapShot(spaPackage.getId(),spaPackage.getName(), spaPackage.getDescription(), spaPackage.getPrice(), spaPackage.getDiscountPercentage());
        LocalDate orderDate = LocalDate.now();
        Order order = new Order("packageId", "packageId", 4,  350, orderDate, "packageId", "packageId@hotmail.com", packageSnapShot ,spaPackage.getId());
        when(mongoOperations.findAll(Order.class)).thenReturn(java.util.Arrays.asList(order));
        Iterable<Order> result = orderServices.getAllOrders();
        assert result != null;
        assertEquals(1, ((java.util.Collection<?>) result).size());
    }
}
