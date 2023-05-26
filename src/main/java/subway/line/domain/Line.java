package subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Line {

  private final Long id;
  private final String name;
  private Section starter;

  public Line(
      final Long id,
      final String name,
      final Collection<Section> sections
  ) {
    this.id = id;
    this.name = name;
    initialLine(sections);
  }

  private void initialLine(final Collection<Section> sections) {
    List<Section> retrySection = new ArrayList<>();

    for (final Section section : sections) {
      addUnorderedSection(retrySection, section);
    }

    retryUnorderedSection(retrySection);
  }

  private void addUnorderedSection(
      final List<Section> retrySection,
      final Section section
  ) {
    try {
      add(section);
    } catch (IllegalArgumentException exception) {
      retrySection.add(section);
    }
  }

  private void retryUnorderedSection(final Collection<Section> retrySection) {
    for (final Section section : retrySection) {
      add(section);
    }
  }

  public void add(Section newSection) {
    if (starter == null) {
      starter = newSection;
      return;
    }

    final Section targetSection = starter.findPreSectionOnAdd(newSection);
    final Section sameStartSection = starter.findSameSectionOnAdd(newSection);

    if (canExchangeStarter(targetSection)) {

      if (starter.isLinked(newSection)) {
        exchangeStarterOnAdd(newSection);
        return;
      }

      if (sameStartSection != null) {
        newSection.updateNextSection(starter);
        starter.updateSectionOnAdd(newSection);
        starter = newSection;
        return;
      }

      throw new IllegalArgumentException("해당 섹션은 현재 Line에 추가할 수 없습니다.");
    }

    starter.addNext(newSection);
  }

  private boolean canExchangeStarter(final Section targetSection) {
    return targetSection == null;
  }

  private void exchangeStarterOnAdd(final Section newSection) {
    newSection.updateNextSection(starter);
    starter = newSection;
  }

  public void delete(final Station deletedStation) {
    final Section target = starter.findPreSectionOnDelete(deletedStation);

    if (canDeleteLine(target)) {
      starter = null;
      return;
    }

    if (canExchangeStarter(target)) {
      if (starter.isSameCurrentWith(deletedStation)) {
        exchangeStarterOnDelete();
        return;
      }
      throw new IllegalArgumentException("삭제할 역이 없습니다.");
    }

    if (canExchangeTail(target)) {
      Section newTailSection = findPreSection(target);
      newTailSection.disconnectNextSection();
      return;
    }

    starter.delete(deletedStation);
  }

  private boolean canDeleteLine(final Section target) {
    return target == starter && target.getTo() == null;
  }

  private void exchangeStarterOnDelete() {
    final Section newStarter = starter.getTo();
    starter.disconnectNextSection();
    starter = newStarter;
  }

  private boolean canExchangeTail(final Section target) {
    return target.getTo() == null;
  }

  private Section findPreSection(Section targetSection) {
    Section current = starter;

    while (isNotTail(targetSection, current)) {
      current = current.getTo();
    }
    return current;
  }

  private boolean isNotTail(
      final Section targetSection,
      final Section current
  ) {
    return current.getTo() != targetSection;
  }

  public boolean isDeleted() {
    return starter == null;
  }

  public List<Section> getSections() {
    Section current = starter;

    List<Section> sections = new ArrayList<>();

    while (current != null) {
      sections.add(current.cloneSection());
      current = current.getTo();
    }

    return sections;
  }

  public Long getId() {
    return id;
  }

  public Section getStarter() {
    return starter;
  }

  public String getName() {
    return name;
  }
}
