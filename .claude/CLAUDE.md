답변은 한국어로 한다.

# 작성

KISS 원칙에 따라 코드와 문서를 작성한다.

문서, 주석 등 모든 텍스트는:
- 명시적으로 요청받은 경우에만 작성하며, 요청받더라도 반드시 최대한 간결하고 핵심만을 작성한다.
  - 예: "scoreList.sumOf { it.score } // 모든 점수를 합산" -> 주석 삭제 (불필요함)
- 최종 결과만을 남겨야 하며, 명시적으로 요청받은 경우를 제외하면 히스토리를 작성하지 않는다.
  - 예: "A 기술은 B 문제가 있어 사용하지 않도록 변경하였다" -> 삭제 (최종 상태가 아님)
- 어떻게 구현되었는지 생산자 관점에서 설명하지 말고, 그래서 무엇을 알아야 하는지 소비자 관점에서 기록한다.
  - 예: "@CurrentIdentity는 PassportFilter에서 X-Identity를 주입받아 IdentityId로 변환한다" -> "@CurrentIdentity로 컨트롤러에서 IdentityId를 주입받을 수 있다"

# 구현

아키텍처 패턴은 Controller - Usecase(Query/Command) - Service/Repository/Entity 구조를 활용한다.
Usecase에 CQRS 패턴을 적용한다.
- UsecaseQuery는 특정 형식 없이 자유로운 조회를 허용한다.
- UsecaseCommand는 DDD 원칙을 준수한다. 풍부한 엔티티와 적극적인 값 객체를 사용한다.
한 객체가 컨트롤러(복잡한 의존성을 가짐)인 동시에 도메인모델(복잡한 도메인 로직을 가짐)이 되지 않게 경계한다.
한 클래스 내부에서 성급한 메서드 공통화를 하지 않는다. 특히 한 줄짜리 본문을 가진 공통화 메서드는 가급적 피한다.
runCatching은 Error까지 잡으므로 사용하지 않는다. 예외 처리는 try-catch로 필요한 예외만 잡는다.
비즈니스 오류는 BusinessException을 상속해 정의한다.

모든 Entity의 Id는 UUIDv7 값을 가진 @Embeddable data class로 정의한다.

API 경로 접두사로 접근권한을 관리한다.
- /api: 사용자 식별 필수. 현재 사용자는 컨트롤러에서 `@CurrentIdentity identityId: IdentityId`로 주입받을 수 있다.
- /api-public: 사용자 식별 불필요.
- /internal: 외부 진입 차단

# 테스트

다음 순서에 따라 TDD를 준수하며 구현한다. 구현 커밋(feat:) 직전에 동일한 커밋 메시지를 가진 테스트 커밋(test:)이 CI에서 강제된다.
1. 구현하고자 하는 기능에 대해, 그 기능을 검사하는 테스트를 test:로 커밋한다.
  - 새로 추가된 테스트는 컴파일되지만 실패해야 한다
2. 테스트가 통과하는 구현을 feat:로 커밋한다.

테스트는 단위 테스트와 E2E 테스트로 나뉜다. 모든 테스트는 비즈니스 의미가 있어야 한다.
테스트는 given-when-then의 고전파 방식으로 작성하며, 컨트롤러, 배치, 컨슈머 등 실제 외부 시스템과 상호작용하는 지점을 테스트한다.
테스트 단언은 Kotest Assertions(shouldBe 등)를 사용한다.
Repository는 Fake를 사용한다. Client 등 외부 의존성도 가급적 Fake를 쓰되 불가피한 경우에만 Mock을 활용한다.

@SpringBootTest를 사용하지 않고 @TestSetup 어노테이션을 활용한다.
테스트에 필요한 Fake는 TestSetupBeans에 등록한다.
복잡한 given을 설정할 때는 TestHelper를 활용해 테스트 가독성을 올린다.

# CI/통합

검사 환경은 /ci에서 관리한다.

소스코드에 관련된 모든 검사는 ./gradlew check에 태스크로 추가한다.
소스코드 외 모든 검사는 /ci/ci.sh에서 관리한다.
detekt 게이트는 실험적이며, 실패할 경우 코드 수정뿐만 아니라 설정 조정도 고려한다.

현재는 GitHub Actions 등은 없다.

# CD/배포

코드 외 인프라(DB, secret 등)는 /cd에서 관리한다.
애플리케이션은 인프라에 의존하지 않는다. 인프라가 애플리케이션에 필요한 의존성을 주입한다.

App 프로필은 로컬용 test, 운영용 live 2개로 관리한다.
현재는 test만 운영한다.
live 환경은 아직 없다. 따라서 배포 파이프라인도 없다.
