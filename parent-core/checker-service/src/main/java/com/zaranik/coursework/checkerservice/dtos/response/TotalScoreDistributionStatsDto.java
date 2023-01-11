package com.zaranik.coursework.checkerservice.dtos.response;

public record TotalScoreDistributionStatsDto(
  int from0Till20,
  int from21Till40,
  int from41Till60,
  int from61Till80,
  int from81Till100,
  int countAll
) {}
