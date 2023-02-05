package bssm.bsm.domain.board.emoticon.domain;

import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.user.domain.User;
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
    private Long id;

    @Column(length = 12)
    private String name;

    @Column(length = 100)
    private String description;

    @Column
    private Boolean active;

    @Column
    private Boolean deleted;

    @Column
    private String deleteReason;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "emoticon", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<EmoticonItem> items = new ArrayList<>();

    @Builder
    public Emoticon(Long id, String name, String description, Boolean active, Boolean deleted, String deleteReason, Long userCode, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.deleteReason = deleteReason;
        this.userCode = userCode;
        this.user = user;
    }

    public void activate() {
        this.active = true;
    }

    public void delete(String deleteReason) {
        this.deleted = true;
        this.deleteReason = deleteReason;
    }

    public EmoticonRes toResponse() {
        return EmoticonRes.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(getCreatedAt())
                .items(items.stream()
                        .map(EmoticonItem::toResponse)
                        .toList())
                .build();
    }
}
