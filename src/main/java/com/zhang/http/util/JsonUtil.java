package com.zhang.http.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson (Object object){
        try {
            return mapper.writeValueAsString(object);
        }catch (Exception e){
            System.out.println(e);
            return "{}";
        }
    }

    public static <T> T fromJson(String jsonString,Class<T> clazz){
        if (StringUtils.isEmpty(jsonString)){
            return null;
        }
        try {
            return mapper.readValue(jsonString,clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
