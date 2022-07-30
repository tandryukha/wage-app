package com.cloudmore.wage.consumer.mapper;

import com.cloudmore.wage.consumer.entity.Wage;
import com.cloudmore.wage.model.UserWage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface WageMapper {

    UserWage convert(Wage wage);

    @Mapping(target = "id", ignore = true)
    Wage convert(UserWage wage);

    List<UserWage> convert(List<Wage> wages);
}
