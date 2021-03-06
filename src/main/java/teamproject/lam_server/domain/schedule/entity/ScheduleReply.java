package teamproject.lam_server.domain.schedule.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamproject.lam_server.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "schedule_replies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_reply_id")
    private Long id;

    @Lob
    private String content;

    @Column(name = "schedule_reply_date")
    private LocalDateTime scheduleReplyDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ref_schedule_reply_id")
    private ScheduleReply parent;

    // 자식 정의
    @OneToMany(mappedBy = "parent")
    private List<ScheduleReply> child = new ArrayList<>();
}
