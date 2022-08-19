package bssm.bsm.school.meister.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetMeisterPointDto extends FindStudentInfoDto {

    private String pw;
}
