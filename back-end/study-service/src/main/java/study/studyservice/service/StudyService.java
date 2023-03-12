package study.studyservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.studyservice.domain.Study;
import study.studyservice.dto.StudyDto;
import study.studyservice.repository.StudyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Long saveStudy(StudyDto studyDto) {
        studyRepository.findByName(studyDto.getName()).ifPresent(study -> {
            throw new RuntimeException("이미 존재한는 스터디입니다.");
        });

        Study study = Study.from(studyDto);
        studyRepository.save(study);
        return study.getId();
    }

    public StudyDto getStudy(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 스터디입니다.");
        });
        StudyDto studyDto = StudyDto.from(study);

        return studyDto;
    }

    public Page<StudyDto> getStudies() {
        PageRequest request = PageRequest.of(0, 10);
        Page<Study> studies = studyRepository.findAll(request);

        return studies.map(StudyDto::from);
    }

    public Page<StudyDto> getStudiesByStudyName(String studyName) {
        PageRequest request = PageRequest.of(0, 10);
        Page<Study> studies = studyRepository.findAllByName(studyName, request);

        return studies.map(StudyDto::from);
    }

    public List<StudyDto> getStudiesByStudyIds(List<Long> studyIds) {
        List<Study> studies = studyRepository.findAllById(studyIds);
        List<StudyDto> studyDtos = studies.stream().map(StudyDto::from).collect(Collectors.toList());

        return studyDtos;
    }
}
