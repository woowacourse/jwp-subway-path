# jwp-subway-path

# 기능요구사항

- [x] 노선에 역 등록 API 구현
  - [x] 노선 정보, 역 두 개, 역 사이의 거리를 입력 받아야한다.
  - [x] 노선에 등록되는 역의 위치는 자유롭게 지정할 수 있어야 한다.
  - [x] 하나의 역은 여러 노선에 등록이 될 수 있다.(환승)
  - [x] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 한다.
    - [x] 역 사이에 역을 등록할 때 거리가 조절되어야 한다.
    - [x] 역 사이에 역을 등록할 때 기존 역 사이의 거리보다 크거나 같아서는 안된다.
- [x] 노선에 역 제거 API 구현
  - [x] 역 사이의 역을 삭제할 때 거리랑 역의 순서가 재배치 되어야 한다.
  - [x] 등록된 역이 두 개인 경우에 하나를 제거하면 모두 제거되어야 한다.
- [x] 노선 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
```json
{
  "id": 1L,
  "name": "노선이름",
  "color": "노선색깔",
  "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
}
```
- [x] 노선 목록 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
```json
[
  {
    "id": 1L,
    "name": "노선이름1",
    "color": "노선색깔1",
    "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
  },
  {
    "id": 2L,
    "name": "노선이름2",
    "color": "노선색깔2",
    "stations": [{"id": 1L, "name": "역이름1"},{"id": 2L, "name": "역이름2"}]
  }
]
```
- [x] 노선을 최초 등록 할 때는 두 개의 역을 등록해야 한다.


## 도메인 설계

- Line(노선)
  - id
  - String name
  - String color

- Sections
  - List<Section> sections 

- Section(구간)
  - Long downStationId
  - Long upStationId
  - Long lineId
  - int distance

- Station(역)
  - id ID
  - String 이름


## API 문서
- [API 구현 목록](https://documenter.getpostman.com/view/22400395/2s93ebTAmd#85e97606-4a86-4627-8ee1-e7feaf8d80b5)
