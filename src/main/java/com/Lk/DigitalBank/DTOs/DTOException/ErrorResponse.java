package com.Lk.DigitalBank.DTOs.DTOException;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime momentError,
        Integer status,
        String erroDescription,
        String menssage,
        String path
) {
}
