package com.agrotech.service.impl;

import com.agrotech.Entity.Cultivo;
import com.agrotech.Entity.TipoCultivo;
import com.agrotech.dto.request.CultivoRequestDTO;
import com.agrotech.dto.request.CultivoUpdateRequestDTO;
import com.agrotech.dto.response.CultivoResponseDTO;
import com.agrotech.mapper.CultivoMapper;
import com.agrotech.repository.CultivoRepository;
import com.agrotech.repository.TipoCultivoRepository;
import com.agrotech.service.CultivoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CultivoServiceImpl implements CultivoService {

    private final CultivoRepository cultivoRepository;
    private final TipoCultivoRepository tipoCultivoRepository;
    private final CultivoMapper cultivoMapper;

    public CultivoServiceImpl(CultivoRepository cultivoRepository, TipoCultivoRepository tipoCultivoRepository, CultivoMapper cultivoMapper) {
        this.cultivoRepository = cultivoRepository;
        this.tipoCultivoRepository = tipoCultivoRepository;
        this.cultivoMapper = cultivoMapper;
    }

    @Override
    public CultivoResponseDTO crear(CultivoRequestDTO cultivoRequestDTO) {
        TipoCultivo tipo = tipoCultivoRepository.findById(cultivoRequestDTO.getIdTipoCultivo())
                .orElseThrow(() -> new RuntimeException("TipoCultivo no encontrado con id: " + cultivoRequestDTO.getIdTipoCultivo()));

        Cultivo cultivo = new Cultivo();
        cultivo.setNombre(cultivoRequestDTO.getNombre());
        cultivo.setTipoCultivo(tipo);

        return cultivoMapper.toResponse(cultivoRepository.save(cultivo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CultivoResponseDTO> buscarPorNombre(String nombre) {
        return cultivoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(cultivoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CultivoResponseDTO> buscarPorTipo(Integer idTipoCultivo) {
        return cultivoRepository.findByTipoCultivo_IdTiposCultivos(idTipoCultivo)
                .stream()
                .map(cultivoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CultivoResponseDTO> listarTodos() {
        return cultivoRepository.findAll()
                .stream()
                .map(cultivoMapper::toResponse)
                .toList();
    }

    @Override
    public CultivoResponseDTO actualizar(Integer id, CultivoUpdateRequestDTO dto) {
        Cultivo cultivo = cultivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cultivo no encontrado con id: " + id));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            cultivo.setNombre(dto.getNombre());
        }

        if (dto.getIdTipoCultivo() != null) {
            TipoCultivo tipo = tipoCultivoRepository.findById(dto.getIdTipoCultivo())
                    .orElseThrow(() -> new RuntimeException("TipoCultivo no encontrado con id: " + dto.getIdTipoCultivo()));
            cultivo.setTipoCultivo(tipo);
        }

        return cultivoMapper.toResponse(cultivoRepository.save(cultivo));
    }

    @Override
    public void eliminar(Integer id) {
        if (!cultivoRepository.existsById(id)) {
            throw new RuntimeException("Cultivo no encontrado con id: " + id);
        }
        cultivoRepository.deleteById(id);
    }
}
