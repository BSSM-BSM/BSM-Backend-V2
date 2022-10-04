package bssm.bsm.domain.board.emoticon.entities;

import bssm.bsm.domain.board.emoticon.dto.response.EmoticonItemResponseDto;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emoticon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 12)
    private String name;

    @Column(length = 100)
    private String description;

    @Column
    private boolean active;

    @Column
    private boolean deleted;

    @Column
    private String deleteReason;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "pk.emoticon", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<EmoticonItem> items = new ArrayList<>();

    @Builder
    public Emoticon(long id, String name, String description, boolean active, boolean deleted, String deleteReason, Long userCode, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.deleteReason = deleteReason;
        this.userCode = userCode;
        this.user = user;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public EmoticonResponseDto toDto() {
        return EmoticonResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(getCreatedAt())
                .items(
                        getItems()
                                .stream().map(EmoticonItem::toDto)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
