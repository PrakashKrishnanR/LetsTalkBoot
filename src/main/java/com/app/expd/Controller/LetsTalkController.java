package com.app.expd.Controller;

import com.app.expd.dto.SubTalkDto;
import com.app.expd.service.SubtalkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subtalk")
@AllArgsConstructor
@Slf4j
public class LetsTalkController {

    private final SubtalkService subtalkService;

    @PostMapping
    public ResponseEntity<SubTalkDto> createSubtalk(@Valid @RequestBody SubTalkDto subTalkDto){

       return ResponseEntity.status(HttpStatus.CREATED)
        .body(subtalkService.saveSubtalk(subTalkDto));
    }

    @GetMapping
    public ResponseEntity<List<SubTalkDto>> getAllSubtalks(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subtalkService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTalkDto> getAllSubtalkById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subtalkService.getSubtalkById(id));
    }
    @DeleteMapping("/deleteBySubId/{subid}")
    public ResponseEntity<Void> deleteBySubtalkID(@PathVariable Long subid){

        subtalkService.deleteBysubId(subid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
