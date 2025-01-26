package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class DuckWithIdResponse {
    @JsonProperty("color")
    private String color;
    @JsonProperty("height")
    private Double height;
    @JsonProperty("id")
    private int id;
    @JsonProperty("material")
    private String material;
    @JsonProperty("sound")
    private String sound;
    @JsonProperty("wingsState")
    private WingState wingsState;
}