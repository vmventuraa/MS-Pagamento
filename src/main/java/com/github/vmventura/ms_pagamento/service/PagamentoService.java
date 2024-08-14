package com.github.vmventura.ms_pagamento.service;

import com.github.vmventura.ms_pagamento.dto.PagamentoDTO;
import com.github.vmventura.ms_pagamento.model.Pagamento;
import com.github.vmventura.ms_pagamento.model.Status;
import com.github.vmventura.ms_pagamento.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Transactional(readOnly = true)
    public Page<PagamentoDTO> findAll(Pageable pageable){
        Page<Pagamento> page = repository.findAll(pageable);
        return page.map(PagamentoDTO::new);
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findById(long id){
        Pagamento entity = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Recurso não encontrado")
        );
        return new PagamentoDTO(entity);
    }

    @Transactional
    public PagamentoDTO insert(PagamentoDTO dto){
        Pagamento entity = new Pagamento();
        copyDtoToEntity(dto,entity);
        entity = repository.save(entity);
        return new PagamentoDTO(entity);
    }

    private void copyDtoToEntity(PagamentoDTO dto, Pagamento entity) {
        entity.setValor(dto.getValor());
        entity.setNome(dto.getNome());
        entity.setNumeroDoCartao(dto.getNumeroDoCartao());
        entity.setValidade(dto.getValidade());
        entity.setCodigoDeSeguranca(dto.getCodigoDeSeguranca());
        entity.setStatus(Status.CRIADO);
        entity.setPedidoId(dto.getPedidoId());
        entity.setFormaDePagamentoId(dto.getFormaDePagamentoId());
    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Recurso não encontrado");
        }
        try{
            repository.deleteById(id);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Recurso não encontrado");
        }
    }

}
