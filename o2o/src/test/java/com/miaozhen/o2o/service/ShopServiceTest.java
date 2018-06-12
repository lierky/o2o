package com.miaozhen.o2o.service;

import com.miaozhen.o2o.BaseTest;
import com.miaozhen.o2o.dto.ShopExecution;
import com.miaozhen.o2o.entity.Area;
import com.miaozhen.o2o.entity.PersonInfo;
import com.miaozhen.o2o.entity.Shop;
import com.miaozhen.o2o.entity.ShopCategory;
import com.miaozhen.o2o.enums.ShopStateEnum;
import com.miaozhen.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest{
    @Autowired
    private ShopService shopService;
    @Test
    public void teatAddShop() throws ShopOperationException,FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("lier的花店2");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("1234561");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("C:/liuhongli/test.png");
        ShopExecution se = shopService.addShop(shop,new FileInputStream(shopImg),shopImg.getName());
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }
}
