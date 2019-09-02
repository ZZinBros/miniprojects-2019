package com.woowacourse.zzinbros.comment.web.support;

import com.woowacourse.zzinbros.comment.dto.CommentResponseDto;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@RestControllerAdvice
public class CommentControllerExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = getLogger(CommentControllerExceptionAdvice.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommentResponseDto> handleResponseStatusException(ResponseStatusException exception) {
        LOGGER.info("HTTP status: {}", exception.getStatus());
        LOGGER.info("{}", exception.getClass().getSimpleName());
        HttpStatus status = resolveAnnotatedResponseStatus(exception);
        return new ResponseEntity<>(new CommentResponseDto(exception), status);
    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
        final ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (Objects.nonNull(annotation)) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
