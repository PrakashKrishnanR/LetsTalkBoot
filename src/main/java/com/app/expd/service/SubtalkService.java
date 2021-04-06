package com.app.expd.service;

import com.app.expd.dto.SubTalkDto;
import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.Comment;
import com.app.expd.models.Post;
import com.app.expd.models.Roles;
import com.app.expd.models.SubTalk;
import com.app.expd.repository.SubTalkRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class SubtalkService {

    private final SubTalkRepository subTalkRepository;
    private final PostService postService;

    private final AuthService authService;
    @Transactional
    public SubTalkDto saveSubtalk(SubTalkDto subTalkDto){

       SubTalk subTalk = subTalkRepository.save(mapDtoToSubtalk(subTalkDto));
       subTalkDto.setId(subTalk.getSubtalkID());
       return subTalkDto;
    }

    public SubTalk mapDtoToSubtalk(SubTalkDto subTalkDto){

             return SubTalk.builder().commName(subTalkDto.getName())
                        .description(subTalkDto.getDescription())
                        .createdDate(Instant.now())
                        .user(this.authService.getCurrentUser())
                        .build();
    }
    @Transactional(readOnly = true)
    public List<SubTalkDto> getAll(){
           return subTalkRepository.findAll()
                    .stream()
                    .map(this::maptoDto)
                    .collect(Collectors.toList());
    }

    private SubTalkDto maptoDto(SubTalk subTalk) {
        Boolean deleteEnabled = this.authService.getCurrentUser()
                .getRole().equals(Roles.ADMIN);

        return SubTalkDto.builder().name(subTalk.getCommName())
                .description(subTalk.getDescription())
                .deleteEnabled(deleteEnabled)
                .noOfPosts(subTalk.getPosts().size())
                .id(subTalk.getSubtalkID())
                .build();
    }

    public SubTalkDto getSubtalkById(Long id) {
        SubTalk subTalk = subTalkRepository.findBySubtalkID(id).orElseThrow(()->
                new LetsTalkException(" No Talks found for the ID : "+ id)
        );

        return maptoDto(subTalk);
    }

    public void deleteBysubId(Long subid) {

        SubTalk subTalk = subTalkRepository.findBySubtalkID(subid).
                orElseThrow(() -> new LetsTalkException("Not a Valid Subject ID !!"));

        //detach user and delete subtalk
        subTalk.setUser(null);
        this.subTalkRepository.delete(subTalk);
    }
}
