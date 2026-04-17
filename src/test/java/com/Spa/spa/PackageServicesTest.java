package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;

import com.Spa.spa.Services.PackageServices;
import com.Spa.spa.models.Package;

@SpringBootTest
public class PackageServicesTest {
    private PackageServices packageServices;
    private MongoOperations mongoOperations;

    @BeforeEach
    public void setUp() {
        mongoOperations = org.mockito.Mockito.mock(MongoOperations.class);
        packageServices = new PackageServices(mongoOperations);
    }

    @Test
    public void addPackage_Should_Return_SuccessMessage() {
        Package spaPackage = new Package("12445","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.save(spaPackage)).thenReturn(spaPackage);
        when(mongoOperations.findById(spaPackage.getId(), Package.class)).thenReturn(spaPackage);
        System.out.println(" *********** Package ID: " + spaPackage.getName() + " ***********");

        String result = packageServices.addPackage(spaPackage);
        assertNotNull(result);
        assertEquals("Package added successfully: " + spaPackage.getName(), result);
    }

    @Test
    public void addPackage_Should_Handle_Null_Package() {
        Package spaPackage = null;
        String result = packageServices.addPackage(spaPackage);
        assertNotNull(result);
        assertEquals("Package cannot be null", result);
    }

    @Test
    public void updatePackage_Should_Return_SuccessMessage() {
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(spaPackage.getId(), Package.class)).thenReturn(spaPackage);
        when(mongoOperations.save(spaPackage)).thenReturn(spaPackage);
        spaPackage.setPrice(250);
        Package updatedPackage = new Package("1234","testPackage", "feeling Good", 250);
        String result = packageServices.updatePackage(updatedPackage);
        assertNotNull(result);
        System.out.println("******** Test2 The new Name is : " + result + " ********" );
        assertEquals(updatedPackage.getName() + " Package updated successfully", result);
    }
}
