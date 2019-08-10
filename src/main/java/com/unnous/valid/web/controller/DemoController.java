package com.unnous.valid.web.controller;


import com.unnous.valid.annotation.valid.function.NotNull;
import com.unnous.valid.annotation.valid.function.ValidParam;
import com.unnous.valid.web.dto.request.DemoRequestDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试Controller
 */
@RestController
public class DemoController {


    @RequestMapping(value = "/demoStr", method = RequestMethod.POST)
    public Object getDemoRequestDemo( @NotNull @RequestBody String dto){
        return dto;
    }
    @RequestMapping(value = "/demo", method = RequestMethod.POST)
    public Object getDemoRequestDemo(@ValidParam({"name","size"}) @RequestBody DemoRequestDto dto){
        return dto;
    }
    @RequestMapping(value = "/demos", method = RequestMethod.POST)
    public Object getDemoRequestDemo(@ValidParam({"name"}) @RequestBody List<DemoRequestDto> dto){
        return dto;
    }

}
