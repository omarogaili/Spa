package com.Spa.spa.Services;

import com.Spa.spa.models.Package;

public interface IPackageServices {
    public String addPackage(Package spaPackage);
    public String updatePackage(Package spaPackage);
    public String deletePackage(String id);
    public Package getPackageById(String id);
    public Iterable<Package> getAllPackages();

}
