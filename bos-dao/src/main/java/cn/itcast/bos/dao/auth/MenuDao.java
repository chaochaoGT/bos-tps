package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Menu;

public interface MenuDao extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {
	@Query("from Menu where generatemenu = 1 order by zindex desc")
	public List<Menu> ajaxListHasSonMenus();

	// user role menu
	@Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id = ?1 order by m.zindex desc")
	public List<Menu> findMenuByUserId(Integer id);

}
