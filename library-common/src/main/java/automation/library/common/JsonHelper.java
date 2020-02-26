package automation.library.common;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

public class JsonHelper {

    static Logger log = LogManager.getLogger(JsonHelper.class);

    /**
     * Reads JSON file and returns a specific json object from that file based on the root element value
     */
    public static JSONObject getJSONData(String filepath, String...key) {
        try {
            FileReader reader = new FileReader(filepath);
            JSONTokener token = new JSONTokener(reader);
            JSONObject json = (JSONObject) (key.length>0?new JSONObject(token).get(key[0]):new JSONObject(token));
            return json;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Reads JSON file and returns a specific json object from that file based on the root element value
     */
    public static JSONArray getJSONArray(String filepath, String... key) {
        try {
            FileReader reader = new FileReader(filepath);
            JSONTokener token = new JSONTokener(reader);
            JSONArray json = (JSONArray) (key.length > 0 ? new JSONObject(token).get(key[0]) : new JSONArray(token));
            return json;
        } catch (FileNotFoundException e) {
            log.error("file not found", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * reads data from json files and casts to supplied pojo format
     */
    public static <T> T getDataPOJO(String filepath, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        File file = new File(filepath);
        T dataObj = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            dataObj = gson.fromJson(br, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataObj;
    }

    /**
     * reads data from json files and casts to supplied pojo format
     */
    public static <T> T getData(String path, String dataGroup, Class<T> clazz) throws IOException {
        String filePath=path+dataGroup+".json";
        Gson gson = new Gson();
        File file = new File(filePath);
        T dataObj = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            dataObj = gson.fromJson(br, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataObj;
    }

    /**
     * Convert json object to map
     * @param json input JSON object
     * @return map
     */
    public static Map<String, String> getJSONToMap(JSONObject json) {

        Map<String, String> map = new HashMap<String, String>();
        String[] keys = JSONObject.getNames(json);
        for (String key : keys) {
            map.put(key, json.get(key).toString());
        }

        return map;

    }

    /**
     * Performs a recursive merge between 2 json objects.
     * When the json includes an array then will loop through this as
     * part of the recursive merge.
     */
    public static JSONObject jsonMerge(JSONObject source, JSONObject target) {
        String[] keys = JSONObject.getNames(source);
        if (keys !=null){
            for (String key: keys) {
                Object value = source.get(key);
                if (!target.has(key)) {
                    target.put(key, value);
                } else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    JSONArray targetarray = (JSONArray) target.get(key);
                    for (int i=0;i<array.length();i++){
                        Object arrayvalue = array.get(i);
                        Object targetarrayvalue = targetarray.get(i);
                        if (arrayvalue instanceof JSONObject) {
                            JSONObject valueJson = (JSONObject)arrayvalue;
                            JSONObject targetvalueJson = (JSONObject)targetarrayvalue;
                            jsonMerge(valueJson, targetvalueJson);
                        }else {
                            target.put(key, value);
                        }
                    }
                } else if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject)value;
                    jsonMerge(valueJson, target.getJSONObject(key));
                } else {
                    target.put(key, value);
                }
            }
        }

        return target;
    }
}
