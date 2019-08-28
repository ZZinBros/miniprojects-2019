package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.dto.CommentResponseDto;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@RestControllerAdvice
public class CommentControllerExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger LOG = getLogger(CommentControllerExceptionAdvice.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommentResponseDto> handleResponseStatusException(final ResponseStatusException exception) {
        LOG.info("HTTP status: {}", exception.getStatus());
        LOG.info("{}", exception.getClass().getSimpleName());
        final HttpStatus status = resolveAnnotatedResponseStatus(exception);
        return new ResponseEntity<>(new CommentResponseDto(exception), status);
    }

    // https://stackoverflow.com/a/51358263
    private HttpStatus resolveAnnotatedResponseStatus(final Exception exception) {
        final ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
