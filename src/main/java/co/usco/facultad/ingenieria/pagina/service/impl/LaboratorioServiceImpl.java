package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import co.usco.facultad.ingenieria.pagina.repository.LaboratorioRepository;
import co.usco.facultad.ingenieria.pagina.service.LaboratorioService;
import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.LaboratorioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Laboratorio}.
 */
@Service
@Transactional
public class LaboratorioServiceImpl implements LaboratorioService {

    private final Logger log = LoggerFactory.getLogger(LaboratorioServiceImpl.class);

    private final LaboratorioRepository laboratorioRepository;

    private final LaboratorioMapper laboratorioMapper;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository, LaboratorioMapper laboratorioMapper) {
        this.laboratorioRepository = laboratorioRepository;
        this.laboratorioMapper = laboratorioMapper;
    }

    @Override
    public Mono<LaboratorioDTO> save(LaboratorioDTO laboratorioDTO) {
        log.debug("Request to save Laboratorio : {}", laboratorioDTO);
        return laboratorioRepository.save(laboratorioMapper.toEntity(laboratorioDTO)).map(laboratorioMapper::toDto);
    }

    @Override
    public Mono<LaboratorioDTO> update(LaboratorioDTO laboratorioDTO) {
        log.debug("Request to save Laboratorio : {}", laboratorioDTO);
        return laboratorioRepository.save(laboratorioMapper.toEntity(laboratorioDTO)).map(laboratorioMapper::toDto);
    }

    @Override
    public Mono<LaboratorioDTO> partialUpdate(LaboratorioDTO laboratorioDTO) {
        log.debug("Request to partially update Laboratorio : {}", laboratorioDTO);

        return laboratorioRepository
            .findById(laboratorioDTO.getId())
            .map(existingLaboratorio -> {
                laboratorioMapper.partialUpdate(existingLaboratorio, laboratorioDTO);

                return existingLaboratorio;
            })
            .flatMap(laboratorioRepository::save)
            .map(laboratorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LaboratorioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Laboratorios");
        return laboratorioRepository.findAllBy(pageable).map(laboratorioMapper::toDto);
    }

    public Mono<Long> countAll() {
        return laboratorioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LaboratorioDTO> findOne(Long id) {
        log.debug("Request to get Laboratorio : {}", id);
        return laboratorioRepository.findById(id).map(laboratorioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Laboratorio : {}", id);
        return laboratorioRepository.deleteById(id);
    }
}
