package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.Spa.spa.Services.PackageServices;
import com.Spa.spa.models.Package;


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
        Package result = packageServices.addPackage(spaPackage);

        assertNotNull(result);
        assertEquals(spaPackage, result);
    }

    @Test
    public void addPackage_Should_Handle_Null_Package() {
        Package spaPackage = null;
        Package result = packageServices.addPackage(spaPackage);
        assertNull(result);
        assertEquals(null, result);
    }

    @Test
    public void updatePackage_Should_Return_SuccessMessage() {
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(spaPackage.getId(), Package.class)).thenReturn(spaPackage);
        when(mongoOperations.save(spaPackage)).thenReturn(spaPackage);
        Package updatedPackage = new Package("1234","testPackage", "feeling Good", 250);
        Package result = packageServices.updatePackage(spaPackage.getId() , updatedPackage);

        assertNotNull(result);
        assertEquals(updatedPackage, result);
    }

    @Test
    public void updatePackage_Should_Handle_Package_Not_Found() {
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(spaPackage.getId(), Package.class)).thenReturn(null);
        Package result = packageServices.updatePackage(spaPackage.getId(),spaPackage);

        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void updatePackage_Should_Handle_Exception() {
        Package spaPackage = new Package("1234","Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(spaPackage.getId(), Package.class)).thenReturn(spaPackage);
        when(mongoOperations.save(spaPackage)).thenThrow(new RuntimeException("Database error"));
        Package result = packageServices.updatePackage(spaPackage.getId(),spaPackage);
        assertNotNull(result);
        assertEquals("Error adding package: Database error", result);
    }

    @Test
    public void deletePackage_Should_Return_SuccessMessage(){
        String id = "1234";
        Package spaPackage = new Package(id,"Relaxation Package", "feeling Good", 200);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        when(mongoOperations.findOne(query, Package.class)).thenReturn(spaPackage);
        String result = packageServices.deletePackage(id);

        assertNotNull(result);
        assertEquals(spaPackage.getName() + " has been deleted successfully", result);
    }

    @Test
    public void deletePackage_Should_Handle_Package_Not_Found(){
        String id = "888";
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        when(mongoOperations.findOne(query, Package.class)).thenReturn(null);
        String result = packageServices.deletePackage(id);

        assertNotNull(result);
        assertEquals("Package not found", result); 
    }

    @Test
    public void getPackageById_Should_Return_Package(){
        String id = "1234";
        Package spaPackage = new Package(id, "Relaxation Package", "feeling Good", 200);
        when(mongoOperations.findById(id, Package.class)).thenReturn(spaPackage);
        Package result = packageServices.getPackageById(id);
        assertNotNull(result);
        assertEquals(spaPackage.getId(), result.getId());
        assertEquals(spaPackage.getName(), result.getName());
        assertEquals(spaPackage.getDescription(), result.getDescription());
    }

    @Test
    public void getPackageById_Should_Handle_Package_Not_Found(){
        String id = "8888";
        when(mongoOperations.findById(id, Package.class)).thenReturn(null);
        Package result = packageServices.getPackageById(id);
        assertNull(result);
        assertEquals(null, result);
    }

    @Test
    public void getAllPackages_Should_Return_ListOfPackages(){
        Package spaPackage1 = new Package("1234", "relaxtion Package", "feeling Good", 200);
        Package spaPackage2 = new Package("5678", "Luxury Package", "feeling Great", 300);
        when(mongoOperations.findAll(Package.class)).thenReturn(java.util.Arrays.asList(spaPackage1, spaPackage2));
        Iterable<Package> result = packageServices.getAllPackages();
        assertNotNull(result);
        List<Package> packageList = new ArrayList<>();
        result.forEach(packageList::add);
        assertEquals(2, packageList.size());
    }
}
