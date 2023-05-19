# jwp-subway-path

## Domain

---

### Subway
- [x] Line List 를 가진다.
- [x] 단일 노선 조회가 가능하다.
  - [x] 찾는 노선이 없는 경우 예외가 발생한다. (ID로 찾는다.)
- [x] 노선 전체 조회가 가능하다.

### SingleLine
- [x] LineProperty 와 Stations 를 가진다.
- [x] 노선에 포함되어 있는 Station 들을 나타낸다.

### Line
- [x] LineProperty 를 가진다 (id, name, color)
- [x] Sections 를 가진다.
- [x] 중복된 이름, 중복된 색깔은 가질 수 없다.

### Sections
- [x] Section List 를 가진다.
- [x] Station 이 포함되어 있는 Section 을 찾을 수 있다.
- [x] Line, PreviousSection, NextSection이 같은 경우를 가질 수 없다.

### Section
- [x] PreviousStation, NextStation을 가진다.
- [x] 거리를 가진다. (Distance)
  - [x] 거리가 양수가 아닌 경우 예외가 발생한다.

### Station
- [x] 중복된 Station 의 이름은 존재할 수 없다.

### Direction
- [x] Up, Down 이라는 값만이 존재한다.
  - [x] 이외의 값이 들어올 경우 예외가 발생한다.

### Path
- [x] Direction, Station, Distance 를 가진다.

### RouteMap
- [x] 해당 Station 에서 이동할 수 있는 그래프를 제공한다.
- [x] 노선 내에 포함되어 있는 역들을 하행 -> 상행 순서로 반환한다.

## 예외 상황들

---

### 노선 추가
- [x] 인수로 넘어온 노선이 없다면, 예외를 반환한다.
- [x] 인수로 넘어온 역이 없는 역이라면 예외를 반환한다.
- [x] 인수로 넘어온 거리가 정수가 아니라면 예외를 반환한다.


### 노선에 역 추가
- [x] 인수로 넘어온 노선이 없다면, 예외를 반환한다.
- [x] 인수로 넘어온 역이 없는 역이라면 예외를 반환한다.
- [x] 인수로 넘어온 방향이 Up or Down 이 아니라면 예외를 반환한다. (대소문자 상관 x)
- [x] 인수로 넘어온 역 사이의 거리가 정수가 아니라면 예외를 반환한다.
- [x] 이미 노선에 두 개의 역이 포함되어 있다면 예외를 반환한다.
- [x] 노선에 두 개의 역이 모두 없다면 예외를 반환한다. 
- [x] 두 개의 역 사이에 역을 넣는 경우, 넘어온 거리가 두 개의 역 사이의 거리보다 같거나 크면 예외를 반환한다.

### 노선에 역 제거
- [x] 인수로 넘어온 노선이 없다면, 예외를 반환한다.
- [x] 인수로 넘어온 역이 같이 넘어온 노선에 없다면, 예외를 반환한다.

### 노선 조회
- [x] 인수로 넘어온 노선이 없다면, 예외를 반환한다.

### API 명세서

#### [Line API 명세서](http/line.http)

#### [Section API 명세서](http/section.http)