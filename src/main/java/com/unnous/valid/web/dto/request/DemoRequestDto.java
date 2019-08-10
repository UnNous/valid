package com.unnous.valid.web.dto.request;


import com.unnous.valid.annotation.valid.function.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoRequestDto {
    @NotNull("名称不能为空")
    public String name;//名称
    @NotNull("请求大小不能为0")
    public Integer size;// 请求大小
}
