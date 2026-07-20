package com.kredmint.loom.attendance.service;

import com.kredmint.loom.attendance.entity.Holiday;
import com.kredmint.loom.attendance.repository.HolidayRepository;

import java.util.List;

public class HolidayService {

    private HolidayRepository holidayRepository;

    public Holiday createHoliday(Holiday holiday){
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAllHolidays(){
        return holidayRepository.findAll();
    }

    public Holiday updateHoliday(String id, Holiday holiday){

        Holiday existingHoliday = holidayRepository.findById(id).orElseThrow(()-> new RuntimeException("Holiday not found"));

        existingHoliday.setHolidayName(holiday.getHolidayName());
        existingHoliday.setDate(holiday.getDate());
        return holidayRepository.save(existingHoliday);
    }

    public void deleteHoliday(String id){
        Holiday existingHoliday = holidayRepository.findById(id).orElseThrow(()->new RuntimeException("Holiday not found"));
        holidayRepository.delete(existingHoliday);

    }

}
