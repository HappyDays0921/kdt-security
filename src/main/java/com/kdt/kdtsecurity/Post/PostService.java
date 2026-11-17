package com.kdt.kdtsecurity.Post;

import com.kdt.kdtsecurity.Common.NotFoundException;
import com.kdt.kdtsecurity.Member.Member;
import com.kdt.kdtsecurity.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> list(){
        return this.postRepository.findAll(Sort.by("id")
                .ascending()).stream().map(post -> PostResponse.from(post)).toList();
    }

    @Transactional
    public PostResponse create(PostCreateRequest request){
        Member member = this.memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException("member missing"));
        Post post = new Post();
        post.setMember(member);
        post.setTitle(request.title());
        post.setBody(request.body());

        return toResponse(postRepository.save(post));
    }
    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request){
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("post missing"));
        post.setTitle(request.title());
        post.setBody(request.body());
        return PostResponse.from(post);
    }
    @Transactional
    public void delete(Long id){
        Post post = this.postRepository.findById(id)
                .orElseThrow(()->new NotFoundException("post missing"));
        this.postRepository.delete(post);
    }

    private PostResponse toResponse(Post post){
        return new PostResponse(post.getId(),post.getTitle(), post.getBody(),
                post.getMember().getId(),post.getMember().getLoginId());
    }

}
