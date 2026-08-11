package com.qmcms.service;

import com.qmcms.dto.request.CompetitionRequest;
import com.qmcms.dto.response.CompetitionResponse;
import com.qmcms.entity.Competition;
import com.qmcms.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements com.qmcms.service.CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    public CompetitionResponse createCompetition(CompetitionRequest request) {

        Competition competition = Competition.builder()
                .title(request.getTitle())
                .venue(request.getVenue())
                .competitionDate(request.getCompetitionDate())
                .status(request.getStatus())
                .build();

        return mapToResponse(competitionRepository.save(competition));
    }

    @Override
    public List<CompetitionResponse> getAllCompetitions() {

        return competitionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CompetitionResponse getCompetitionById(Long id) {

        return mapToResponse(findCompetition(id));
    }

    @Override
    public CompetitionResponse updateCompetition(Long id, CompetitionRequest request) {

        Competition competition = findCompetition(id);

        competition.setTitle(request.getTitle());
        competition.setVenue(request.getVenue());
        competition.setCompetitionDate(request.getCompetitionDate());
        competition.setStatus(request.getStatus());

        return mapToResponse(competitionRepository.save(competition));
    }

    @Override
    public void deleteCompetition(Long id) {

        Competition competition = findCompetition(id);

        competitionRepository.delete(competition);
    }

    // ==========================
    // Helper Methods
    // ==========================

    private Competition findCompetition(Long id) {

        return competitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Competition not found."
                ));

    }

    private CompetitionResponse mapToResponse(Competition competition) {

        return CompetitionResponse.builder()
                .id(competition.getId())
                .title(competition.getTitle())
                .venue(competition.getVenue())
                .competitionDate(competition.getCompetitionDate())
                .status(competition.getStatus())
                .createdAt(competition.getCreatedAt())
                .build();

    }

}