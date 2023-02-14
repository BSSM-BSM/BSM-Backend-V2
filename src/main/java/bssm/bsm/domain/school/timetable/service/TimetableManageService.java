package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItemRepository;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageRepository;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItemRepository;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableRepository;
import bssm.bsm.domain.school.timetable.presentation.dto.request.CreateTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.UpdateTimetableListRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.UpdateTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableResponse;
import bssm.bsm.global.error.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class TimetableManageService {

    private final TimetableRepository timetableRepository;
    private final TimetableItemRepository timetableItemRepository;
    private final TimetableManageRepository timetableManageRepository;
    private final TimetableManageItemRepository timetableManageItemRepository;
    private final TimetableNotificationService timetableNotification;

    public List<TimetableManageResponse> getManageList(@Valid TimetableRequest dto) {
        return timetableManageRepository.findAllByGradeAndClassNoOrderByModifiedAtDesc(dto.getGrade(), dto.getClassNo())
                .stream().map(TimetableManage::toResponse).toList();
    }

    public void createTimetable(CreateTimetableRequest dto) {
        TimetableManage manage = TimetableManage.builder()
                .name(dto.getName())
                .type(dto.getType())
                .grade(dto.getGrade())
                .classNo(dto.getClassNo())
                .build();
        timetableManageRepository.save(manage);
    }

    @Transactional
    public void applyTimetable(long id) throws JsonProcessingException {
        TimetableManage manage = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);
        Timetable timetable = timetableRepository.findByPkGradeAndPkClassNo(manage.getGrade(), manage.getClassNo())
                .orElseThrow(NotFoundException::new);

        List<TimetableItem> deleteList = timetable.getItems();
        List<TimetableItem> newTimetableList = manage.getItems().stream()
                .map(item -> item.toTimetableItem(timetable))
                .toList();

        newTimetableList.forEach(deleteList::remove);
        timetableItemRepository.deleteAll(deleteList);
        timetableItemRepository.saveAll(newTimetableList);

        timetableNotification.sendChangeTimetableNotification(manage);
    }

    @Transactional
    public void deleteTimetable(long id) {
        TimetableManage manage = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);
        timetableManageRepository.delete(manage);
    }

    @Transactional
    public void updateTimetable(long id, UpdateTimetableRequest dto) {
        TimetableManage manage = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);
        manage.setName(dto.getName());
        manage.setType(dto.getType());
        timetableManageRepository.save(manage);
    }

    @Transactional
    public void updateTimetableList(long id, UpdateTimetableListRequest dto) {
        TimetableManage manage = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);
        manage.setModifiedAt(LocalDateTime.now());

        List<TimetableManageItem> deleteList = manage.getItems();
        List<TimetableManageItem> newTimetableList = dto.getTimetableList().stream()
                .filter(Objects::nonNull)
                .map(item -> item.toEntity(manage))
                .toList();

        newTimetableList.forEach(deleteList::remove);
        timetableManageItemRepository.deleteAll(deleteList);

        timetableManageItemRepository.saveAll(newTimetableList);
        timetableManageRepository.save(manage);
    }

    public List<List<TimetableResponse>> getTimetableList(@Valid @Positive Long id) {
        TimetableManage timetable = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);

        Map<Integer, List<TimetableResponse>> timetableMap = new HashMap<>();
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
