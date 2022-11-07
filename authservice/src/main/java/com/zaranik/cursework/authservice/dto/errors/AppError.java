package com.zaranik.cursework.authservice.dto.errors;

import java.time.LocalDateTime;

public record AppError(String description, LocalDateTime causedTime) {

}
