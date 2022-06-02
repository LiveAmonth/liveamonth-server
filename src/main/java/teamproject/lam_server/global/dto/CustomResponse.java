package teamproject.lam_server.global.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import teamproject.lam_server.global.enumMapper.EnumClassConst;
import teamproject.lam_server.global.enumMapper.EnumMapper;
import teamproject.lam_server.global.exception.ErrorCode;
import teamproject.lam_server.global.exception.ValidationResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static teamproject.lam_server.global.constants.ResponseMessage.READ_CATEGORY;
import static teamproject.lam_server.global.constants.ResponseMessage.SUCCESS;
import static teamproject.lam_server.global.exception.ErrorCode.ARGUMENTS_NOT_VALID;

@Component
public class CustomResponse {
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class Result<T> {

        private int status;
        private LocalDateTime timeStamp;
        private String message;
        private T data;
        private String error;
        private String code;

        @Builder(builderClassName = "ResponseBuilder", builderMethodName = "ResponseBuilder")
        public Result(int status, LocalDateTime timeStamp, String message, T data, String code) {
            this.status = status;
            this.code = code;
            this.timeStamp = timeStamp;
            this.message = message;
            this.data = data;
        }

        @Builder(builderClassName = "ErrorBuilder", builderMethodName = "ErrorBuilder")
        private Result(ErrorCode errorCode) {
            this.timeStamp = LocalDateTime.now();
            this.status = errorCode.getStatus().value();
            this.error = errorCode.getStatus().name();
            this.code = errorCode.name();
            this.message = errorCode.getDetail();
        }

        @Builder(builderClassName = "ValidationBuilder", builderMethodName = "ValidationBuilder")
        private Result(ErrorCode errorCode, T data) {
            this.timeStamp = LocalDateTime.now();
            this.status = errorCode.getStatus().value();
            this.error = errorCode.getStatus().name();
            this.code = errorCode.name();
            this.message = errorCode.getDetail();
            this.data = data;
        }
    }

    public static <T> ResponseEntity<?> success(HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status.value())
                .body(Result.<T>ResponseBuilder()
                        .timeStamp(LocalDateTime.now())
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

    /**
     * 상태 리턴
     *
     * @return status
     */
    public static ResponseEntity<?> success() {
        return success(HttpStatus.OK, SUCCESS, Collections.emptyList());
    }

    /**
     * 메시지, 상태 리턴
     *
     * @return message, status
     */
    public static ResponseEntity<?> success(String message) {
        return success(HttpStatus.OK, message, Collections.emptyList());
    }

    /**
     * 데이터, 상태 리턴
     *
     * @return data, status
     */
    public static <T> ResponseEntity<?> success(T data) {
        return success(HttpStatus.OK, SUCCESS, data);
    }

    /**
     * 데이터, 메시지 리턴
     *
     * @return data, message
     */
    public static <T> ResponseEntity<?> success(String message, T data) {
        return success(HttpStatus.OK, message, data);
    }

    public static ResponseEntity<?> getCategorySuccess(EnumMapper enumMapper, EnumClassConst category) {
        return CustomResponse.success(
                category.getValue() + READ_CATEGORY,
                enumMapper.get(category.getClassName())
        );
    }

    public static ResponseEntity<Object> fail(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(Result.ErrorBuilder()
                        .errorCode(errorCode)
                        .build()
                );
    }

    public static ResponseEntity<Object> validationFail(List<ValidationResponse> detail) {
        return ResponseEntity
                .status(ARGUMENTS_NOT_VALID.getStatus())
                .body(Result.ValidationBuilder()
                        .errorCode(ARGUMENTS_NOT_VALID)
                        .data(detail)
                        .build()
                );

    }
}
