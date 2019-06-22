package com.ycg.ksh.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ycg.ksh.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  自定义json转换
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/06 0006
 */
public class CustomObjectMapper extends ObjectMapper {
    protected static final Logger logger = LoggerFactory.getLogger(CustomObjectMapper.class);

    private static final DateTimeFormatter YMDHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CustomObjectMapper(){
        this.registerModule(buildLocalDate());
    }

    private Module buildLocalDate(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(localDateTime.format(YMDHM));
            }
        });

        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(localDate.format(YMD));
            }
        });

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                if(p != null && StringUtils.isNotBlank(p.getText())){
                    try{
                        return LocalDateTime.parse(p.getText(), YMDHM);
                    }catch (Exception e){
                        logger.error("deserialize  {} to LocalDateTime error", p.getText());
                    }
                }
                return null;
            }
        });

        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                if(p != null && StringUtils.isNotBlank(p.getText())){
                    try{
                        return LocalDate.parse(p.getText(), YMD);
                    }catch (Exception e){
                        logger.error("deserialize  {} to LocalDate error", p.getText());
                    }
                }
                return null;
            }
        });
        return module;
    }
}
