package bssm.bsm.domain.board.emoticon.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EmoticonResponse {

    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<EmoticonItemResponse> items;
}
