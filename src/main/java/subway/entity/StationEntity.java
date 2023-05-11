package subway.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StationEntity {

    private Long id;
    private String name;

    private StationEntity(Long id, String name) {
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

        public StationEntity build() {
            return new StationEntity(id, name);
        }
    }
}
