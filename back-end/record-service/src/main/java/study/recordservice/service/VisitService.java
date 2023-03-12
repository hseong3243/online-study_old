package study.recordservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.recordservice.domain.Visit;
import study.recordservice.dto.VisitDto;
import study.recordservice.repository.VisitRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;


    public VisitDto getVisited() {
        LocalDateTime now = LocalDateTime.now();
        if(now.getHour()<5)
            now = now.minusDays(1);
        Visit visit = visitRepository.findByDate(now).orElse(Visit.create());

        VisitDto visitDto = VisitDto.from(visit);
        return visitDto;
    }
}
