package automation.library.conversion2jmx.postman2jmx.model.postman;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import automation.library.conversion2jmx.common.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostmanEvent {

    @JsonProperty("listen")
    private String listen;

    @JsonProperty("script")
    private PostmanScript script;

    @JsonIgnore
    private List<String> variables = new ArrayList<>();

    public PostmanEvent() {
    }

    public PostmanEvent(String listen, PostmanScript script) {
        this.listen = listen;
        this.script = script;
        this.variables = ValueUtils.extractVariables(script.getExecs());
    }

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public PostmanScript getScript() {
        return script;
    }

    public void setScript(PostmanScript script) {
        this.script = script;
        this.variables = ValueUtils.extractVariables(script.getExecs());

    }

    public List<String> getVariables() {
        return variables;
    }
}
