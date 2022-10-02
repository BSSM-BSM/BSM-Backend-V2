package bssm.bsm.domain.board.emoticon.entities;

import bssm.bsm.domain.board.emoticon.dto.response.EmoticonItemResponseDto;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
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

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "pk.emoticon", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<EmoticonItem> items = new ArrayList<>();

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
