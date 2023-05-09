# jwp-subway-path

---

## 기능 구현 목록

- [x] 테이블 설계
    - [x] station(station_id, name)
    - [x] line(line_id, color, name)
    - [x] section(section_id, sectionA_id, sectionB_id)
    - [x] station_in_line(station_point_id, section_id, line_id)
    - [x] station_end_point (id, upStation_id, downStation_id)

- [ ] 테스트 작성
    - [ ] 통합 테스트로 전체 플로우 테스트
    - [ ] TDD 단위 테스트
    - [ ] 계층 통합 테스트

- [ ] 도메인 로직 구현
    - [ ] Graph 구현

## API 명세서

## Line Controller

### POST(/lines)

지하철 노선을 만든다.

**request**

```json
{
  "name": "name",
  "color": "color"
}
```

**response**

```json
{
  "id": 1,
  "name": "name",
  "color": "color"
}
```

---

### GET(/lines)

지하철 전체 노선을 가져온다.

**request**

x

**response**

```json
[
  {
    "id": 1,
    "name": "name",
    "color": "color"
  },
  {
    "id": 2,
    "name": "name2",
    "color": "color2"
  }
]
```

---

### GET(/lines/{id})

id값에 해당하는 노선을 가져온다.

**request**

param : “id”

**response**

```json
{
  "id": 1,
  "name": "name",
  "color": "color"
}
```

---

### PUT(/lines/{id})

id값에 해당하는 노선을 수정한다.

**request**

param : “id”

LineRequest

```json
{
  "name": "nameEdit",
  "color": "colorEdit"
}
```

**response**

x

---

### DELETE(/lines/{id})

id값에 해당하는 노선을 삭제한다.

**request**

param : “id”

**response**

x

---

---

## Station Controller

### POST(/stations)

지하철 역을 만든다.

**request**

```json
{
  "name": "name"
}
```

**response**

```json
{
  "id": 1,
  "name": "name"
}
```

---

### GET(/stations)

지하철 전체 역을 가져온다.

**request**

x

**response**

```json
[
  {
    "id": 1,
    "name": "name"
  },
  {
    "id": 2,
    "name": "name2"
  }
]
```

---

### GET(/stations/{id})

id값에 해당하는 역을 가져온다.

**request**

param : “id”

**response**

```json
{
  "id": 1,
  "name": "name"
}
```

---

### PUT(/stations/{id})

id값에 해당하는 역을 수정한다.

**request**

param : “id”

LineRequest

```json
{
  "name": "nameEdit"
}
```

**response**

x

---

### DELETE(/stations/{id})

id값에 해당하는 역을 삭제한다.

**request**

param : “id”

**response**

x
