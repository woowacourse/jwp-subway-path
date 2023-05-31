package subway.route.domain.fare;

import java.util.HashMap;
import java.util.Map;

public class FareFactors {

    private final Map<String, Object> fareFactors = new HashMap<>();

    public void setFactor(String key, Object value) {
        fareFactors.put(key, value);
    }

    public Object getFactor(String key) {
        return fareFactors.get(key);
    }
}
