package subway.domain.farePolicy;

import org.springframework.stereotype.Component;
import subway.application.FarePolicy;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.general.Age;
import subway.domain.general.Money;
import subway.dto.PassengerDto;
import subway.exception.business.SectionNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Component
public class LineAdditionalPolicy implements FarePolicy {

    @Override
    public Money applyPolicies(Money money, List<Sections> allSections, List<Section> path, PassengerDto passengerDto, FarePolicyChain policyChain) {
        Money fare = money;

        List<Long> lineIds = findPassLines(allSections, path);
        fare = fare.plus(LineAdditionalFee.getBiggestFee(lineIds));

        return policyChain.applyPolicy(fare, allSections, path, passengerDto);
    }

    private List<Long> findPassLines(List<Sections> allSections, List<Section> path) {
        List<Long> lines = new ArrayList<>();
        for (Section section : path) {
            Sections containLine = allSections.stream().filter(sections -> sections.contains(section)).findFirst().orElseThrow(() -> new SectionNotFoundException());
            lines.add(containLine.getLineId());
        }
        return lines;
    }
}
