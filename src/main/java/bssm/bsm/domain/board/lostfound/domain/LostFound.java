package bssm.bsm.domain.board.lostfound.domain;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column( length = 25)
    private String location;

    private LocalDateTime findDateTime;

    private String description;

    @Enumerated(EnumType.STRING)
    private Process process;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private User foundUser;

    @Builder
    public LostFound(String objectName, String imgSrc, String location, LocalDateTime findDateTime, String description, Process process, User user) {
        this.objectName = objectName;
        this.imgSrc = imgSrc;
        this.location = location;
        this.findDateTime = findDateTime;
        this.description = description;
        this.process = process;
        this.foundUser = user;
    }

    public void updateProcess(Process process) {
        this.process = process;
    }
}
