package com.yahve.eventnotificator.dto;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) { }
