package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageRepository;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import bssm.bsm.domain.school.timetable.presentation.dto.req.CreateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.TimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableListReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableManageRes;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableManageService {

    private final TimetableProvider timetableProvider;
    private final TimetableManageProvider timetableManageProvider;

    private final TimetableManageRepository timetableManageRepository;

    private final TimetableNotificationService timetableNotification;

    public List<TimetableManageRes> getManageList(@Valid TimetableReq dto) {
        return timetableManageProvider.findManageList(dto.getGrade(), dto.getClassNo()).stream()
                .map(TimetableManage::toResponse)
                .toList();
    }

    @Transactional
    public void createTimetable(CreateTimetableReq dto) {
        TimetableManage manage = TimetableManage.create(
                dto.getName(),
                dto.getType(),
                dto.getGrade(),
                dto.getClassNo());
        timetableManageRepository.save(manage);
    }

    @Transactional
    public void applyTimetable(long id) throws JsonProcessingException {
        TimetableManage manage = timetableManageProvider.findManage(id);
        Timetable timetable = timetableProvider.findTimetable(manage.getGrade(), manage.getClassNo());

        List<TimetableItem> newItemList = manage.getItems().stream()
                .map(item -> item.toTimetableItem(timetable))
                .toList();
        timetable.upsertItems(newItemList);

        timetableNotification.sendChangeTimetableNotification(manage);
    }

    @Transactional
    public void deleteTimetable(long id) {
        TimetableManage manage = timetableManageProvider.findManage(id);
        timetableManageRepository.delete(manage);
    }

    @Transactional
    public void updateTimetable(long id, UpdateTimetableReq dto) {
        TimetableManage manage = timetableManageProvider.findManage(id);
        manage.update(dto.getName(), dto.getType());
        timetableManageRepository.save(manage);
    }

    @Transactional
    public void updateTimetableList(long id, UpdateTimetableListReq dto) {
        TimetableManage manage = timetableManageProvider.findManage(id);
        List<TimetableManageItem> newItemList = dto.getTimetableList().stream()
                .map(item -> item.toEntity(manage))
                .toList();
        manage.upsertItems(newItemList);
        manage.updateModifiedAt();
    }

    public List<List<TimetableRes>> getTimetableList(@Valid @Positive Long id) {
        TimetableManage timetable = timetableManageProvider.findManage(id);

        Map<Integer, List<TimetableRes>> timetableMap = new HashMap<>();
        timetable.getItems().forEach(item -> {
            int day = item.getPk().getDay();
            if (timetableMap.get(day) == null) {
                timetableMap.put(day, new ArrayList<>(List.of(item.toResponse())));
                return;
            }
            timetableMap.get(day).add(item.toResponse());
        });

        return new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6))
                .stream().map(timetableMap::get)
                .toList();
    }

}
