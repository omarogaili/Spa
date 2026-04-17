package com.Spa.spa.Services;

import org.springframework.data.mongodb.core.MongoOperations;

import com.Spa.spa.models.Package;

public class PackageServices implements IPackageServices {
    private MongoOperations mongoOperations;

    public PackageServices(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public String addPackage(Package spaPackage) {
        try {
            if (spaPackage == null) {
                return "Package cannot be null";
            }
            mongoOperations.save(spaPackage);
            return "Package added successfully: " + spaPackage.getName();
        } catch (Exception e) {
            return "Error adding package: " + e.getMessage();
        }
    }

    @Override
    public String updatePackage(Package spaPackage) {
        Package existingPackage = mongoOperations.findById(spaPackage.getId(), Package.class);
        if(existingPackage == null){
            return "Package not found";
        }
        existingPackage.setName(spaPackage.getName());
        existingPackage.setDescription(spaPackage.getDescription());
        existingPackage.setPrice(spaPackage.getPrice());
        existingPackage.setDiscountPercentage(spaPackage.getDiscountPercentage());
        mongoOperations.save(existingPackage);
        return  existingPackage.getName() +" Package updated successfully";
    }

    @Override
    public String deletePackage(String id) {
        return null;
    }

    @Override
    public Package getPackageById(String id) {
        return null;
    }

    @Override
    public Iterable<Package> getAllPackages() {
        return null;
    }
}
