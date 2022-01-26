package com.eb.modules.controller;

import com.alibaba.fastjson.JSONObject;
import com.eb.framework.utils.CommonUtil;
import com.eb.framework.utils.CustomFileUtil;
import com.eb.framework.utils.PageHelper;
import com.eb.framework.utils.Res;
import com.eb.modules.model.Product;
import com.eb.modules.model.User;
import com.eb.modules.service.ProductService;
import com.eb.modules.service.ProductService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;

@RestController
@RequestMapping("/sys/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${custom.common.uploadPath}")
    private String uploadPath;

    public static File getImgDirFile(String targetPath) {
        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = new String("src/main/resources" + targetPath);

        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }

    /**
     * 保存信息
     **/
    @RequestMapping("/save")
//    public Res save(@RequestBody Product product) {
    public Res save(
//                    @RequestParam("uploadFile") MultipartFile file,
                    @RequestParam("uploadFile")MultipartFile file,
                    @RequestParam("productId") String productId,
                    @RequestParam("productNum") String productNum,
                    @RequestParam("classify") String classify,
                    @RequestParam("productName") String productName,
                    @RequestParam("oldPicUrl") String oldPicUrl,
                    @RequestParam("vendor") String vendor,
                    @RequestParam("stock") String stock,
                    @RequestParam("remark") String remark) throws Exception {

        // 封装实体类
        Product product = new Product();

        // 该方式存储的是容器路径，需对应调整为指向当前目录的路径
        if(file!=null){
            // 拿到文件名
            String fileName = file.getOriginalFilename();
            // 随机生成uuid作为文件名 file.getOriginalFilename().split("\\.")
            String newFileName = CommonUtil.getRandomId() + fileName.substring(fileName.lastIndexOf("."),fileName.length());
            // 存放上传图片的文件夹
            File fileDir = getImgDirFile(uploadPath);
            // 输出文件夹绝对路径  -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径
            System.out.println(fileDir.getAbsolutePath());
            // 构建真实的文件路径
            File newFile = new File(fileDir.getAbsolutePath() + "/" + newFileName);
            System.out.println(newFile.getAbsolutePath());
            // 上传图片到 -》 “绝对路径”
            file.transferTo(newFile);
            // 存储文件名称，后续系统自动拼接完整路径 // /ebProject/** 对应前端项目相对路径前缀（此处指定完整路径）
            product.setPicUrl("http://localhost:8080"+"/ebProject" + uploadPath + "/" + newFileName);
        }else{
            // 没有检测到更新文件，引用原有的旧文件路径
            product.setPicUrl(oldPicUrl);
        }
        product.setProductId(productId);
        product.setProductNum(productNum);
        product.setClassify(classify);
        product.setProductName(productName);
        product.setVendor(vendor);
        product.setStock(stock);
        product.setRemark(remark);
        productService.edit(product);
        return Res.ok();
    }

    /**
     * 删除信息(单条或批量删除)
     **/
    @RequestMapping("/delete")
    public Res delete(@RequestBody JSONObject requestParam) {
        productService.delete(requestParam.getObject("productIdList", ArrayList.class));
        return Res.ok();
    }

    /**
     * 更新信息（处理json数据）
     **/
    @RequestMapping("/update")
    public Res update(@RequestBody Product product) {
        productService.edit(product);
        return Res.ok();
    }

    /**
     * list信息
     **/
    @RequestMapping("/list")
    public Res list(@RequestBody JSONObject queryCond) {
        Page<Product> pageData = productService.getByPage(queryCond);
        // 借助分页插件转化分页数据
        return Res.ok().put("page", new PageHelper<Product>(pageData));
    }

    /**
     * 获取信息
     **/
    @RequestMapping("/info/{productId}")
    public Res info(@PathVariable String productId) {
        return Res.ok().put("product",productService.getById(productId));
    }

    /**
     * 获取信息
     **/
    @RequestMapping("/getByCond")
    public Res getByCond(@RequestBody JSONObject requestParam) {
        return Res.ok().put("list", productService.getByCond(requestParam));
    }

}
