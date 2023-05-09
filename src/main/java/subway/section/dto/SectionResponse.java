package subway.section.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.domain.Section;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

    List<Section> sections;
}
