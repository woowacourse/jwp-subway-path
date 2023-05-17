package subway.domain.section.add;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.section.add.strategy.AddSectionStrategy;
import subway.domain.section.add.strategy.DownEndAddSectionStrategy;
import subway.domain.section.add.strategy.InitAddSectionStrategy;
import subway.domain.section.add.strategy.MidAddSectionStrategy;
import subway.domain.section.add.strategy.UpEndAddSectionStrategy;

import java.util.Arrays;
import java.util.function.BiPredicate;

public enum AddSectionHandler {

    INIT(Sections::isInitial, new InitAddSectionStrategy()),
    UP_END(Sections::isUpEndSection, new UpEndAddSectionStrategy()),
    DOWN_END(Sections::isDownEndSection, new DownEndAddSectionStrategy()),
    MID(Sections::isMidSection, new MidAddSectionStrategy());

    private final BiPredicate<Sections, Section> isStrategy;
    private final AddSectionStrategy addSectionStrategy;

    AddSectionHandler(final BiPredicate<Sections, Section> isStrategy, final AddSectionStrategy addSectionStrategy) {
        this.isStrategy = isStrategy;
        this.addSectionStrategy = addSectionStrategy;
    }

    public static AddSectionStrategy bind(final Sections sections, final Section insertSection) {
        return Arrays.stream(AddSectionHandler.values())
                .filter(addSectionHandler -> addSectionHandler.isStrategy.test(sections, insertSection))
                .map(addSectionHandler -> addSectionHandler.addSectionStrategy)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 추가할 수 없습니다."));
    }
}
