package com.minhvu.monolithic.exception;

import com.minhvu.monolithic.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File size exceeds the maximum limit is 20 MB! Please upload a smaller file.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response handleNotFoundException(NotFoundException exception) {
        return new Response(404, exception.getLocalizedMessage());
    }

    @ExceptionHandler({BadRequestException.class, BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Response handleRequestException(Exception exception) {
        return new Response(400, exception.getLocalizedMessage());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Response handleUnauthorizedException(Exception exception) {
        return new Response(401, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Response forbiddenException(ForbiddenException exception) {
        return new Response(403, exception.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleAllException(Exception exception) {
        return new Response(500, exception.getLocalizedMessage());
    }
}
