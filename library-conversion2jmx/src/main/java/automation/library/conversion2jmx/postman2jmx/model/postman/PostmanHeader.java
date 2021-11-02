package automation.library.conversion2jmx.postman2jmx.model.postman;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import automation.library.conversion2jmx.common.utils.ValueUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostmanHeader {

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;

    public PostmanHeader() {
    }

    public PostmanHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = ValueUtils.value(key);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = ValueUtils.value(value);
    }
}
