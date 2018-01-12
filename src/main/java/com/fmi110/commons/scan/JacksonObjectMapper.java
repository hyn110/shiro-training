package com.fmi110.commons.scan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 定制 Jackson 序列化 Date 类型的格式,以及时区问题
 * 不指定中国时区,时间会相差8个小时
 */
@Component("jacksonObjectMapper")
public class JacksonObjectMapper extends ObjectMapper {

    private static final Locale CHINA = Locale.CHINA;
    
    public JacksonObjectMapper() {
        this.setLocale(CHINA);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", CHINA));
        this.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

}
