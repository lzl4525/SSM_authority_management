package com.gdut.ssm.service.impl;

import com.gdut.ssm.dao.IPermissionDao;
import com.gdut.ssm.domain.Permission;
import com.gdut.ssm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private IPermissionDao permissionDao;

    @Override
    public List<Permission> findAll() throws Exception {

        return permissionDao.findAll();
    }

    @Override
    public void save(Permission permission) throws Exception{
        permissionDao.save(permission);
    }
}
