package resume.miles.equalizer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.equalizer.dto.EqualizerDto;
import resume.miles.equalizer.mapper.EqualizerMapper;
import resume.miles.equalizer.repository.EqualizerRepository;

@Service
@RequiredArgsConstructor
public class EqualizerService {
    private final EqualizerRepository repository;
    public List<EqualizerDto>getEqualizersByAwarenessId(Long awarenessId){
        List<EqualizerDto> data=repository.findByAwarenessId(awarenessId)
        .stream().map(EqualizerMapper::toDto).collect(Collectors.toList());

        if (data.isEmpty()) {
            throw new RuntimeException("No equalizer data found for awareness id: \" + awarenessId");
        }
        return data;
        
    }
}
