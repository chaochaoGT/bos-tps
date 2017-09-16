package cn.itcast.bos.dao.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.auth.Function;

public interface FunctionDao extends JpaSpecificationExecutor<Function>, JpaRepository<Function, String> {

}
