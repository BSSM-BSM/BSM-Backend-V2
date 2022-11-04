package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSendDto;
import bssm.bsm.global.utils.WebPushUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TimetableNotification {

    private final UserFacade userFacade;
    private final WebPushUtil webPushUtil;
    private final WebPushRepository webPushRepository;

    @Value("${env.timetable.url}")
    private String TIMETABLE_ACCESS_URL;

    public void sendChangeTimetableNotification(TimetableManage manage) throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("시간표가 " + manage.getName() + "(으)로 변경되었습니다")
                .body("여기를 클릭하여 변경된 시간표를 확인해보세요")
                .link(TIMETABLE_ACCESS_URL)
                .build();
        List<User> userList = userFacade.findAllByGradeAndClassNo(manage.getGrade(), manage.getClassNo());
        List<WebPush> webPushList = webPushRepository.findAllByUserIn(userList);

        webPushUtil.sendNotificationToAll(webPushList, dto);
    }

}
