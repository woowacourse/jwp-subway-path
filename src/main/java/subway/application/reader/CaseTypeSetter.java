package subway.application.reader;

import subway.domain.vo.Section;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CaseTypeSetter {
    public static CaseDto setCase(final CaseDto caseDto, List<Section> allSection) {
        if (allSection.isEmpty()) {
            return setNonDeleteSaveCase(caseDto);
        }
        List<Section> departureMatch = allSection.stream()
                .filter(section -> section.getDepartureValue().equals(caseDto.getDeparture()))
                .collect(toList());
        List<Section> crossMatch = allSection.stream()
                .filter(section -> section.getDepartureValue().equals(caseDto.getArrival()) ||
                        section.getArrivalValue().equals(caseDto.getDeparture()))
                .collect(toList());
        List<Section> arrivalMatch = allSection.stream()
                .filter(section -> section.getArrivalValue().equals(caseDto.getArrival()))
                .collect(toList());

        if (crossMatch.size() > 1 || (!departureMatch.isEmpty() & !arrivalMatch.isEmpty())) {
            return setExceptionCase();
        }
        if (crossMatch.size() == 1 & departureMatch.isEmpty() & arrivalMatch.isEmpty()) {
            return setNonDeleteSaveCase(caseDto);
        }
        if (!departureMatch.isEmpty() & arrivalMatch.isEmpty()) {
            return setUpperCase(caseDto, departureMatch);
        }
        if (!arrivalMatch.isEmpty()) {
            return setLowerCase(caseDto, arrivalMatch);
        }
        return setExceptionCase();
    }

    private static CaseDto setLowerCase(CaseDto caseDto, List<Section> arrivalMatch) {
        return new CaseDto.Builder()
                .lineId(caseDto.getLineId())
                .departure(caseDto.getDeparture())
                .arrival(caseDto.getArrival())
                .distance(caseDto.getDistance())
                .caseType(CaseType.LOWER)
                .deleteSection(arrivalMatch.get(0))
                .build();
    }

    private static CaseDto setUpperCase(CaseDto caseDto, List<Section> departureMatch) {
        return new CaseDto.Builder()
                .lineId(caseDto.getLineId())
                .departure(caseDto.getDeparture())
                .arrival(caseDto.getArrival())
                .distance(caseDto.getDistance())
                .caseType(CaseType.UPPER)
                .deleteSection(departureMatch.get(0))
                .build();
    }

    private static CaseDto setExceptionCase() {
        return new CaseDto.Builder()
                .caseType(CaseType.EXCEPTION_CASE)
                .build();
    }

    private static CaseDto setNonDeleteSaveCase(CaseDto caseDto) {
        return new CaseDto.Builder()
                .lineId(caseDto.getLineId())
                .departure(caseDto.getDeparture())
                .arrival(caseDto.getArrival())
                .distance(caseDto.getDistance())
                .caseType(CaseType.NON_DELETE_SAVE_CASE)
                .build();
    }
}
