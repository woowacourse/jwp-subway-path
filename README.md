# jwp-subway-path

## Domain

---

### Subway
- [ ] Line List 를 가진다.
- [ ] 단일 노선 조회가 가능하다.
  - [ ] 찾는 노선이 없는 경우 예외가 발생한다. (ID로 찾는다.)
- [ ] 노선 전체 조회가 가능하다.

### Line
- [ ] LineProperty 를 가진다 (id, name, color)
- [ ] Sections 를 가진다.
- [ ] 중복된 이름, 중복된 색깔은 가질 수 없다.

### Sections
- [ ] Section List 를 가진다.
- [ ] Station 이 포함되어 있는 Section 을 찾을 수 있다.
- [ ] Line, PreviousSection, NextSection이 같은 경우를 가질 수 없다.

### Section
- [ ] PreviousStation, NextStation을 가진다.
- [ ] 거리를 가진다. (Distance)
  - [ ] 거리가 양수가 아닌 경우 예외가 발생한다.

### Station
- [ ] 중복된 Station 의 이름은 존재할 수 없다.

### Direction
- [ ] Up, Down 이라는 값만이 존재한다.
  - [ ] 이외의 값이 들어올 경우 예외가 발생한다.

![img.png](img.png)

## 예외 상황들

---

