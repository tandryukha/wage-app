package com.cloudmore.wage.consumer.service;

import com.cloudmore.wage.consumer.mapper.WageMapper;
import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class WageServiceImpl implements WageService {
    private final WageRepository wageRepository;
    private final WageMapper wageMapper;
    private final BigDecimal taxRate;

    @Override
    public List<UserWage> findAll() {
        return wageMapper.convert(wageRepository.findAll());
    }

    @Override
    public void save(UserWage userWage) {
        userWage.setWage(getGross(userWage.getWage()));
        wageRepository.save(wageMapper.convert(userWage));
    }

    private BigDecimal getGross(BigDecimal wage) {
        return wage.multiply(taxRate.add(BigDecimal.ONE));
    }
}
