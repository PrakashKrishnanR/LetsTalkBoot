package com.app.expd.models;

import com.app.expd.exceptions.LetsTalkException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;


public enum VoteType {
    UPVOTE(1),
    DOWNVOTE(-1);

    private int direction;

    VoteType(int direction){

    }
    public static VoteType lookUp(Integer direction){
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(()-> new LetsTalkException("Invalid VoteType"));
    }
    public Integer getDirection(){
        return direction;
    }
}
