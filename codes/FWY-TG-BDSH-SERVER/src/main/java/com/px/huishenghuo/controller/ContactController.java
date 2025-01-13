package com.px.huishenghuo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.huishenghuo.controller.request.ContactRequest;
import com.px.huishenghuo.dto.ContactDto;
import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.service.IContactService;
import com.px.huishenghuo.vo.ContactVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @@author 品讯科技
 * @Description: 管理平台用户 前端控制器
 * @Date: 2020-01-16
 **/
@RestController
@RequestMapping("/contact")
@Api(value = "/contact", tags = "联系平台")
public class ContactController {
    @Autowired
    private IContactService contactService;


    @PostMapping("/admin/addContact")
    @ApiOperation(value = "新增联系方式", nickname = "新增联系方式", httpMethod = "POST", response = Result.class, notes = "新增联系方式")
    public Result<ContactVo> addContact(@RequestBody ContactRequest request){
        ContactDto dto = ContactRequest.toDto(request);
        ContactVo contactVo = contactService.addContact(dto);
        return ResultType.SUCCESS.build().bindData(contactVo);
    }

    @GetMapping("/admin/getContactPage")
    @ApiOperation(value = "获取分页", nickname = "获取分页", httpMethod = "GET", response = Result.class, notes = "获取分页")
    public Result<Page<ContactVo>> getContactPage(@RequestParam(defaultValue = "1") Long current,
                                                  @RequestParam(defaultValue = "10") Long size){
        Page<ContactVo> contactPage = contactService.getContactPage(current, size);
        return ResultType.SUCCESS.build().bindData(contactPage);
    }

    @DeleteMapping("/admin/deleteContact")
    @ApiOperation(value = "删除联系", nickname = "删除联系", httpMethod = "DELETE", response = Result.class, notes = "删除联系")
    public Result<Boolean> deleteContact(@RequestParam String id){
        Boolean aBoolean = contactService.deleteContact(id);
        return ResultType.SUCCESS.build().bindData(aBoolean);
    }

    @PutMapping("/admin/updateContact")
    @ApiOperation(value = "更新联系", nickname = "更新联系", httpMethod = "PUT", response = Result.class, notes = "更新联系")
    public Result<ContactVo> updateContact(@RequestBody ContactRequest request){
        ContactDto dto = ContactRequest.toDto(request);
        ContactVo contactVo = contactService.updateContact(dto);
        return ResultType.SUCCESS.build().bindData(contactVo);
    }

    @GetMapping("/admin/getContactInfo")
    @ApiOperation(value = "获取联系信息详情", nickname = "获取联系信息详情", httpMethod = "GET", response = Result.class, notes = "获取联系信息详情")
    public Result<ContactVo> getContactInfo(@RequestParam String id){
        ContactVo contactVo = contactService.getContactInfo(id,null);
        return ResultType.SUCCESS.build().bindData(contactVo);
    }

    @GetMapping("/user/getContact")
    @ApiOperation(value = "获取联系信息详情", nickname = "获取联系信息详情", httpMethod = "GET", response = Result.class, notes = "获取联系信息详情")
    public Result<ContactVo> getContact(@RequestParam(value = "typeEnum") Integer typeEnum){
        ContactVo contactVo = contactService.getContactInfo(null,typeEnum);
        return ResultType.SUCCESS.build().bindData(contactVo);
    }
}
