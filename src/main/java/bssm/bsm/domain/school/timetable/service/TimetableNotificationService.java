package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushMsgDto;
import bssm.bsm.domain.webpush.service.SendWebPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableNotificationService {

    private final UserFacade userFacade;
    private final SendWebPushService webPushUtil;
    private final WebPushRepository webPushRepository;

    @Value("${env.timetable.url}")
    private String TIMETABLE_ACCESS_URL;

    public void sendChangeTimetableNotification(TimetableManage timetableManage) throws JsonProcessingException {
        WebPushMsgDto dto = WebPushMsgDto.create(
                "시간표가 " + timetableManage.getName() + "(으)로 변경되었습니다",
                "여기를 클릭하여 변경된 시간표를 확인해보세요",
                TIMETABLE_ACCESS_URL);
        List<User> userList = userFacade.findAllByGradeAndClassNo(timetableManage.getGrade(), timetableManage.getClassNo());
        List<WebPush> webPushList = webPushRepository.findAllByUserIn(userList);

        webPushUtil.sendNotificationToAll(webPushList, dto);
    }

}
