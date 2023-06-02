package bssm.bsm.domain.board.lostfound.presentation.dto.req;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateProcessReq {
    @NotBlank
    private Process process;
}
