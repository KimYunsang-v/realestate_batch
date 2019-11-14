# View동산 Batch Server

전국의 모든 부동산 정보를 가져오기 위한 프로젝트

## Spring batch Framework

대용량 데이터 처리를 위해 Spring batch framework 사용

### 구현 사항

#### partitioner

- 총 6개의 URL의 데이터를 가져오기 위해 URL별 파티셔닝 기능 구현

#### ItemReader

- 파티셔닝된 URL에 Job parameter로 받은 연도 + 월 데이터와 전국의 지역코드를 조합하여 데이터 요청

- 공공데이터 포탈에서 응답으로 보낸 xml파일을 태그별로 분류하여 객체화

#### ItemWriter

- 전달 받은 데이터를 비즈니스 로직을 통해 가공 후 JPA를 사용하여 데이터베이스에 저장

