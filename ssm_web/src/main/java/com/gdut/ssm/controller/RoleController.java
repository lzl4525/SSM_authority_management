package com.gdut.ssm.controller;

import com.gdut.ssm.domain.Permission;
import com.gdut.ssm.domain.Role;
import com.gdut.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/addPermissionToRole.do")
    public String addPermissionToRole(@RequestParam(name = "roleId", required = true) String roleId, @RequestParam(name = "ids", required = true) String[] permissionIds) throws Exception {
        System.out.println("-----------");
        System.out.println(roleId);
        System.out.println("####################");
        for(String ids:permissionIds){
            System.out.println(ids);
        }
        System.out.println("**************");

        roleService.addPermissionToRole(roleId,permissionIds);
        return "redirect:findAll.do";
    }


    @RequestMapping("findRoleByIdAndAllPermission")
    public ModelAndView findRoleByIdAndAllPermission(@RequestParam(name = "id",required = true) String roleId) throws Exception{
        ModelAndView mv = new ModelAndView();

        Role role = roleService.findById(roleId);
        List<Permission> otherPermission = roleService.findOtherPermissions( roleId);

        mv.addObject("role",role);
        mv.addObject("permissionList",otherPermission);
        mv.setViewName("role-permission-add");
        return mv;

    }

    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception{
        ModelAndView mv = new ModelAndView();
        List<Role> roleList = roleService.findAll();
        mv.addObject("roleList",roleList);
        mv.setViewName("role-list");
        return mv;
    }

    @RequestMapping("/save.do")
    public String save(Role role) throws Exception{

        roleService.save(role);
        return "redirect:findAll.do";

    }
}
