package subway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import subway.controller.dto.request.SubwayDirection;

@Component
public class StringToSubwayDirectionConverter implements Converter<String, SubwayDirection> {

    @Override
    public SubwayDirection convert(final String value) {
        return SubwayDirection.from(value);
    }
}
