package com.smartassessment.assessment_service.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PassingCriteriaRequest {

    @NotNull(message = "Passing percentage is required")
    @DecimalMin(value = "0.0", message = "Passing percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Passing percentage must be <= 100")
    private BigDecimal passingPercentage;

    private Integer passingMarks;
}
