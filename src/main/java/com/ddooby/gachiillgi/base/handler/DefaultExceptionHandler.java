package com.ddooby.gachiillgi.base.handler;

import com.ddooby.gachiillgi.base.enums.exception.ErrorCodeEnum;
import com.ddooby.gachiillgi.base.exception.BizException;
import com.ddooby.gachiillgi.interfaces.dto.DefaultErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(OK)
    @ExceptionHandler(Exception.class)
    public DefaultErrorResponseDTO defaultErrorHandler (Exception e) {

        log.error("## Biz exception response ##");
        log.error("{}" , e.toString());

        boolean isBizException = e instanceof BizException;
        boolean isLoginException = e instanceof InternalAuthenticationServiceException;
        String errorCode = null;
        String errorLongMessage;
        String errorShortMessage;

        if (isBizException) {
            BizException bizException = (BizException) e;
            ErrorCodeEnum errorCodeEnum = bizException.getCode();
            if (errorCodeEnum == null) {
                errorShortMessage = e.getMessage();
                errorLongMessage = e.getMessage();
            } else {
                errorCode = errorCodeEnum.getName();
                errorShortMessage = errorCodeEnum.getShortMessage();
                errorLongMessage = errorCodeEnum.getLongMessage();

                if (hasErrorArgument(bizException)) {
                    Map<String, String> argsMap = bizException.getArgs();
                    for (Map.Entry<String, String> entry : argsMap.entrySet()) {
                        log.debug(entry.toString());
                        String argsKey = "{" + entry.getKey() + "}";
                        errorLongMessage = errorLongMessage.replace(argsKey, entry.getValue());
                        errorShortMessage = errorShortMessage.replace(argsKey, entry.getValue());
                    }
                }
            }
            return DefaultErrorResponseDTO.error(errorCode, errorLongMessage, errorShortMessage);
        } else if (isLoginException) {
            errorShortMessage = e.getMessage();
            errorLongMessage = e.getMessage();
            return DefaultErrorResponseDTO.error(errorLongMessage, errorShortMessage);
        }else {
            errorLongMessage = e.getMessage();
            errorShortMessage = "system error";
            return DefaultErrorResponseDTO.systemError(errorLongMessage, errorShortMessage);
        }
    }

    private boolean hasErrorArgument(BizException bizException) {
        return bizException.getArgs() != null && !bizException.getArgs().isEmpty();
    }
}