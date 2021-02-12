package com.kuke.videomeeting.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ListResult<T> extends Result {
    private List<T> data;
}
