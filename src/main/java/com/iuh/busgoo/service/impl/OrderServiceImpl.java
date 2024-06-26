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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.OrderDTO;
import com.iuh.busgoo.dto.OrderDetailDTO;
import com.iuh.busgoo.dto.OrderManagerDTO;
import com.iuh.busgoo.dto.PromotionDTO;
import com.iuh.busgoo.dto.UserDTO;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.entity.OrderDetail;
import com.iuh.busgoo.entity.PriceDetail;
import com.iuh.busgoo.entity.Promotion;
import com.iuh.busgoo.entity.PromotionDetail;
import com.iuh.busgoo.entity.PromotionLine;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.SeatOrder;
import com.iuh.busgoo.entity.Station;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.filter.OrderFilter;
import com.iuh.busgoo.mapper.OrderDetailMapper;
import com.iuh.busgoo.mapper.OrderMapper;
import com.iuh.busgoo.mapper.UserMapper;
import com.iuh.busgoo.repository.OrderDetailRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.PriceDetailRepository;
import com.iuh.busgoo.repository.PromotionDetailRepository;
import com.iuh.busgoo.repository.PromotionLineRepository;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.SeatOrderRepository;
import com.iuh.busgoo.repository.StationRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.OrderCreateRequest;
import com.iuh.busgoo.service.OrderService;
import com.iuh.busgoo.utils.PageUtils;

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
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired 
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RegionDetailRepository detailRepository;

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
					Station pickupPoint = stationRepository.getById(orderCreateRequest.getPickupPointId());
					Station dropOffPoint = stationRepository.getById(orderCreateRequest.getDropoffPointId());
					Order order = new Order();
					order.setUser(user);
					if(pickupPoint == null) {
						throw new Exception();
					}
					if(dropOffPoint == null) {
						throw new Exception();
					}
					order.setPickUpPoint(pickupPoint.getId());
					order.setDropOffPoint(dropOffPoint.getId());
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
						seat.setOrderDetail(orderDetail);
						seatOrderRepository.save(seat);
						lstDetail.add(orderDetail);
					}
					
					//apply promotion
					Double discountPrice = Double.valueOf(0);
					//Tìm các promotion lines với các detail thoải điều kiện
					List<PromotionLine> lines = promotionLineRepository.findPromotionLineByCondition(currDate,new BigDecimal(totalTiketPrice));
					List<PromotionDTO> promotionDTOs = null ;
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
						if(discountPrice < discountValue) {
							promotionDTOs = new ArrayList<PromotionDTO>();
							discountPrice = discountValue;
							promotionDTO.setPromotionCode(line.getPromotion().getCode());
							promotionDTO.setPromotionLineName(line.getLineName());
							promotionDTO.setPromotionType(line.getPromotionType());
							promotionDTO.setDiscount(new BigDecimal(discountValue));
							promotionDTO.setPromotionDetailId(detail.getId());
							promotionDTOs.add(promotionDTO);
						}

					}
					
					// update order 
					order.setTotalTiketPrice(totalTiketPrice);
					order.setTotalDiscount(discountPrice);
					order.setTotal((totalTiketPrice - discountPrice) > 0 ? (totalTiketPrice - discountPrice) : 0);
					//add promotion to order
					if(promotionDTOs != null && promotionDTOs.size()>0) {
						PromotionDetail promotionDetail = promotionDetailRepository.getById(promotionDTOs.get(0).getPromotionDetailId());
						order.setPromotionDetail(promotionDetail);
					}
					orderRepository.save(order);
					
					OrderDTO orderDTO = new OrderDTO();
					orderDTO.setCode(order.getCode());
					orderDTO.setOrderId(order.getId());
					orderDTO.setIsPay(order.getIsPay());
					orderDTO.setTotal(order.getTotal());
					orderDTO.setStatus(order.getStatus());
					orderDTO.setTotalDiscount(order.getTotalDiscount());
					orderDTO.setTotalTiketPrice(totalTiketPrice);
					if(promotionDTOs != null && promotionDTOs.size()>0) {
						orderDTO.setPromotionDTO(promotionDTOs.get(0));;
					}
