package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Spa.spa.Services.PackageServices;
import com.Spa.spa.models.Package;

@SpringBootTest
public class PackageServicesTest {
    private PackageServices packageServices;

    @Test
    public void addPackage_Should_Return_SuccessMessage() {
        Package spaPackage = new Package("Relaxation Package", "feeling Good", 200);
        String result = packageServices.addPackage(spaPackage);
        assertNotNull(result);
        assert result.equals("Package added successfully");
    }
}
