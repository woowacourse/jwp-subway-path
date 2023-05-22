package subway.domain.fare;

import java.util.Optional;

import subway.domain.Sections;

public class FarePolicyRelatedParameters {

    private final Sections sections;
    private final Integer age;

    private FarePolicyRelatedParameters(Builder builder){
        sections = builder.sections;
        age = builder.age;
    }

    public Sections getSections() {
        return sections;
    }

    public Optional<Integer> getOptionalAge() {
        return Optional.ofNullable(age);
    }

    public static class Builder {

        private final Sections sections;

        private Integer age = null;

        public Builder(Sections sections) {
            this.sections = sections;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public FarePolicyRelatedParameters build() {
            return new FarePolicyRelatedParameters(this);
        }
    }
}
