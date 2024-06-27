package com.apeng.smartlogisticsbackend.controller.util;

import java.util.List;

public record OrderAddingRequest(List<Long> dishIds) {
}
