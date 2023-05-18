package subway.repository.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.entity.LineEntity;
import subway.service.domain.LineProperty;

import static org.assertj.core.api.Assertions.assertThat;

class LinePropertyConverterTest {

    @Test
    @DisplayName("LinePropertyConverter 를 이용해 LineEntity 를 Station 으로 변경한다.")
    void entityToDomain() {
        LineEntity lineEntity = new LineEntity(1L, "name", "red");

        LineProperty lineProperty = LinePropertyConverter.entityToDomain(lineEntity);

        assertThat(lineProperty.getId()).isEqualTo(1L);
        assertThat(lineProperty.getName()).isEqualTo("name");
        assertThat(lineProperty.getColor()).isEqualTo("red");
    }

    @Test
    @DisplayName("LinePropertyConverter 를 이용해 LineEntity 를 Station 으로 변경 (ID 는 따로 주입)")
    void entityToDomainWithId() {
        LineEntity lineEntity = new LineEntity("name", "red");

        LineProperty lineProperty = LinePropertyConverter.entityToDomain(1L, lineEntity);

        assertThat(lineProperty.getId()).isEqualTo(1L);
        assertThat(lineProperty.getName()).isEqualTo("name");
        assertThat(lineProperty.getColor()).isEqualTo("red");
    }

    @Test
    @DisplayName("LinePropertyConverter 를 이용해 LineProperty 를 LineEntity 으로 변경한다. (ID 있음)")
    void domainToEntityWithId() {
        LineProperty lineProperty = new LineProperty(1L, "name", "red");

        LineEntity lineEntity = LinePropertyConverter.domainToEntity(lineProperty);

        assertThat(lineEntity.getId()).isEqualTo(1L);
        assertThat(lineEntity.getName()).isEqualTo("name");
        assertThat(lineEntity.getColor()).isEqualTo("red");
    }

    @Test
    @DisplayName("LinePropertyConverter 를 이용해 LineProperty 을 LineEntity 로 변경한다. (ID 없음)")
    void domainToEntity() {
        LineProperty lineProperty = new LineProperty("name", "red");

        LineEntity lineEntity = LinePropertyConverter.domainToEntity(lineProperty);

        assertThat(lineEntity.getId()).isNull();
        assertThat(lineEntity.getName()).isEqualTo("name");
        assertThat(lineEntity.getColor()).isEqualTo("red");
    }

}
