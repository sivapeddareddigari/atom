package automation.library.conversion2jmx.postman2jmx.model.postman;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import automation.library.conversion2jmx.common.utils.ValueUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostmanFormDataBody {

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type;

    public PostmanFormDataBody() {
    }

    public PostmanFormDataBody(String key, String value, String description, String type) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = ValueUtils.value(value);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
