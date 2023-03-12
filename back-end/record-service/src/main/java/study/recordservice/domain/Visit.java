package study.recordservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long id;
    private LocalDateTime date;
    private long visited;

    public static Visit create() {
        Visit visit = new Visit();
        LocalDateTime now = LocalDateTime.now();
        if(now.getHour()<5)
            now = now.minusDays(1);
        visit.date = now;
        visit.visited = 0;
        return visit;
    }
}
