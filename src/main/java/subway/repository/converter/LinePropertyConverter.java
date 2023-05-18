package subway.repository.converter;

import subway.entity.LineEntity;
import subway.service.domain.LineProperty;

public class LinePropertyConverter {

    private LinePropertyConverter() {
    }

    public static LineProperty entityToDomain(Long id, LineEntity lineEntity) {
        return new LineProperty(
                id,
                lineEntity.getName(),
                lineEntity.getColor()
        );
    }

    public static LineProperty entityToDomain(LineEntity lineEntity) {
        return new LineProperty(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor()
        );
    }

    public static LineEntity domainToEntity(LineProperty lineProperty) {
        return new LineEntity(
                lineProperty.getId(),
                lineProperty.getName(),
                lineProperty.getColor()
        );
    }

}
