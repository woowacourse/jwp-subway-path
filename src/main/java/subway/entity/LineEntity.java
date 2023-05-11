package subway.entity;

import lombok.Getter;

@Getter
public class LineEntity {

    private final Long id;
    private final String name;

    public LineEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class Builder {

        private Long id;
        private String name;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public LineEntity build() {
            return new LineEntity(id, name);
        }
    }
}
