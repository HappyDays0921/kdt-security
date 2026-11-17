package com.kdt.kdtsecurity.Post;

public record PostResponse(
        Long id,
        String title,
        String body,
        Long memberId,
        String loginId
) {
    public static PostResponse from(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getMember().getId(),
                post.getMember().getLoginId()
        );

    }
}
