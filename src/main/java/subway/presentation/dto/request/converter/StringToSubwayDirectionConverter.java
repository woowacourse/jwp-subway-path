package subway.presentation.dto.request.converter;


import org.springframework.core.convert.converter.Converter;

public class StringToSubwayDirectionConverter implements Converter<String, SubwayDirection> {

    @Override
    public SubwayDirection convert(final String value) {
        return SubwayDirection.from(value);
    }
}
