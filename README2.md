# jwp-subway-path

## 비즈니스 로직
- 노선에 역 등록
  - [] 역 최초 등록시, 두 역을 동시에 등록해야 한다
    - [] 상행역과 하행역은 다른 역이다
    - [] 거리는 1이상의 양의 정수이다
  - [] 기존 역에 새로운 역 등록하기
    - [] 기존역/ 새로운역 / 기존역을 기준으로 새로운 역의 위치(왼쪽, 오른쪽) / 거리
    - [] 상행역과 하행역은 다른 역이다
    - [] 거리는 1이상의 양의 정수이다
    
    - [] 기존역은 노선에 존재하는 역이어야 한다
    - [] 새로운 역은 노선에 존재하는 역이면 안된다
    - [] 하나의 역은 여러 노선에 존재할 수 있다
    - [] 기존 구간 사이에 새로운 역을 등록하는 경우, 새로 추가하는 역과의 구간은 기존 역들 사이의 구간보다 작아야 한다
    
- 노선에 역 제거
  - [] 노선 역이 2개만 있을 경우
    - [] 하나의 역 제거시 두 역 모두 제거된다.
  - [] 기존 구간 사이의 역 제거하는 경우
    - [] 제거하는 역 오른쪽, 왼쪽 거리를 더한다.

1. Line
   - Name
   - Color
   - Sections
2. Section
   - Station upStation
   - Station downStation
   - Distance
3. Station
   - Name

4. Direction (enum)
   - 왼쪽, 오른쪽
Subway
    - lines
Lines
    - List<Line>

---
## API 명세
1. Line API
   - 모든 노선 조회하기 : GET /lines                
   - 특정 노선 조회하기 (노선 정보, 모든 역) : GET /lines/{lineId}       
   - 새 노선 등록하기 : POST /lines               
   - 특정 노선 정보 수정하기 : PUT /lines/{lineId}      
   - 특정 노선 삭제하기 : DELETE /lines/{lineId}    
    
   - 특정 노선에 역 추가하기 : POST /lines/{lineId}/stations/{stationId} 
   - 특정 노선에 역 삭제하기 : DELETE  /lines/{lineId}/stations/{stationId} 

2. Station API
   문제 : 역 등록할 때, 특정 노선의 역 등록하기로 가야함
   문제 2 : 역 수정, 삭제할 때, 여러 역
   문제 3 : 같은 역 사이에도 노선이 다르면 거리가 존재함?
      - 모든 역 조회하기
      - 특정 역 조회하기 (id, )
      - 새 역 등록하기
      - 특정 역 정보 수정하기
      - 특정 역 삭제하기
