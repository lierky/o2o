package com.miaozhen.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private  static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private  static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    //将CommonsMultipartFile转换为File对象
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile){
        File newFile = new File(cFile.getOriginalFilename());
        System.out.println();
        try {
            cFile.transferTo(newFile);
        } catch (IllegalStateException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }catch (IOException e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }
    //生成缩略图,并返回目标相对路径，存入数据库中
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName,String targetAddr){
        //获取图片名
        String realFilename = getRandomFileName();
        //获取扩展名
        String extension = getFileExtension(fileName);
        //创建存储的目录
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr +realFilename+extension;
        logger.error("current relativeAddr is:"+relativeAddr);
        //输出的目的地址
        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
        //输入要处理的图片，生成缩略图
        logger.debug("current cpmplete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
        try {
            Thumbnails.of(thumbnailInputStream).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //生成文件名称
    public static String getRandomFileName() {
        int rannum = r.nextInt(89999)+10000;
        String nowTimeStr = simpleDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }



    public static void main(String[] args) throws IOException {

        Thumbnails.of(new File("C:/liuhongli/test.png")).size(600,600)
                .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).
                outputQuality(0.8f).toFile("C:/liuhongli/test-watermark.png");
    }
}
