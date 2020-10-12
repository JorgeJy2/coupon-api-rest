package com.jorgejy.springboot.coupons.api.rest.controllers;

import java.util.Date;
import java.util.HashMap;
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
import com.jorgejy.springboot.coupons.api.rest.models.ResponseService;
import com.jorgejy.springboot.coupons.api.rest.services.CouponService;

@RestController
@RequestMapping("coupons-api")
public class CouponsController {

	private static Logger log = LoggerFactory.getLogger(CouponsController.class);

	@Autowired
	private CouponService couponService;

	@GetMapping()
	public ResponseEntity<Map<String, Object>> getAll() {
		Map<String, Object> response = new HashMap<>();
		ResponseService<List<Coupon>> modelResponse = new ResponseService<List<Coupon>>();
		HttpStatus httpStatus;
		try {

			modelResponse.setMessage("Coupons found.");
			modelResponse.setInternalMessage("");
			modelResponse.setContent(couponService.getAllCoupons());
			httpStatus = HttpStatus.OK;

		} catch (DataAccessException e) {
			modelResponse.setMessage("Error get all coupons in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> finById(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		ResponseService<Coupon> modelResponse = new ResponseService<Coupon>();
		HttpStatus httpStatus;
		try {
			Coupon coupon = couponService.findById(id);

			if (coupon != null) {
				modelResponse.setMessage("Coupon found.");
				modelResponse.setInternalMessage("");
				modelResponse.setContent(coupon);
				httpStatus = HttpStatus.OK;
			} else {
				modelResponse.setMessage("Coupon not found.");
				modelResponse.setInternalMessage("Coupon is empty.");
				modelResponse.setContent(null);
				httpStatus = HttpStatus.NOT_FOUND;
			}
		} catch (DataAccessException e) {
			modelResponse.setMessage("Error search in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
	}

	@PostMapping()
	public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Coupon coupon, BindingResult result) {
		Map<String, Object> response = new HashMap<String, Object>();
		ResponseService<Coupon> modelResponse = new ResponseService<Coupon>();
		HttpStatus httpStatus;

		try {
			if (result.hasErrors()) {
				List<String> errros = result.getFieldErrors().stream()
						.map(error -> error.getField() + " " + error.getDefaultMessage())
						.peek(error -> log.error(error)).collect(Collectors.toList());
				modelResponse.setMessage("Coupon not valid.");
				modelResponse.setInternalMessage(String.join(",", errros));
				modelResponse.setContent(null);
				httpStatus = HttpStatus.NOT_ACCEPTABLE;
			} else {

				Coupon couponResponse = couponService.save(coupon);

				if (couponResponse != null) {
					modelResponse.setMessage("Coupon add.");
					modelResponse.setInternalMessage("");
					modelResponse.setContent(couponResponse);
					httpStatus = HttpStatus.CREATED;
				} else {
					modelResponse.setMessage("Coupon not add.");
					modelResponse.setInternalMessage("Coupon is empty.");
					modelResponse.setContent(null);
					httpStatus = HttpStatus.NOT_FOUND;
				}

			}

		} catch (DataAccessException e) {
			modelResponse.setMessage("Error add in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Coupon coupon) {
		Map<String, Object> response = new HashMap<String, Object>();
		ResponseService<Coupon> modelResponse = new ResponseService<Coupon>();
		HttpStatus httpStatus;
		try {
			Coupon couponFind = couponService.findById(id);

			if (couponFind != null) {

				if (coupon.getName() != null) {
					couponFind.setName(coupon.getName());
				}
				if (coupon.getDescription() != null) {
					couponFind.setDescription(coupon.getDescription());
				}

				if (coupon.getImageUrl() != null) {
					couponFind.setImageUrl(coupon.getImageUrl());
				}

				if (coupon.getStore() != null) {
					couponFind.setStore(coupon.getStore());
				}

				if (coupon.getOffertText() != null) {
					couponFind.setOffertText(coupon.getOffertText());
				}

				if (coupon.getOffertValue() != null) {
					couponFind.setOffertValue(coupon.getOffertValue());
				}

				if (coupon.getCode() != null) {
					couponFind.setCode(coupon.getCode());
				}

				if (coupon.getTermsAndConditions() != null) {
					couponFind.setTermsAndConditions(coupon.getTermsAndConditions());
				}

				if (coupon.getUrl() != null) {
					couponFind.setUrl(coupon.getUrl());
				}

				if (coupon.getStatus() != null) {
					couponFind.setStatus(coupon.getStatus());
				}

				if (coupon.getStartDate() != null) {
					couponFind.setStartDate(coupon.getStartDate());
				}

				if (coupon.getEndDate() != null) {
					couponFind.setEndDate(coupon.getEndDate());
				}

				couponFind.setUpdateAt(new Date());
				Coupon couponUpdate = couponService.save(couponFind);

				modelResponse.setMessage("Coupon update.");
				modelResponse.setInternalMessage("");
				modelResponse.setContent(couponUpdate);
				httpStatus = HttpStatus.CREATED;

			} else {
				modelResponse.setMessage("Coupon not found.");
				modelResponse.setInternalMessage("Coupon is empty.");
				modelResponse.setContent(null);
				httpStatus = HttpStatus.NOT_FOUND;
			}
		} catch (DataAccessException e) {
			modelResponse.setMessage("Error update in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		ResponseService<Boolean> modelResponse = new ResponseService<Boolean>();
		HttpStatus httpStatus;

		try {
			couponService.delete(id);
			modelResponse.setMessage("Coupon delete.");
			modelResponse.setInternalMessage("");
			modelResponse.setContent(true);
			httpStatus = HttpStatus.OK;
		} catch (DataAccessException e) {
			modelResponse.setMessage("Error delete in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(false);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(false);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}

		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
	}
}
