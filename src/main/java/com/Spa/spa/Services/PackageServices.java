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
        mongoOperations.save(spaPackage);
        return "Package added successfully: " + spaPackage.getName();
    }

    @Override
    public String updatePackage(Package spaPackage) {
        return null;
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
