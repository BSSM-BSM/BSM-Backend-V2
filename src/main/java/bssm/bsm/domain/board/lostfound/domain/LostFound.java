package bssm.bsm.domain.board.lostfound.domain;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostFound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String objectName;

    private String imgSrc;

    @Column(length = 25)
    private String location;

    private LocalDateTime findDateTime;

    private String description;

    @Enumerated(EnumType.STRING)
    private Process process;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code", nullable = false)
    private User foundUser;

    @CreatedDate
    private LocalDateTime createdLocalDateTime;

    @Builder
    public LostFound(Long id, String objectName, String imgSrc, String location, LocalDateTime findDateTime, String description, Process process, User user, LocalDateTime createdLocalDateTime) {
        this.id = id;
        this.objectName = objectName;
        this.imgSrc = imgSrc;
        this.location = location;
        this.findDateTime = findDateTime;
        this.description = description;
        this.process = process;
        this.foundUser = user;
        this.createdLocalDateTime = createdLocalDateTime;
    }

    public void updateProcess(Process process) {
        this.process = process;
    }
}
