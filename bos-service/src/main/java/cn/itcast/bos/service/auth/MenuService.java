package cn.itcast.bos.service.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.auth.Menu;

public interface MenuService {

	List<Menu> ajaxListHasSonMenus();

	Page<Menu> pageQuery(PageRequest pageRequest);

	void save(Menu model);

	List<Menu> ajaxList();

	List<Menu> findMenuByUserId(Integer id);

}
