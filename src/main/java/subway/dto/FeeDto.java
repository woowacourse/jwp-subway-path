package subway.dto;

public class FeeDto {

    private Integer fee;

    private FeeDto() {
    }

    public FeeDto(final Integer fee) {
        this.fee = fee;
    }

    public Integer getFee() {
        return fee;
    }

}
