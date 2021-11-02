package automation.library.conversion2jmx.postman2jmx.model.postman;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import automation.library.conversion2jmx.common.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostmanUrl {

    @JsonProperty("raw")
    private String raw;

    @JsonProperty("query")
    private List<PostmanQuery> queries = new ArrayList<>();

    public PostmanUrl() {
    }

    public PostmanUrl(String raw) {
        this.raw = ValueUtils.value(raw);
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = ValueUtils.value(raw);
    }

    public List<PostmanQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<PostmanQuery> queries) {
        this.queries = queries;
    }
}
