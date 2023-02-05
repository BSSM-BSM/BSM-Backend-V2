package bssm.bsm.domain.board.emoticon.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EmoticonRes {

    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<EmoticonItemRes> items;
}
