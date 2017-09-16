package cn.itcast.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.qp.NoticeBill;

public interface NoticeBillDao extends JpaRepository<NoticeBill, String>, JpaSpecificationExecutor<NoticeBill> {

}
