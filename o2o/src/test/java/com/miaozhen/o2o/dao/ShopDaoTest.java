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

public class ShopDaoTest extends BaseTest {
    Logger logger = LoggerFactory.getLogger(ShopDaoTest.class);
    @Autowired
    private ShopDao shopDao;
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
