package com.cloudmore.wage.consumer.service;

import com.cloudmore.wage.consumer.mapper.WageMapper;
import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WageServiceImpl implements WageService {
    private final WageRepository wageRepository;
    private final WageMapper wageMapper;

    @Override
    public List<UserWage> findAll() {
        return wageMapper.convert(wageRepository.findAll());
    }
}
