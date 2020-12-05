package com.jorgejy.springboot.coupons.api.rest.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jorgejy.springboot.coupons.api.rest.models.Coupon;

import com.jorgejy.springboot.coupons.api.rest.models.response.ResponseServiceModelMap;
import com.jorgejy.springboot.coupons.api.rest.services.CouponService;

@RestController
@RequestMapping("coupons-api")
public class CouponsController {

	private static Logger log = LoggerFactory.getLogger(CouponsController.class);

	@Autowired
	private CouponService couponService;

	@GetMapping()
	public ResponseEntity<Map<String, Object>> getAll() {

		ResponseServiceModelMap<List<Coupon>> modelResponse;
		ResponseEntity<Map<String, Object>> response;

		HttpStatus httpStatus;

		try {
			modelResponse = new ResponseServiceModelMap<List<Coupon>>("Coupons found.", "",
					couponService.getAllCoupons());
			httpStatus = HttpStatus.OK;
		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<List<Coupon>>("Error get all coupons in database.",
					e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<List<Coupon>>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());

		return response;
	}

	@GetMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Map<String, Object>> finById(@PathVariable Long id) {

		ResponseServiceModelMap<Coupon> modelResponse;
		ResponseEntity<Map<String, Object>> response;

		HttpStatus httpStatus;
		try {
			Coupon coupon = couponService.findById(id);

			if (coupon != null) {
				modelResponse = new ResponseServiceModelMap<Coupon>("Coupon found.", "", coupon);
				httpStatus = HttpStatus.OK;
			} else {
				modelResponse = new ResponseServiceModelMap<Coupon>("Coupon not found.", "Coupon is empty.", null);
				httpStatus = HttpStatus.NOT_FOUND;
			}
		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Error search in database.", e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());

		return response;
	}

	@PostMapping()
	public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Coupon coupon, BindingResult result) {
		ResponseServiceModelMap<Coupon> modelResponse;
		ResponseEntity<Map<String, Object>> response;

		HttpStatus httpStatus;

		try {
			if (result.hasErrors()) {
				List<String> errros = result.getFieldErrors().stream()
						.map(error -> error.getField() + " " + error.getDefaultMessage())
						.peek(error -> log.error(error)).collect(Collectors.toList());

				modelResponse = new ResponseServiceModelMap<Coupon>("Coupon not valid.", String.join(", ", errros),
						null);

				httpStatus = HttpStatus.NOT_ACCEPTABLE;
			} else {

				Coupon couponResponse = couponService.save(coupon);

				if (couponResponse != null) {

					modelResponse = new ResponseServiceModelMap<Coupon>("Coupon add.", "", couponResponse);
					httpStatus = HttpStatus.CREATED;
				} else {

					modelResponse = new ResponseServiceModelMap<Coupon>("Coupon not add.", "Coupon is empty.", null);
					httpStatus = HttpStatus.NOT_FOUND;
				}

			}

		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Error search in database.", e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());

		return response;
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody Coupon coupon,
			BindingResult result) {
		ResponseServiceModelMap<Coupon> modelResponse;
		ResponseEntity<Map<String, Object>> response;

		HttpStatus httpStatus;
		try {
			if (result.hasErrors()) {
				List<String> errros = result.getFieldErrors().stream()
						.map(error -> error.getField() + " " + error.getDefaultMessage())
						.peek(error -> log.error(error)).collect(Collectors.toList());

				modelResponse = new ResponseServiceModelMap<Coupon>("Coupon not valid.", String.join(", ", errros),
						null);

				httpStatus = HttpStatus.NOT_ACCEPTABLE;
			} else {

				Coupon couponFind = couponService.findById(id);

				if (couponFind != null) {

					coupon.setId(id);
					coupon.setUpdateAt(new Date());

					Coupon couponUpdate = couponService.save(coupon);

					modelResponse = new ResponseServiceModelMap<Coupon>("Coupon update.", "", couponUpdate);

					httpStatus = HttpStatus.CREATED;
				} else {

					modelResponse = new ResponseServiceModelMap<Coupon>("Coupon not found.", "Coupon is empty.", null);

					httpStatus = HttpStatus.NOT_FOUND;
				}
			}

		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Error update in database.", e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<Coupon>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());

		return response;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {

		ResponseServiceModelMap<Boolean> modelResponse;
		ResponseEntity<Map<String, Object>> response;

		HttpStatus httpStatus;

		try {
			couponService.delete(id);
			modelResponse = new ResponseServiceModelMap<Boolean>("Coupon delete.", "", true);

			httpStatus = HttpStatus.OK;
		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<Boolean>("Error delete in database.", e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<Boolean>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());

		return response;
	}
}
