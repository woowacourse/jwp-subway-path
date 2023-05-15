package subway.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SectionCreateRequest {
    @NonNull
    private String upStationName;
    @NonNull
    private String downStationName;
    @Positive
    private Long distance;
}
