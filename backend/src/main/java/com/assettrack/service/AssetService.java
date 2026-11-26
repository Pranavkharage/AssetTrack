package com.assettrack.service;
import com.assettrack.entity.*;import com.assettrack.dto.Requests;import com.assettrack.repository.*;import com.assettrack.exception.ApiException;import org.springframework.stereotype.Service;import org.springframework.http.HttpStatus;import java.util.*;
@Service
public class AssetService {
 private final AssetRepository assets;private final CategoryRepository cats;private final AssignmentRepository assignments;
 public AssetService(AssetRepository a,CategoryRepository c,AssignmentRepository as){assets=a;cats=c;assignments=as;}
 public List<Asset> all(){return assets.findAll();}
 public Asset save(Requests.AssetRequest r,Long id){
  Asset a=id==null?new Asset():assets.findById(id).orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));
  if(id==null&&assets.findByAssetCode(r.assetCode()).isPresent())throw new ApiException("Asset code already exists",HttpStatus.CONFLICT);
  a.setAssetCode(r.assetCode());a.setName(r.name());a.setCategory(cats.findById(r.categoryId()).orElseThrow(()->new ApiException("Category not found",HttpStatus.NOT_FOUND)));
  a.setBrand(r.brand());a.setModel(r.model());a.setSerialNumber(r.serialNumber());a.setPurchaseDate(r.purchaseDate());a.setPurchasePrice(r.purchasePrice());a.setVendor(r.vendor());return assets.save(a);
 }
 public Asset retire(Long id){Asset a=assets.findById(id).orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));if(assignments.existsByAssetIdAndReturnDateIsNull(id))throw new ApiException("Return the asset before retiring it",HttpStatus.CONFLICT);a.setStatus(AssetStatus.RETIRED);return assets.save(a);}
}
