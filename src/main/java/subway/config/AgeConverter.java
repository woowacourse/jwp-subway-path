package subway.config;

import org.springframework.core.convert.converter.Converter;
import subway.domain.discount.Age;

public class AgeConverter implements Converter<String, Age> {
    @Override
    public Age convert(String age) {
        return new Age(parseInt(age));
    }

    public int parseInt(String age) {
        try {
            return Integer.parseInt(age);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("나이는 숫자만 가능합니다.");
        }
    }
}
