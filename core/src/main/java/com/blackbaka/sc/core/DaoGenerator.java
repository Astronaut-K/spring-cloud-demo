package com.blackbaka.sc.core;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * DAO 代码文件生成器
 * </p>
 */
public class DaoGenerator {


    /**
     * RUN THIS
     */
    public static void main(String[] args) {

        // +++++  配置开始  +++++
        //需要生成的数据库表在这里填写对应表名
        String[] tableNames = new String[]{
//                "account",
                "orders",
//                "stock"
        };
        // mysql配置
        String host = "127.0.0.1";
        String port = "3306";
        String schema = "seata_demo";
        String user = "root";
        String pass = "root";

        // 工程内项目路径
        String javaPath = "/core/src/main/java";
        String xmlPath = "/core/src/main/resources/mapper/core";
        String modelPackage = "com.blackbaka.seata.core.dao.model";
        String mapperPackage = "com.blackbaka.seata.core.dao.mapper";
        // +++++  配置结束  +++++

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + javaPath);
        gc.setAuthor("Auto Generate");
        gc.setOpen(false);
        //是否覆盖
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setSwagger2(true);
        gc.setDateType(DateType.ONLY_DATE);

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://" + host + ":" + port + "/" + schema + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(user);
        dsc.setPassword(pass);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(scanner("模块名"));
        // pc.setParent("com.baomidou.mybatisplus.samples.generator");

        //另一种方式
        pc.setParent(null);
        pc.setEntity(modelPackage);
        pc.setMapper(mapperPackage);
        pc.setModuleName("");

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + xmlPath + "/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setTablePrefix("base_");
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setEntityBuilderModel(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setLogicDeleteFieldName("deleted");

//        strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
//        strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix(pc.getModuleName() + a"_");

        // 设为null代表生成全部表
        strategy.setInclude(tableNames);
        mpg.setStrategy(strategy);


        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setController(null);
        templateConfig.setServiceImpl(null);
        mpg.setTemplate(templateConfig);

        mpg.execute();
    }

}
