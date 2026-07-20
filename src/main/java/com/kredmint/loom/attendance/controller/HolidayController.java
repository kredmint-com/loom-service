package com.kredmint.loom.attendance.controller;

import com.kredmint.loom.attendance.entity.Holiday;
import com.kredmint.loom.attendance.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private HolidayService holidayService;

    @PostMapping
    public Holiday createHoliday(@RequestBody Holiday holiday){
        return holidayService.createHoliday(holiday);
    }

    @GetMapping
    public List<Holiday> getAllHolidays(){
        return holidayService.getAllHolidays();
    }

    @PutMapping("/{id}")
    public Holiday updateHoliday(@PathVariable String id, @RequestBody Holiday holiday){
        return holidayService.updateHoliday(id, holiday);
    }

    @DeleteMapping("/{id}")
    public void deleteHoliday(@PathVariable String id){
        holidayService.deleteHoliday(id);
    }
}