package com.apeng.smartlogisticsbackend.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public record LngLat(Double Lng, Double Lat) {}
