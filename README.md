# jwp-subway-path

### API 명세

- [x] 노선에 역 추가
  - `/sections/:lineId`
  - POST
  ```json
  {
    "startStation": "잠실나루역",
    "endStation": "잠실역",
    "distance": 10
  }
  ``` 

- [ ] 노선에 역 제거
  - `/sections/:lineId`
  - DELETE
  ```json
  {
    "station":"잠실역"
  }
  ```

### 기능 요구 사항

- [x] 노선에 역 등록
  - [x] 노선에 역을 등록할 때는 상행, 하행 상관 없이 등록 할 수 있다.
    - [x] 없는 노선에 역을 추가할 경우, 예외가 발생한다.
    - [x] 없는 역을 입력할 경우, 예외가 발생한다.
  - [x] startStation이 상행역이고, endStation이 하행역이다.
    - [x] 노선에 구간이 없으면 바로 추가된다.
    - [x] startStation과 endStation 둘 다 노선에 있거나, 없으면 예외가 발생해야 한다. 
    - [x] 노선에 역을 추가할 때, 두 역 중 반드시 하나만 해당 노선에 있어야 한다.
    - startStation가 이미 노선에 있는 역이라면, endStation가 startStation 기준 하행역으로 추가된다.
    - endStation가 이미 노선에 있는 역이라면, startStation가 endStation 기준 상행역으로 추가된다.
  - [x] 추가되는 구간의 길이가 기존 구간의 길이보다 작아야 한다.
  - [x] 구간의 길이는 0보다 커야 한다.

- [ ] 노선에 역 삭제
  - [ ] 노선에서 역을 삭제할 때는, 노선이 재배치 되어야 한다.
    - [ ] A-B-C-D 역이 있는 노선에서 C를 삭제하면 A-B-D 순으로 재배치 된다.
    - [ ] A-2km-B, B-3km-C, C-4km-D 일 경우, 구간의 길이가 B-7km-D가 되어야 한다.
  - [ ] 노선에 등록된 역이 2개일때, 하나의 역을 삭제하면 두 역이 삭제된다.
