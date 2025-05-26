package org.my_tma_test_app.tmaz.security.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import lombok.Value;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiErrorDto {

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final String requestGuid;

    public static ResponseEntity<RestApiErrorDto> toResponse(HttpStatusCode responseStatus, Throwable throwable, String requestGuid) {
        return toResponse(responseStatus, throwable, requestGuid);
    }

    public static ResponseEntity<RestApiErrorDto> toResponse(HttpStatusCode responseStatus, String message, String requestGuid) {
        return ResponseEntity.status(responseStatus)
                .body(new RestApiErrorDto(LocalDateTime.now(), responseStatus.value(), message, requestGuid));
    }
}
