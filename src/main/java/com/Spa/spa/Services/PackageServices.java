package com.Spa.spa.Services;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
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
        try{

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
        } catch (Exception e) {
            return "Error adding package: " + e.getMessage();
        }
    }

    @Override
    public String deletePackage(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Package existingPackage = mongoOperations.findOne(query, Package.class);
        if(existingPackage == null){
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
    public Iterable<Package> getAllPackages() {
        return mongoOperations.findAll(Package.class);
    }
}
