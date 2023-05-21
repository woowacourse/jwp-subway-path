# jwp-subway-path

## API 기능 요구사항 구현하기

1. 노선에 역 등록 API 신규 구현
    1. 노선 신규 등록
       :ux를 고려했을 때, 신규 노선을 등록할 때, 초기 역을 함께 설정하는 것이 적합하다고 판단함
         ```
         POST /lines
         {
             "name": "2호선"
             "color": "Green"
         }
         ```

    2. 기존 노선에 역 등록
       ```
         POST /lines/{lineId}/stations
         {
             "upStation": "낙성대id"
             "downStation": "사당id"
             "distance": 1
         }
         ```
        - 빈 노선인 경우,두 역을 동시에 등록해야 한다
        - 빈노선이 아닌 경우, 한 역은 해당 노선에 존재하는 역이어야 하고, 다른 역은 노선에 등록되지 않은 역이어야 한다 
        - 거리 정보는 양의 정수로 제한한다

2. 노선에 역 제거 API 신규 구현
    1. 노선 삭제
       :ux를 고려했을 때, 신규 노선을 등록할 때, 초기 역을 함께 설정하는 것이 적합하다고 판단함
         ```
         DELETE /lines/{lineId}
         ```

    2. 노선에 역 삭제
       ```
         DELETE /lines/{lineId}/stations/{stationId}
       ```
       - 
3. 노선 조회 API 수정
    1. 노선 조회
        ```
          GET /lines/{lineId}
        ```
    
    2. 노선 목록 조회
        ```
          GET /lines
        ```
    
4. 테스트 하기
 - 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 합니다. 
   - 하나의 역은 여러 노선에 등록이 될 수 있습니다.
     (1호선: A-B-C, 2호선: Z-B-D 처럼 B역은 두개 이상의 노선에 포함될 수 있습니다.)
   - 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 합니다.
     A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
     B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.
   - 노선 가운데 역이 등록 될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다. 
     A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
     B-C역의 거리가 3km인 경우 B-D 거리는 3km보다 적어야 합니다.
   - 노선에 역 제거 테스트
     - 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 합니다.
        A-B-C-D 역이 있는 노선에서 C역이 제거되는 경우 A-B-D 순으로 재배치됩니다.
     - 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야 합니다. 
       A-B가 2km, B-C가 3km, C-D가 4km인 경우 C역이 제거되면 B-D 거리가 7km가 되어야 합니다.
     - 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 합니다. 
       A-B 노선에서 B를 제거할 때 거리 정보를 포함할 수 없기 때문에 두 역 모두 제거되어야 합니다.


## 도메인 설계하기
### Line
- 필드
  - Long id
  - String name => LineName name (-선으로 끝나야함) endwith
  - String color => Color color
  - Sections
- 기능
  - 역 목록 조회하기

### Sections
- 필드
  - List<Section>
- 기능
  - 역 상행-하행 순으로 정렬하기
  - 역 목록 조회하기

### Section
- 필드
  - Station upStation
  - Station downStation
  - Integer distance => Distance distance

### Station
- 필드
  - Long id
  - String name => StaionName name (-역으로 끝나야함)  endwith
