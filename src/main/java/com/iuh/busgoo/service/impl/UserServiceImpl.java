package com.iuh.busgoo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.UserDTO;
import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.mapper.UserMapper;
import com.iuh.busgoo.repository.AccountRepository;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.ChangePassRequest;
import com.iuh.busgoo.requestType.UserCreateRequest;
import com.iuh.busgoo.requestType.UserUpdateRequest;
import com.iuh.busgoo.service.UserService;
import com.iuh.busgoo.utils.PageUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RegionDetailRepository regionDetailRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User findUserByCode(String code) {
		return userRepo.getUserByUserCode(code);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}

	@Override
	public DataResponse findUserActive(Pageable pageable) {
		DataResponse dataResponse = new DataResponse();
		try {
			List<User> users = userRepo.findByStatus(1);
			if(users != null && users.size() > 0) {
				Page<User> pageUser = PageUtils.createPageFromList(users, pageable);
				dataResponse.setResponseMsg("Get user success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<String, Object>();
				reponseValue.put("data", pageUser);
				dataResponse.setValueReponse(reponseValue);
				return dataResponse;
			}else {
				dataResponse.setResponseMsg("Get user success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createUser(UserCreateRequest userCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(userCreateRequest == null) {
				throw new Exception();
			}else {
				User checkExist = userRepo.findByPhoneAndStatus(userCreateRequest.getPhone(),1);
				if(checkExist != null) {
					dataResponse.setResponseMsg("User already exists");
					dataResponse.setRespType(Constant.USER_HAS_EXIST);
					return dataResponse;
				}else {
					RegionDetail regionDetail;
					User user = new User();
					Long count = userRepo.count();
					user.setUserCode("US"+(count++));
					user.setFullName(userCreateRequest.getFullName());
					user.setPhone(userCreateRequest.getPhone());
					Optional<RegionDetail> otpRegionDetail = regionDetailRepository.findById(userCreateRequest.getRegeionDetailId());
					if(otpRegionDetail != null && otpRegionDetail.get() != null) {
						regionDetail = otpRegionDetail.get();
					}else {
						throw new Exception();
					}
					if(userCreateRequest.getAddressDescription()!= null && userCreateRequest.getAddressDescription().length()>0) {
						user.setAddressDescription(userCreateRequest.getAddressDescription());
					}
					user.setRegeionDetail(regionDetail);
					user.setStatus(1);
					userRepo.save(user);
					
					dataResponse.setResponseMsg("Create user success !!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> respValue = new HashMap<>();
					respValue.put("data",user);
					dataResponse.setValueReponse(respValue);
					return dataResponse;
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findUserByPhone(String phone) {
		DataResponse dataResponse = new DataResponse();
		try {
			User user = userRepo.findByPhoneAndStatus(phone,1);
			UserDTO userDTO = userMapper.toDto(user);
			StringBuilder address = new StringBuilder();
			if(userDTO.getAddressDescription() != null && userDTO.getAddressDescription().length()>0) {
				address.append(userDTO.getAddressDescription());
				address.append(", ");
			}
			RegionDetail district = regionDetailRepository.findById(userDTO.getAddressId()).get();
			address.append(district.getFullName());
			if(district != null && district.getRegionParent() != null) {
				RegionDetail province = regionDetailRepository.findById(district.getRegionParent().getId()).get();
				address.append(", ");
				address.append(province.getFullName());
			}
			userDTO.setAddress(address.toString());
			dataResponse.setResponseMsg("Get user success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",userDTO);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findUserByFilter(FilterUserRq filterUserRq) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if(filterUserRq.getSortBy() != null && filterUserRq.getOrderBy() != null) {
				if(filterUserRq.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(filterUserRq.getOrderBy()).ascending();
				}else {
					sort = Sort.by(filterUserRq.getOrderBy()).descending();
				}
				page= PageRequest.of(filterUserRq.getPage(), filterUserRq.getItemPerPage(), sort);
			}else {
				page = PageRequest.of(filterUserRq.getPage(), filterUserRq.getItemPerPage());
			}
			Page<User> pageUsers;
			if(filterUserRq.getStatus() == null && filterUserRq.getQ() == null) {
				pageUsers = userRepo.findAll(page);
			}else if(filterUserRq.getStatus() != null && filterUserRq.getQ() == null) {
				pageUsers = userRepo.findByStatus(filterUserRq.getStatus(), page);
			}else if(filterUserRq.getStatus() == null && filterUserRq.getQ() != null) {
				pageUsers = userRepo.findByFullNameContaining(filterUserRq.getQ(), page);
			}else {
				pageUsers = userRepo.findByStatusAndFullNameContaining(filterUserRq.getStatus(),filterUserRq.getQ(), page);
			}
			List<User> users = new ArrayList<User>();
			if(pageUsers.getContent().size() > 0) {
				users = pageUsers.getContent();
			}
			List<UserDTO> dtos = userMapper.toDto(users);
			for(UserDTO dto : dtos) {
				StringBuilder address = new StringBuilder();
				if(dto.getAddressDescription() != null && dto.getAddressDescription().length()>0) {
					address.append(dto.getAddressDescription());
					address.append(", ");
				}
				if(dto.getAddressId() != null) {
					RegionDetail district = regionDetailRepository.findById(dto.getAddressId()).get();
					address.append(district.getFullName());
					if (district != null && district.getRegionParent() != null) {
						RegionDetail province = regionDetailRepository.findById(district.getRegionParent().getId())
								.get();
						address.append(", ");
						address.append(province.getFullName());
					}
					dto.setAddress(address.toString());
				}
			}
			Page<UserDTO> userDtoPage = new PageImpl<>(dtos, page, pageUsers.getTotalElements());
//			Page<User> pageUsers = userRepo.findAll(page);
//			Page<User> pageUser = userRepo.findAll(page);
			dataResponse.setResponseMsg("Get data success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", userDtoPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			e.printStackTrace();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getUserById(Long userId) {
		DataResponse dataResponse = new DataResponse();
		try {
			User user = userRepo.findById(userId).get();
			dataResponse.setResponseMsg("Get data success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", user);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			e.printStackTrace();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deleteUser(Long id) {
		DataResponse response = new DataResponse();
		try {
			User user = userRepo.findById(id).get();
			if (user == null) {
				response.setResponseMsg("User is not exist");
				response.setRespType(Constant.USER_NOT_EXIST);
				return response;
			} else {
				user.setStatus(0);
				userRepo.save(user);
				response.setResponseMsg("Delete success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse changePass(ChangePassRequest changePassRequest) {
		DataResponse response = new DataResponse();
		try {
			if (changePassRequest.getEmail() == null || changePassRequest.getEmail().length()==0) {
				throw new Exception();
			}
			Account account = accountRepository.findAccountByEmail(changePassRequest.getEmail());
			if(bCryptPasswordEncoder.matches(changePassRequest.getPassword(), account.getPassword())) {
				if(bCryptPasswordEncoder.matches(changePassRequest.getNewPassword(), account.getPassword())) {
					response.setResponseMsg("The new password cannot be the same as the old password!");
					response.setRespType(Constant.CHANGE_PASS_FAIL);
					return response;
				}else {
					account.setPassword(passwordEncoder.encode(changePassRequest.getNewPassword()));
				}
				accountRepository.save(account);
				response.setResponseMsg("Change password success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				return response;
			}else {
				response.setResponseMsg("The new password cannot be the same as the old password!");
				response.setRespType(Constant.CHANGE_PASS_FAIL);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse changeInfo(UserUpdateRequest userUpdateRequest) {
		DataResponse response = new DataResponse();
		try {
			User user = userRepo.getById(userUpdateRequest.getUserId());
			RegionDetail regionDetail = regionDetailRepository.getById(userUpdateRequest.getAddressId());
			if (user == null || regionDetail == null) {
				throw new Exception();
			}else {
				user.setFullName(userUpdateRequest.getUserName());
				user.setAddressDescription(userUpdateRequest.getAddressDesciption());
				user.setRegeionDetail(regionDetail);
				userRepo.save(user);
				response.setResponseMsg("Information updated success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}
	
	

}
