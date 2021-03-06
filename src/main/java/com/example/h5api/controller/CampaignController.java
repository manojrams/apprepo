package com.example.h5api.controller;

import com.example.h5api.dto.CampaignDto;
import com.example.h5api.dto.CampaignDtoIdDescription;
import com.example.h5api.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("campaign")
public class CampaignController implements GenericController<CampaignDto> {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    public List<CampaignDto> list() {
        return campaignService.findAll();
    }

    @Override
    public CampaignDto findById(Integer id) {
        return campaignService.findById(id);
    }

    @Override
    public CampaignDto save(CampaignDto campaignDto) {
        return campaignService.save(campaignDto);
    }

    @Override
    public void delete(Integer id) {
        campaignService.deleteById(id);
    }

    @GetMapping("/enable/{id}")
    public CampaignDto enableCampaign(@PathVariable Integer id) {
        return campaignService.enableCampaign(id);
    }

    @GetMapping("/disable/{id}")
    public CampaignDto disableCampaign(@PathVariable Integer id) {
        return campaignService.disableCampaign(id);
    }

    @GetMapping("/get/{date}")
    public List<CampaignDto> nominationSummary(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return campaignService.getCampaignByDate(date);
    }

    @GetMapping("/get/")
    public List<CampaignDto> nominationSummary() {
        return campaignService.getCampaignByDateNow();
    }

    @GetMapping("/listCampaigns")
    public List<CampaignDtoIdDescription> findAllCampaignIdName() {
        return campaignService.findAllCampaignIdName();
    }
}
