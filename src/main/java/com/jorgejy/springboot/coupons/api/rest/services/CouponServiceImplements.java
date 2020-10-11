package com.jorgejy.springboot.coupons.api.rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jorgejy.springboot.coupons.api.rest.daos.CouponDao;
import com.jorgejy.springboot.coupons.api.rest.models.Coupon;

@Service
public class CouponServiceImplements implements CouponService {

	@Autowired
	private CouponDao couponDao;

	@Override
	@Transactional(readOnly = true)
	public List<Coupon> getAllCoupons() {
		return (List<Coupon>) couponDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Coupon findById(Long id) {
		return couponDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Coupon save(Coupon coupon) {
		return couponDao.save(coupon);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		couponDao.deleteById(id);
	}

}
