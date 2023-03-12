package study.recordservice.dto;

import study.recordservice.domain.Visit;

import java.time.LocalDateTime;

public class VisitDto {
    private Long visitId;
    private LocalDateTime date;
    private long visited;

    public static VisitDto from(Visit visit) {
        VisitDto visitDto = new VisitDto();
        visitDto.visited = visit.getId();
        visitDto.date = visit.getDate();
        visitDto.visited = visit.getVisited();
        return visitDto;
    }
}
