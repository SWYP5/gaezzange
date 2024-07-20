package com.swyp.gaezzange.api.routine.result;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RoutineResultController", description = "Routine Result API")
@RestController
@RequestMapping("/v1/routine/result")
@RequiredArgsConstructor
public class RoutineResultController {

}
