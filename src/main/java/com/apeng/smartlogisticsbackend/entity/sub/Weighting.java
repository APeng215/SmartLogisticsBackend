package com.apeng.smartlogisticsbackend.entity.sub;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Weighting {

    private int count;
    private double weight;

}
