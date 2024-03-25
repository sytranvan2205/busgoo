package com.iuh.busgoo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.OrderDTO;
import com.iuh.busgoo.dto.PromotionDTO;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.entity.OrderDetail;
import com.iuh.busgoo.entity.PriceDetail;
import com.iuh.busgoo.entity.Promotion;
import com.iuh.busgoo.entity.PromotionDetail;
import com.iuh.busgoo.entity.PromotionLine;
import com.iuh.busgoo.entity.SeatOrder;
import com.iuh.busgoo.entity.Station;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.repository.OrderDetailRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.PriceDetailRepository;
import com.iuh.busgoo.repository.PromotionDetailRepository;
import com.iuh.busgoo.repository.PromotionLineRepository;
import com.iuh.busgoo.repository.SeatOrderRepository;
import com.iuh.busgoo.repository.StationRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.OrderCreateRequest;
import com.iuh.busgoo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private SeatOrderRepository seatOrderRepository;
	
	@Autowired
	private PriceDetailRepository priceDetailRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private PromotionLineRepository promotionLineRepository;
	
	@Autowired
	private PromotionDetailRepository promotionDetailRepository;

	@Override
	public DataResponse createOrder(OrderCreateRequest orderCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		LocalDate currDate = LocalDate.now();
		try {
			if(orderCreateRequest.getUserId()== null || orderCreateRequest.getLstSeatOrderId() == null || orderCreateRequest.getLstSeatOrderId().size()==0 || orderCreateRequest.getPickupPointId() == null) {
				throw new Exception();
			}else {
				List<SeatOrder> lstSeat = new ArrayList<SeatOrder>();
				for(Long seatId: orderCreateRequest.getLstSeatOrderId()) {
					SeatOrder seatOrder = seatOrderRepository.findById(seatId).get();
					if (seatOrder == null) {
						throw new Exception();
					}else {
						if(!seatOrder.getIsAvailable()) {
							dataResponse.setResponseMsg("This seat has been reserved");
							dataResponse.setRespType(Constant.SEAT_IS_NOT_AVAILABLE);
							Map<String, Object> seatError = new HashMap<String, Object>();
							seatError.put("data", seatOrder.getSeatName());
							dataResponse.setValueReponse(seatError);
							return dataResponse;
						}else {
							seatOrder.setIsAvailable(false);
							seatOrderRepository.save(seatOrder);
							lstSeat.add(seatOrder);
						}
					}
				}
				if (lstSeat.size()>0) {
					//create order
					User user = userRepository.findById(orderCreateRequest.getUserId()).get();
					Station pickupPoint = stationRepository.findById(orderCreateRequest.getPickupPointId()).get();
					Order order = new Order();
					order.setUser(user);
					if(pickupPoint == null) {
						throw new Exception();
					}
					order.setPickUpPoint(pickupPoint.getId());
					Long count = orderRepository.count();
					order.setCode("ODR"+(count+1));
					order.setIsPay(0);
					order.setStatus(1);
					orderRepository.save(order);
					
					//create order_detail
					List<OrderDetail> lstDetail = new ArrayList<OrderDetail>();
					Double totalTiketPrice = Double.valueOf(0);
					for(SeatOrder seat: lstSeat) {
						Long countDetail = orderDetailRepository.count();
						OrderDetail orderDetail = new OrderDetail();
						orderDetail.setCode("ODTL"+(countDetail+1));
						orderDetail.setSeat(seat);
						orderDetail.setOrder(order);
						PriceDetail pd = priceDetailRepository.findBySeatOrderIdAndCurrDate(seat.getId(), currDate);
						totalTiketPrice += pd.getValue();
						orderDetail.setPrice(pd.getValue());
						orderDetailRepository.save(orderDetail);
						lstDetail.add(orderDetail);
					}
					
					//apply promotion
					Double discountPrice = Double.valueOf(0);
					//Tìm các promotion lines với các detail thoải điều kiện
					List<PromotionLine> lines = promotionLineRepository.findPromotionLineByCondition(currDate,new BigDecimal(totalTiketPrice));
					List<PromotionDTO> promotionDTOs = new ArrayList<PromotionDTO>();
					for(PromotionLine line: lines) {
						PromotionDTO promotionDTO = new PromotionDTO();
						PromotionDetail detail = promotionDetailRepository.findByPromotionLineIdAndStatus(line.getId(), 1);
						Double discountValue = Double.valueOf(0);
						if (line.getPromotionType().equals(1)) {
							discountValue = detail.getDiscount().doubleValue();
						}else if(line.getPromotionType().equals(2)) {
							Double discountValueTmp = detail.getDiscount().doubleValue()*totalTiketPrice;
							discountValue = (discountValueTmp<= detail.getMaxDiscount().doubleValue())? discountValue: detail.getMaxDiscount().doubleValue();
						}
						discountPrice += discountValue;
						promotionDTO.setPromotionCode(line.getPromotion().getCode());
						promotionDTO.setPromotionLineName(line.getLineName());
						promotionDTO.setPromotionType(line.getPromotionType());
						promotionDTO.setDiscount(new BigDecimal(discountValue));
						promotionDTOs.add(promotionDTO);
					}
					
					// update order 
					order.setTotalTiketPrice(totalTiketPrice);
					order.setTotalDiscount(totalTiketPrice);
					order.setTotal(totalTiketPrice - discountPrice);
					orderRepository.save(order);
					
					OrderDTO orderDTO = new OrderDTO();
					orderDTO.setCode(order.getCode());
					orderDTO.setOrderId(order.getId());
					orderDTO.setIsPay(order.getIsPay());
					orderDTO.setTotal(order.getTotal());
					orderDTO.setStatus(order.getStatus());
					orderDTO.setTotalDiscount(order.getTotalDiscount());
					orderDTO.setTotalTiketPrice(totalTiketPrice);
					orderDTO.setPromotionDTOs(promotionDTOs);
					orderDTO.setOrderDetails(lstDetail);
					
					
					dataResponse.setResponseMsg("Reservation successful !!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> respValue = new HashMap<String, Object>();
					respValue.put("data", orderDTO);
					dataResponse.setValueReponse(respValue);
					return dataResponse;
	
				}else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}

	@Override
	public DataResponse getOrderDetail(Long orderId) {
		return null;
	}

	@Override
	public DataResponse deleteOrder(Long orderId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Optional<Order> tmp = orderRepository.findById(orderId);
			if(tmp != null) {
				Order order = tmp.get();
				order.setStatus(0);
				orderRepository.save(order);
				dataResponse.setResponseMsg("Delete order success");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getAllOrder() {
		DataResponse dataResponse = new DataResponse();
		try {
			List<Order> orders = orderRepository.findByStatus(1);
			dataResponse.setResponseMsg("Get orders success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", orders);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
