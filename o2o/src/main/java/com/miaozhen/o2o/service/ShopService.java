package com.miaozhen.o2o.service;

import com.miaozhen.o2o.dto.ShopExecution;
import com.miaozhen.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    ShopExecution addShop(Shop shop, File shopImg);
}
