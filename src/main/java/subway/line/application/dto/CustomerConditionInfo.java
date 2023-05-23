package subway.line.application.dto;

import subway.line.domain.fare.application.domain.Age;

public class CustomerConditionInfo {
    private Age age;

    public CustomerConditionInfo(Age age) {
        this.age = age;
    }

    public Age getAge() {
        return age;
    }
}
