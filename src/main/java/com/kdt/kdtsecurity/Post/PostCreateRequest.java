package com.kdt.kdtsecurity.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest (
        @NotNull Long memberId,
        @NotBlank @Size(max=200) String title,
        @NotBlank @Size(max= 1000) String body
){
}