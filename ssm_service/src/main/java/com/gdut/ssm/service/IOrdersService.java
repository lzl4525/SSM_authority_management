package com.gdut.ssm.service;

import com.gdut.ssm.domain.Orders;

import java.util.List;

public interface IOrdersService {


    List<Orders> findAll(int page, int size) throws Exception;
}
