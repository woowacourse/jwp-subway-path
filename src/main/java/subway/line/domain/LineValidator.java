package subway.line.domain;

import static subway.line.exception.line.LineExceptionType.DUPLICATE_LINE_NAME;
import static subway.line.exception.line.LineExceptionType.INCONSISTENT_EXISTING_SECTION;

import org.springframework.stereotype.Component;
import subway.line.exception.line.LineException;

@Component
public class LineValidator {

    private final LineRepository lineRepository;

    public LineValidator(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void validateDuplicateLineName(final String lineName) {
        if (lineRepository.findByName(lineName).isPresent()) {
            throw new LineException(DUPLICATE_LINE_NAME);
        }
    }

    public void validateSectionConsistency(final Section newSection) {
        lineRepository.findAll().stream()
                .flatMap(line -> line.sections().stream())
                .filter(it -> it.containsAllStation(newSection))
                .findAny()
                .ifPresent(it -> validateConsistency(newSection, it));
    }

    private void validateConsistency(final Section newSection, final Section existing) {
        if (!existing.equals(newSection)) {
            throw new LineException(INCONSISTENT_EXISTING_SECTION);
        }
    }
}
