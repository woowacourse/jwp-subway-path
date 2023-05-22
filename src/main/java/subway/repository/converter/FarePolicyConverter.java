package subway.repository.converter;

import subway.entity.FarePolicyEntity;
import subway.service.domain.FarePolicy;
import subway.service.domain.LineProperty;

public class FarePolicyConverter {

    private FarePolicyConverter() {
    }

    public static FarePolicyEntity domainToEntity(FarePolicy farePolicy) {
        return new FarePolicyEntity(
                farePolicy.getLineProperty().getId(),
                farePolicy.getAdditionalFare()
        );
    }

    public static FarePolicy entityToDomain(Long id,
                                            LineProperty lineProperty,
                                            FarePolicyEntity farePolicyEntity) {
        return new FarePolicy(
                id,
                lineProperty,
                farePolicyEntity.getAdditionalFare()
        );
    }

}
