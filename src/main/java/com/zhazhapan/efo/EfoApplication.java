package com.zhazhapan.efo;

import com.zhazhapan.config.JsonParser;
import com.zhazhapan.efo.config.TokenConfig;
import com.zhazhapan.efo.modules.constant.ConfigConsts;
import com.zhazhapan.efo.modules.constant.DefaultValues;
import com.zhazhapan.util.FileExecutor;
import com.zhazhapan.util.MailSender;
import com.zhazhapan.util.ReflectUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * @author pantao
 */
@SpringBootApplication
@MapperScan("com.zhazhapan.efo.dao")
@EnableAutoConfiguration
@EnableTransactionManagement
public class EfoApplication extends SpringBootServletInitializer {

    public static JsonParser settings = new JsonParser();

    public static List<Class<?>> controllers;

    public static Hashtable<String, Integer> tokens;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        settings.setJsonObject(FileExecutor.read(EfoApplication.class.getResourceAsStream(DefaultValues.SETTING_PATH)));
        MailSender.config(settings.getObjectUseEval(ConfigConsts.EMAIL_CONFIG_OF_SETTINGS));
        controllers = ReflectUtils.getClasses(DefaultValues.CONTROLLER_PACKAGE);
        tokens = TokenConfig.loadToken();
        SpringApplication.run(EfoApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)  {
        try {
            settings.setJsonObject(FileExecutor.read(EfoApplication.class.getResourceAsStream(DefaultValues.SETTING_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSender.config(settings.getObjectUseEval(ConfigConsts.EMAIL_CONFIG_OF_SETTINGS));
        try {
            controllers = ReflectUtils.getClasses(DefaultValues.CONTROLLER_PACKAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tokens = TokenConfig.loadToken();
        return builder.sources(EfoApplication.class);

    }

}
