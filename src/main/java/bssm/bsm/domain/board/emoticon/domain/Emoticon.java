package bssm.bsm.domain.board.emoticon.domain;

import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emoticon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long totalView;

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

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @OneToMany(mappedBy = "emoticon", cascade = CascadeType.REMOVE)
    private final List<EmoticonItem> items = new ArrayList<>();

    public static Emoticon create(String name, String description, User user) {
        Emoticon emoticon = new Emoticon();
        emoticon.totalView = 0L;
        emoticon.name = name;
        emoticon.description = description;
        emoticon.user = user;
        emoticon.active = false;
        emoticon.deleted = false;
        emoticon.deleteReason = null;
        return emoticon;
    }

    public void activate() {
        this.active = true;
    }

    public void delete(String deleteReason) {
        this.deleted = true;
        this.deleteReason = deleteReason;
    }

    public void incrementTotalView() {
        this.totalView++;
    }

    public EmoticonRes toResponse() {
        return EmoticonRes.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(getCreatedAt())
                .build();
    }
}
