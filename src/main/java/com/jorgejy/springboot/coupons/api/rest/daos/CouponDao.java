package com.jorgejy.springboot.coupons.api.rest.daos;

import org.springframework.data.repository.CrudRepository;

import com.jorgejy.springboot.coupons.api.rest.models.Coupon;

public interface CouponDao extends CrudRepository<Coupon, Long>{

}
