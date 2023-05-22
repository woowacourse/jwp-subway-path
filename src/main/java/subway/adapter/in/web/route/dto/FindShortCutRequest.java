package subway.adapter.in.web.route.dto;

public class FindShortCutRequest {
    private String fromStation;
    private String toStation;
    private Integer age;

    public FindShortCutRequest() {
    }

    public FindShortCutRequest(final String fromStation, final String toStation, final Integer age) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.age = age;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public Integer getAge() {
        return age;
    }
}
