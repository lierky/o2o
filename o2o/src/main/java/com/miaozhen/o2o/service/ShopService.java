package com.miaozhen.o2o.service;

import com.miaozhen.o2o.dto.ShopExecution;
import com.miaozhen.o2o.entity.Shop;
import com.miaozhen.o2o.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}
