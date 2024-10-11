package bssm.bsm.domain.board.lostfound.presentation.dto.req;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;

@Getter
public class UpdateProcessReq {

    @NotNull
    private Process process;
}
