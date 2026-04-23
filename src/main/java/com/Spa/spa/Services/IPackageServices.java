package com.Spa.spa.Services;

import java.util.List;

import com.Spa.spa.models.Package;

public interface IPackageServices {
    public Package addPackage(Package spaPackage);
    public Package updatePackage(String id,Package spaPackage);
    public String deletePackage(String id);
    public Package getPackageById(String id);
    public List<Package> getAllPackages();

}
