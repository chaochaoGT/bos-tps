package cn.itcast.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.service.auth.MenuService;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.bos.service.bc.RegionService;
import cn.itcast.bos.service.bc.StaffService;
import cn.itcast.bos.service.bc.StandardService;
import cn.itcast.bos.service.bc.SubareaService;
import cn.itcast.bos.service.city.CityService;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.bos.service.user.UserService;

@Service
public class FacadeService {
	@Autowired
	private UserService userService;
	@Autowired
	private StandardService standardService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private CityService cityService;
	@Autowired
	private SubareaService subareaService;
	@Autowired
	private DecidedZoneService decidedZoneService;
	@Autowired
	private NoticeBillService noticeBillService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;

	public UserService getUserService() {
		return userService;
	}

	public StandardService getStandardService() {
		// TODO Auto-generated method stub
		return standardService;
	}

	public StaffService getStaffService() {
		// TODO Auto-generated method stub
		return staffService;
	}

	public RegionService getRegionService() {
		// TODO Auto-generated method stub
		return regionService;
	}

	public CityService getCityService() {
		// TODO Auto-generated method stub
		return cityService;
	}

	public SubareaService getSubareaService() {
		// TODO Auto-generated method stub
		return subareaService;
	}

	public DecidedZoneService getDecidedZoneService() {
		// TODO Auto-generated method stub
		return decidedZoneService;
	}

	public NoticeBillService getNoticeBillService() {
		// TODO Auto-generated method stub
		return noticeBillService;
	}

	public FunctionService getFunctionService() {
		// TODO Auto-generated method stub
		return functionService;
	}

	public MenuService getMenuService() {
		// TODO Auto-generated method stub
		return menuService;
	}

	public RoleService getRoleService() {
		// TODO Auto-generated method stub
		return roleService;
	}
}
