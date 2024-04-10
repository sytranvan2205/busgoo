package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.InvoiceDTO;
import com.iuh.busgoo.dto.OrderDetailDTO;
import com.iuh.busgoo.dto.OrderManagerDTO;
import com.iuh.busgoo.entity.Invoice;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.entity.OrderDetail;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.entity.SeatOrder;
import com.iuh.busgoo.entity.TimeTable;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.filter.InvoiceFilter;
import com.iuh.busgoo.mapper.InvoiceMapper;
import com.iuh.busgoo.mapper.OrderDetailMapper;
import com.iuh.busgoo.repository.InvoiceRepository;
import com.iuh.busgoo.repository.OrderDetailRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.SeatOrderRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.ReturnRequest;
import com.iuh.busgoo.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceMapper invoiceMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private RegionDetailRepository regionDetailRepository;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private SeatOrderRepository seatOrderRepository;

	@Override
	public DataResponse getInvoiceByFilter(InvoiceFilter invoiceFilter) {

		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if(invoiceFilter.getSortBy() != null && invoiceFilter.getOrderBy() != null) {
				if(invoiceFilter.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(invoiceFilter.getOrderBy()).ascending();
				}else {
					sort = Sort.by(invoiceFilter.getOrderBy()).descending();
				}
				page= PageRequest.of(invoiceFilter.getPage(), invoiceFilter.getItemPerPage(), sort);
			}else {
				page = PageRequest.of(invoiceFilter.getPage(), invoiceFilter.getItemPerPage());
			}
			Page<Invoice> invoicePage = invoiceRepository.findPageFilter(invoiceFilter.getStatus(),invoiceFilter.getFromDate(),invoiceFilter.getToDate(),invoiceFilter.getQ(),page);
			List<Invoice> invoices = invoicePage.getContent();
			List<InvoiceDTO> invoiceDTOs = invoiceMapper.toDto(invoices);
			for(InvoiceDTO dto: invoiceDTOs) {
				User user = userRepository.getById(dto.getUserId());
				if(user == null) {
					throw new Exception();
				}else {
					dto.setUserCode(user.getUserCode());
					dto.setUserName(user.getFullName());
				}
				Order order = orderRepository.getById(dto.getOrderId());
				if(order == null) {
					throw new Exception();
				}else {
					List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
					StringBuilder strSeatName = new StringBuilder();
					for(OrderDetail detail:orderDetails) {
						strSeatName.append(detail.getSeat().getSeatName());
						strSeatName.append(", ");
					}
					String seatName = strSeatName.toString().trim();
					if (seatName.length()>0) {
						seatName = seatName.substring(0,(seatName.length()-1));
					}
					dto.setStrLstSeatName(seatName);
					TimeTable timeTable = orderDetails.get(0).getSeat().getTimeTable();
					Route route = timeTable.getRoute();
					String bustrip = route.getFrom().getFullName()+" - "+ route.getTo().getFullName();
					dto.setBusTrip(bustrip);
				}
			}
			Page<InvoiceDTO> invoiceDTOPage = new PageImpl<>(invoiceDTOs, page, invoicePage.getTotalElements());
			
			dataResponse.setResponseMsg("Get invoice success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",invoiceDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	
	}

	@Override
	public DataResponse getInvoiceById(Long invoiceId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Invoice invoice = invoiceRepository.getById(invoiceId);
			if(invoice == null) {
				throw new Exception();
			}else {
				InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
				User user = userRepository.getById(invoiceDTO.getUserId());
				if(user == null) {
					throw new Exception();
				}else {
					invoiceDTO.setUserCode(user.getUserCode());
					invoiceDTO.setUserName(user.getFullName());
				}
				// set data full address
				StringBuilder address = new StringBuilder();
				if(user.getAddressDescription() != null && user.getAddressDescription().length()>0) {
					address.append(user.getAddressDescription());
					address.append(", ");
				}
				RegionDetail district = regionDetailRepository.findById(user.getRegeionDetail().getId()).get();
				address.append(district.getFullName());
				if(district != null && district.getRegionParent() != null) {
					RegionDetail province = regionDetailRepository.findById(district.getRegionParent().getId()).get();
					address.append(", ");
					address.append(province.getFullName());
				}
				invoiceDTO.setUserAddress(address.toString());
				List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(invoice.getOrderId());
				List<OrderDetailDTO> orderDetailDTOs = orderDetailMapper.toDto(orderDetails);
				invoiceDTO.setList(orderDetailDTOs);
				TimeTable timeTable = orderDetails.get(0).getSeat().getTimeTable();
				Route route = timeTable.getRoute();
				String bustrip = route.getFrom().getFullName()+" - "+ route.getTo().getFullName();
				invoiceDTO.setBusTrip(bustrip);
				invoiceDTO.setDateStarted(invoiceDTO.getTimeStarted().toLocalDate());
				invoiceDTO.setStartedTime(invoiceDTO.getTimeStarted().toLocalTime());
				dataResponse.setResponseMsg("Get invoice success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data",invoiceDTO);
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
	public DataResponse returnInvoice(ReturnRequest returnRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			LocalDate currDate = LocalDate.now();
			Invoice invoice = invoiceRepository.getById(returnRequest.getInvoiceId());
			if(invoice == null) {
				throw new Exception();
			}else {
				LocalDate timeStarted = invoice.getTimeStarted().toLocalDate();
				if(currDate.plusDays(2).isBefore(timeStarted)) {
					if(returnRequest.getReason() == null || returnRequest.getReason().trim().length()==0) {
						dataResponse.setResponseMsg("The reason for return cannot be left blank.");
						dataResponse.setRespType(Constant.RETURN_INVOICE_FAILED);
						return dataResponse;
					}else {
						invoice.setStatus(0);
						invoice.setReason(returnRequest.getReason());
						invoiceRepository.save(invoice);
						//update lại trạng thái hóa đơn
						Order order = orderRepository.getById(invoice.getOrderId());
						order.setStatus(0);
						orderRepository.save(order);
						List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
						for(OrderDetail orderDetail : orderDetails) {
							//update lại ghế trống
							SeatOrder seatOrder = orderDetail.getSeat();
							seatOrder.setIsAvailable(true);
							seatOrderRepository.save(seatOrder);
							//update lại trạng thái chi tiết hóa đơn
//							orderDetail.setSeat(null);
//							orderDetailRepository.save(orderDetail);
						}
						dataResponse.setResponseMsg("Return order success !!!");
						dataResponse.setRespType(Constant.HTTP_SUCCESS);
						return dataResponse;
					}
				}else {
					dataResponse.setResponseMsg("The invoice due date must be at least 2 days before the departure date.");
					dataResponse.setRespType(Constant.RETURN_INVOICE_FAILED);
					return dataResponse;
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}


}
