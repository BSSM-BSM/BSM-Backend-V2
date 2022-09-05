package bssm.bsm.domain.school.meister.dto.response;

import bssm.bsm.domain.school.meister.type.MeisterInfoResultType;
import bssm.bsm.domain.user.entities.Student;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeisterRankingDto implements Comparable<MeisterRankingDto> {

    private Float score;
    private Integer positivePoint;
    private Integer negativePoint;
    private LocalDateTime lastUpdate;
    private MeisterStudentResponseDto student;
    private MeisterInfoResultType result;

    @Override
    public int compareTo(@NotNull MeisterRankingDto ranking) {
        if (score == null) return 1;
        if (ranking.score == null || ranking.score < score) {
            return -1;
        } else if (ranking.score > score) {
            return 1;
        }
        return 0;
    }
}
