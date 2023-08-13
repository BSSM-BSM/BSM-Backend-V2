package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageRepository;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import bssm.bsm.domain.school.timetable.presentation.dto.req.ApplyTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.CreateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.FindTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableListReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableListRes;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableManageRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableManageService {

    private final TimetableProvider timetableProvider;
    private final TimetableManageProvider timetableManageProvider;

    private final TimetableManageRepository timetableManageRepository;

    private final TimetableNotificationService timetableNotification;

    public List<TimetableManageRes> findManageList(@Valid FindTimetableReq req) {
        return timetableManageProvider.findManageList(req.getGrade(), req.getClassNo()).stream()
                .map(TimetableManageRes::create)
                .toList();
    }

    @Transactional
    public void createTimetable(CreateTimetableReq req) {
        TimetableManage timetableManage = TimetableManage.create(
                req.getName(),
                req.getType(),
                req.getGrade(),
                req.getClassNo());
        timetableManageRepository.save(timetableManage);
    }

    @Transactional
    public void applyTimetable(ApplyTimetableReq req) throws JsonProcessingException {
        TimetableManage timetableManage = timetableManageProvider.findManage(req.getId());
        Timetable timetable = timetableProvider.findTimetable(timetableManage.getGrade(), timetableManage.getClassNo());

        List<TimetableItem> newItemList = timetableManage.getItems().stream()
                .map(item -> item.toTimetableItem(timetable))
                .toList();
        timetable.upsertItems(newItemList);

        timetableNotification.sendChangeTimetableNotification(timetableManage);
    }

    @Transactional
    public void deleteTimetable(long id) {
        TimetableManage timetableManage = timetableManageProvider.findManage(id);
        timetableManageRepository.delete(timetableManage);
    }

    @Transactional
    public void updateTimetable(UpdateTimetableReq req) {
        TimetableManage timetableManage = timetableManageProvider.findManage(req.getId());
        timetableManage.update(req.getName(), req.getType());
        timetableManageRepository.save(timetableManage);
    }

    @Transactional
    public void updateTimetableList(UpdateTimetableListReq req) {
        TimetableManage timetableManage = timetableManageProvider.findManage(req.getId());
        List<TimetableManageItem> newItemList = req.getTimetableList().stream()
                .map(item -> item.toEntity(timetableManage))
                .toList();
        timetableManage.upsertItems(newItemList);
        timetableManage.updateModifiedAt();
    }

    public TimetableListRes getTimetableList(@Valid @Positive Long id) {
        TimetableManage timetable = timetableManageProvider.findManage(id);
        return TimetableListRes.create(timetable);
    }

}
