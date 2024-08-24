package com.minhvu.friend.exception;

import com.minhvu.friend.exception.response.Response;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MyExceptionHandler {

    @ResponseStatus(value = HttpStatus.OK)
    public Response handleOk(NotFoundException exception) {
        return new Response(exception.getLocalizedMessage());
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

    @ExceptionHandler(FriendRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Response handleFriendRequestException(FriendRequestException exception) {
        return new Response(400, exception.getLocalizedMessage());
    }
}
