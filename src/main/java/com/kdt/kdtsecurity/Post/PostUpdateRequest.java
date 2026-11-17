package com.kdt.kdtsecurity.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @NotBlank @Size(max=200) String title,
        @NotBlank @Size(max = 10000) String body
) {
}