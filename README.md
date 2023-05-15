# jwp-subway-path

### API

#### /stations

- [x] 역 등록
    - [x] POST, /stations
    - [x] 요청 포맷
        - [x] 역 이름
    - [x] 응답 포맷
        - [x] Locations : /stations/{id}
- [x] 역 제거
    - [x] DELETE, /stations/{id}
    - [x] 요청 포맷
        - [x] 역 id

#### /lines

- [x] 노선 등록
    - [x] POST, /lines
    - [x] 요청 포맷
        - [x] 노선 이름, 노선 색깔
    - [x] 응답 포맷
        - [x] Locations : /lines/{id}
- [x] 조회
    - [x] GET, /lines
    - [x] 응답
        - [x] List<List<Station>>
- [x] 노선별 조회
    - [x] GET, /lines?id=

#### /lines/{lineId}/stations

- [x] 노선에 역 등록
    - [x] POST, /lines/{lineId}/stations
    - [x] 요청 포맷
        - [x] 노선 id, 상행 역 id, 하행 역 id, 거리
- [x] 노선에서 역 삭제
    - [x] DELETE, /lines/{lineId}/stations/{stationId}
- [x] 노선별 역 조회
    - [x] GET, /lines/{lineId}/stations

### Domain

- Station
    - name
- Line
    - name
    - color
- Section
    - line
    - preStation
    - Station
    - distance
- Subway
    - Map\<Station, List\<Section>>

### 기능 요구사항

#### 노선에 역 등록

- [x] 요청 시 거리는 양의 정수여야 한다
- [x] section 테이블에 요청된 노선에 해당하는 구간이 있는지 확인한다
    - [x] 노선에 해당하는 구간이 있으면 예외 처리
    - [x] 노선에 해당하는 구간이 없으면
        - [x] 노선이 비어있으면 구간 등록
        - [x] preStation 이 기존 노선에 있고 Station 이 기존 노선에 없을 때
            - [x] 기존 노선에서 preStation 의 다음 역을 구한다
                - [x] 다음 역이 있으면
                    - [x] 기존 구간의 거리보다 요청 구간의 거리가 크거나 같으면 예외
                    - [x] 작으면 구간 등록 후 거리 재배치
                - [x] 다음 역이 없으면
                    - [x] 구간 등록(하행 종점)
        - [x] preStation 이 기존 노선에 없고 Station 이 기존 노선에 있을 때
            - [x] 기존 노선에서 Station 의 이전 역을 구한다
                - [x] 이전 역이 있으면
                    - [x] 기존 구간의 거리보다 요청 구간의 거리가 크거나 같으면 예외
                    - [x] 작으면 구간 등록 후 거리 재배치
                - [x] 이전 역이 없으면
                    - [x] 구간 등록(상행 종점)
        - [x] 그 외는 예외 처리

#### 노선에 역 제거

- [x] 제거할 역이 노선에 존재하지 않으면 예외
- [x] 제거할 역이 상행 종점이거나 하행 종점이면 거리 재배치 필요 x
- [x] 제거할 역이 중간 역이면
    - [x] 제거할 역의 이전 역과 다음 역 구하기
    - [x] 제거할 역과 그 이전 역 사이의 거리 및 제거할 역과 그 다음 역 사이의 거리를 구하기
    - [x] 이전역과 다음역의 구간 추가
- [x] 제거할 역이 PreStation 이거나 Station 인 구간 모두 삭제

#### 노선별 역 조회

- [ ] 조회할 노선의 List\<Section\> 을 만든다
- [ ] pivot Station 을 하나 잡는다
    - [ ] List\<Section\> 을 순회하면서 pivot 의 다음 역들을 List\<Station\> 에 추가한다
    - [ ] List\<Section\> 을 순회하면서 pivot 의 이전 역들을 List\<Station\> 에 추가한다
    - [ ] 두 리스트를 합친다
