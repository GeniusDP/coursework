package com.zaranik.coursework.checkerservice.dtos.response;

import com.zaranik.coursework.checkerservice.entities.RuntimeStatus;

public record RuntimeStatusStatsDto(RuntimeStatus status, Long count){

}
