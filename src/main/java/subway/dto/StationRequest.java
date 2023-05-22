package subway.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;

public class StationRequest {
    
    @NotBlank
    private String name;
    
    public StationRequest() {
    }
    
    public StationRequest(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StationRequest that = (StationRequest) o;
        return Objects.equals(this.name, that.name);
    }
}
