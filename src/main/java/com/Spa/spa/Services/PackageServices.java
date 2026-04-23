package com.Spa.spa.Services;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;

import com.Spa.spa.models.Package;

@Service
public class PackageServices implements IPackageServices {
    private MongoOperations mongoOperations;

    public PackageServices(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Package addPackage(Package spaPackage) {
        try {
            if (spaPackage == null) {
                return spaPackage;
            }
            mongoOperations.save(spaPackage);
            return spaPackage;
        } catch (Exception e) {
            throw new RuntimeException("Error adding package: " + e.getMessage(), e);
        }
    }

    @Override
    public Package updatePackage(String id ,Package spaPackage) {
        try {
            Package existingPackage = mongoOperations.findById(id, Package.class);
            
            if (existingPackage == null) {
                return null;
            }
            
            if(spaPackage.getName() !=null){
                existingPackage.setName(spaPackage.getName());
            }
            if(spaPackage.getDescription() != null){
                existingPackage.setDescription(spaPackage.getDescription());
            }
            if(spaPackage.getPrice() > 0){
                existingPackage.setPrice(spaPackage.getPrice());

            }
            if(spaPackage.getDiscountPercentage() > 0){
                existingPackage.setDiscountPercentage(spaPackage.getDiscountPercentage());
            }
            
            mongoOperations.save(existingPackage);
            return existingPackage;
        
        } catch (Exception e) {
            throw new RuntimeException("Error adding package: " + e.getMessage(), e);
        }
    }

    @Override
    public String deletePackage(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Package existingPackage = mongoOperations.findOne(query, Package.class);
        if (existingPackage == null) {
            return "Package not found";
        }
        String packageName = existingPackage.getName();
        mongoOperations.remove(query, Package.class);
        return packageName + " has been deleted successfully";
    }

    @Override
    public Package getPackageById(String id) {
        return mongoOperations.findById(id, Package.class);
    }

    @Override
    public List<Package> getAllPackages() {
        return mongoOperations.findAll(Package.class);
    }
}
