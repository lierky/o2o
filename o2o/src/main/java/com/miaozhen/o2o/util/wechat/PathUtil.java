package com.miaozhen.o2o.util.wechat;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "C:/liuhongli/projectdev/o2o/image/";
        }else {
            basePath = "/home/o2o/image/";
        }
        basePath = basePath.replace("/",separator);
        return basePath;
    }
    //获取商铺对应的图片文件夹
    public static String getShopImagePath(long shopId){
        String imagepath = "/upload/item/shop/"+shopId+"/";
        return imagepath.replace("/",separator);
    }
}
