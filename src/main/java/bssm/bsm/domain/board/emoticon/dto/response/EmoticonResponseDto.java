package bssm.bsm.domain.board.emoticon.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EmoticonResponseDto {

    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<EmoticonItemResponseDto> items;
}
