package teamproject.lam_server.global.exception;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import teamproject.lam_server.global.dto.CustomResponse;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static teamproject.lam_server.global.exception.ErrorCode.*;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<?> handleCustomExceptions(CustomException e) {
        return CustomResponse.fail(e.getErrorCode());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return CustomResponse.fail(ENTITY_NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<?> handleIllegalArgumentExceptions() {
        return CustomResponse.fail(ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public final ResponseEntity<?> handleDataException() {
        return CustomResponse.fail(DUPLICATED_RESOURCE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return CustomResponse.validationFail(createValidationDetails(ex));
    }

    private List<ValidationResponse> createValidationDetails(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream()
                .map(fieldError -> new ValidationResponse(fieldError, messageSource))
                .collect(Collectors.toList());
    }

}
