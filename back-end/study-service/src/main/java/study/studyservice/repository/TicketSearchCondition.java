package study.studyservice.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.studyservice.domain.TicketStatus;

@Data
@AllArgsConstructor
public class TicketSearchCondition {

    private TicketStatus status;
}