//					orderDTO.setPromotionDTOs(promotionDTOs);
//					orderDTO.setOrderDetails(lstDetail);
					List<OrderDetailDTO> detailDTOs = orderDetailMapper.toDto(lstDetail);
					orderDTO.setOrderDetails(detailDTOs);
					
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

	@SuppressWarnings("deprecation")
	@Override
	public Order findOrderById(Long orderId) {
		return orderRepository.getById(orderId);
	}

	@Override
	public DataResponse getOrderByFilter(OrderFilter orderFilter) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if(orderFilter.getSortBy() != null && orderFilter.getOrderBy() != null) {
				if(orderFilter.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(orderFilter.getOrderBy()).ascending();
				}else {
					sort = Sort.by(orderFilter.getOrderBy()).descending();
				}
				page= PageRequest.of(orderFilter.getPage(), orderFilter.getItemPerPage(), sort);
			}else {
				page = PageRequest.of(orderFilter.getPage(), orderFilter.getItemPerPage());
			}
			Page<Order> orderPage = orderRepository.findPageFilter(orderFilter.getStatusPaying(),orderFilter.getFromDate(),orderFilter.getToDate(),orderFilter.getQ(),page);
			List<Order> orders = orderPage.getContent();
			List<OrderManagerDTO> ordeDtos = orderMapper.toDto(orders);
			Page<OrderManagerDTO> orderDTOPage = new PageImpl<>(ordeDtos, page, orderPage.getTotalElements());
			
			dataResponse.setResponseMsg("Get orders success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",orderDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getOrderById(Long orderId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Order order = orderRepository.getById(orderId);
			if(order == null) {
				throw new Exception();
			}else {
				OrderDTO orderDTO = new OrderDTO();
				orderDTO.setCode(order.getCode());
				orderDTO.setOrderId(order.getId());
				orderDTO.setIsPay(order.getIsPay());
				orderDTO.setStatus(order.getStatus());
				orderDTO.setTotal(order.getTotal());
				orderDTO.setTotalDiscount(order.getTotalDiscount());
				orderDTO.setTotalTiketPrice(order.getTotalTiketPrice());
				orderDTO.setOrderCreateDate(order.getCreatedDate());
//				orderDTO.setUser(order.getUser());
				UserDTO userDTO = userMapper.toDto(order.getUser());
				
				// set data full address
				StringBuilder address = new StringBuilder();
				if(userDTO.getAddressDescription() != null && userDTO.getAddressDescription().length()>0) {
					address.append(userDTO.getAddressDescription());
					address.append(", ");
				}
				RegionDetail district = detailRepository.findById(userDTO.getAddressId()).get();
				address.append(district.getFullName());
				if(district != null && district.getRegionParent() != null) {
					RegionDetail province = detailRepository.findById(district.getRegionParent().getId()).get();
					address.append(", ");
					address.append(province.getFullName());
				}
				userDTO.setAddress(address.toString());
				orderDTO.setUserDTO(userDTO);
				
				List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
				List<OrderDetailDTO> detailDTOs = orderDetailMapper.toDto(orderDetails);
				orderDTO.setOrderDetails(detailDTOs);
				if(order.getPromotionDetail() != null) {
					PromotionDetail promotionDetail = order.getPromotionDetail();
					PromotionLine promotionLine = promotionDetail.getPromotionLine();
					Promotion promotion = promotionLine.getPromotion();
					PromotionDTO promotionDTO = new PromotionDTO();
					promotionDTO.setConditionApply(promotionDetail.getConditionApply());
					promotionDTO.setDiscount(promotionDetail.getDiscount());
					if(promotionDetail.getMaxDiscount() != null ) {
						promotionDTO.setMaxDiscount(promotionDetail.getMaxDiscount());
					}
					promotionDTO.setPromotionLineName(promotionLine.getLineName());
					promotionDTO.setPromotionType(promotionLine.getPromotionType());
					promotionDTO.setPromotionCode(promotion.getCode());	
					orderDTO.setPromotionDTO(promotionDTO);
				}
				dataResponse.setResponseMsg("Get order success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data",orderDTO);
				dataResponse.setValueReponse(respValue);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getOrderByCurrentUser(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataResponse getListOrderByUserId(Long userId) {
		DataResponse dataResponse = new DataResponse();
		try {
			User user = userRepository.getById(userId);
			if(user == null) {
				throw new Exception();
			}else {
				List<Order> orders = orderRepository.findByUserUserIdAndStatusOrderByCreatedDateDescIdDesc(userId,1);
				List<OrderManagerDTO> ordeDtos = orderMapper.toDto(orders);
				dataResponse.setResponseMsg("Get orders success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data",ordeDtos);
				dataResponse.setValueReponse(respValue);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
