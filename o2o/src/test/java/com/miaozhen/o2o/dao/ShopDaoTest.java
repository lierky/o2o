package com.miaozhen.o2o.dao;

import com.miaozhen.o2o.BaseTest;
import com.miaozhen.o2o.entity.Area;
import com.miaozhen.o2o.entity.PersonInfo;
import com.miaozhen.o2o.entity.Shop;
import com.miaozhen.o2o.entity.ShopCategory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
import java.util.Date;
import java.util.List;

public class ShopDaoTest extends BaseTest {
    Logger logger = LoggerFactory.getLogger(ShopDaoTest.class);
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> list = shopDao.queryShopList(shopCondition,0,100);
    }

    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(12L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小：" + shopList.size());
        System.out.println("店铺总数：" + count);
    }

    @Test
    public void testInsertShop(){
        logger.info("测试一下输出日志");
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
        shop.setShopName("lier的花店");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("123456");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        //获取影响的行数
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
        logger.info("你真棒！成功了！");
    }
}
