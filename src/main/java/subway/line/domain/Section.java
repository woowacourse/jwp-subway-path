package subway.line.domain;

import java.util.Objects;

public class Section {

  private final Long id;
  private final Stations stations;
  private Section to;

  public Section(final Long id, final Stations stations) {
    this.id = id;
    this.stations = stations;
    to = null;
  }

  public Section(final Stations stations) {
    this(null, stations);
  }

  public Section findPreSectionOnAdd(final Section newSection) {
    Section current = this;

    while (current != null) {
      if (current.stations.isLinked(newSection.stations)) {
        return current;
      }
      current = current.to;
    }

    return null;
  }

  public Section findSameSectionOnAdd(final Section newSection) {
    Section current = this;

    while (current != null) {
      if (current.stations.isSameCurrent(newSection.stations.getCurrent())) {
        return current;
      }
      current = current.to;
    }

    return null;
  }

  public void addNext(final Section newSection) {
    Section current = findPreSectionOnAdd(newSection);

    if (isTail(current)) {
      current.to = newSection;
      return;
    }

    if (current.to.stations.isDistanceShorterThan(newSection.stations)) {
      throw new IllegalArgumentException("새로운 구간이 길이가 기존 구간의 길이보다 깁니다.");
    }

    addIntermediate(newSection, current);
  }

  private boolean isTail(final Section current) {
    return current.to == null;
  }

  private void addIntermediate(final Section newSection, final Section current) {
    newSection.to = current.to;
    current.to = newSection;

    newSection.to.stations.updateStationOnAdd(newSection.stations);
  }

  public void updateSectionOnAdd(final Section newSection) {
    stations.updateStationOnAdd(newSection.stations);
  }

  public boolean isLinked(final Section other) {
    return other.stations.isLinked(this.stations);
  }

  public void delete(final Station deletedStation) {
    final Section current = findPreSectionOnDelete(deletedStation);

    if (current == null) {
      throw new IllegalArgumentException("해당 Section에는 삭제할 역이 존재하지 않습니다.");
    }

    final Section deletedSection = current.to;

    current.stations.updateStationOnDelete(deletedSection.stations);

    updateSectionOnDelete(current, deletedSection);
  }

  private void updateSectionOnDelete(final Section current, final Section deletedSection) {
    current.to = deletedSection.to;
    deletedSection.to = null;
  }

  public Section findPreSectionOnDelete(final Station station) {
    Section current = this;

    while (current != null) {
      if (current.stations.isSameNext(station)) {
        return current;
      }
      current = current.to;
    }

    return null;
  }

  public boolean isSameCurrentWith(final Station station) {
    return stations.isSameCurrent(station);
  }

  public boolean isSame(final Section section) {
    return stations.isSame(section.stations);
  }

  public void disconnectNextSection() {
    updateNextSection(null);
  }

  public void updateNextSection(final Section section) {
    to = section;
  }

  public Section cloneSection() {
    return new Section(id, stations.cloneStations());
  }

  public Long getId() {
    return id;
  }

  public Stations getStations() {
    return stations;
  }

  public Section getTo() {
    return to;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Section section = (Section) o;
    return Objects.equals(id, section.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
