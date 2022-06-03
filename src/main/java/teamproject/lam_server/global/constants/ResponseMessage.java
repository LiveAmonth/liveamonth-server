package teamproject.lam_server.global.constants;

public abstract class ResponseMessage {

    public static final String SUCCESS = "성공";

    /**
     * Domain: Member
     */
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGOUT_SUCCESS = "로그아웃 성공";
    public static final String REISSUE_TOKEN_SUCCESS = "토큰 재발급 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_MEMBER = "회원 정보 조회 성공";
    public static final String CREATED_MEMBER = "회원 가입 성공";
    public static final String CREATED_ADMIN = "관리자 계정 생성 성송";
    public static final String UPDATE_MEMBER = "회원 정보 수정 성공";
    public static final String DELETE_MEMBER = "회원 탈퇴 성공";

    /**
     * Domain: Schedule
     */
    public static final String READ_SCHEDULE = "상영일정 조회 성공";
    public static final String READ_SCHEDULE_SCREEN = "상영일정(상영관) 조회 성공";
    public static final String CREATE_SCHEDULE = "상영일정 저장 성공";
    public static final String READ_SCHEDULE_SEATS = "상영 일정 좌석 조회 성공";
    public static final String DELETE_SCHEDULE = "상영일정 삭제 성공";

    /**
     * Domain: City
     */
    public static final String CREATE_CITY = "도시정보 저장 성송";
    public static final String READ_CITY = "도시정보 조회 성공";
    public static final String UPDATE_CITY = "도시정보 업데이트 성공";
    public static final String DELETE_CITY = "도시정보 삭제 성공";



    public static final String CREATE_MOVIE = "영화 저장 성공";
    public static final String UPDATE_MOVIE = "영화 업데이트 성공";
    public static final String DELETE_MOVIE = "영화 삭제 성공";
    public static final String READ_MOVIE = "영화 조회 성공";
    public static final String READ_MOVIE_CONDITION = "영화 조건 조회 성공";
    public static final String READ_UNRELEASED_MOVIE = "상영예정 영화 조회 성공";
    public static final String READ_MOVIE_SORT = "영화 정렬 조회 성공";

    public static final String CREATE_REVIEW = "리뷰 저장 성공";
    public static final String UPDATE_REVIEW = "리뷰 업데이트 성공";
    public static final String DELETE_REVIEW = "리뷰 삭제 성공";
    public static final String READ_REVIEW = "모든 리뷰 조회 성공";


    /**
     * Category
     */
    public static final String READ_CATEGORY = " 카테고리 조회 성공";

}
