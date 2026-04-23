package com.Spa.spa.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.Spa.spa.models.Package;

import com.Spa.spa.Services.PackageServices;

@RestController
public class PackageEndpoint {
    @Autowired
    private PackageServices packageServices;

    @GetMapping("/api/packages")
    public ResponseEntity<Iterable<Package>> getAllPackage(){
        List<Package> packages = packageServices.getAllPackages();
        
        if(packages.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/api/package/{packageId}")
    public ResponseEntity<Package> getPackageById (@PathVariable String packageId){
        Package searchedPackage = packageServices.getPackageById(packageId);
        if(searchedPackage == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searchedPackage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/manage-packages")
    public ResponseEntity<Package> addNewPackage(@RequestBody Package spapackage){
        Package response = packageServices.addPackage(spapackage);
        if("Package not found".equals(response)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/admin/manage-packages-deleted/{id}")
    public ResponseEntity<String> deletePackage(@PathVariable String id){
        String response = packageServices.deletePackage(id);
        if(response.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/manage-packages-update/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable String id,@RequestBody Package spaPackage){
        Package response = packageServices.updatePackage(id, spaPackage);
        if(response == null){
            return ResponseEntity.noContent().build();
        }
        URI location = URI.create("/api/package/" + response.getId());

        return ResponseEntity.created(location).body(response);
    }

}
