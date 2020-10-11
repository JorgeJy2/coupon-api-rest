package com.jorgejy.springboot.coupons.api.rest.services;

import java.util.List;

import com.jorgejy.springboot.coupons.api.rest.models.Coupon;

public interface CouponService {

	public List<Coupon> getAllCoupons();

	public Coupon findById(Long id);

	public Coupon save(Coupon coupon);

	public void delete(Long id);

}
