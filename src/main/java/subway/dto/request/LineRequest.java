package subway.dto.request;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull(message = "라인 이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private String name;

    @NotNull(message = "라인 이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private String color;

    private int cost;

    public LineRequest() {
    }

    public LineRequest(String name, String color, int cost) {
        this.name = name;
        this.color = color;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getCost() {
        return cost;
    }
}
