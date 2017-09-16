package cn.itcast.bos.service.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.SubareaModel;

public class SubareaCondtion<Subarea> implements Specification<Subarea> {
	private SubareaModel model;

	public SubareaCondtion(SubareaModel obj) {
		this.model = obj;
	}

	@Override
	public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		// root Subarea cb 建立条件
		List<Predicate> predicates = new ArrayList<Predicate>();

		// subarea表添加条件n 1 Region表添加条件 1 定区编号查询 OID
		// 单表连接自己
		if (StringUtils.isNotBlank(model.getAddresskey())) {
			Predicate p1 = cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%");
			// p1 满足查询对象
			predicates.add(p1);
		}
		// 主表Subarea n 连接从表 1 Region
		// 多方连接一方
		if (model.getRegion() != null) {
			// 连接 region表 regionJoin 操作region表
			Join<Subarea, Region> regionJoin = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.LEFT);
			// 多方 去连接一方 ....
			if (StringUtils.isNotBlank(model.getRegion().getProvince())) {
				Predicate p2 = cb.like(regionJoin.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%");
				// p2 满足查询对象
				predicates.add(p2);
			}
			if (StringUtils.isNotBlank(model.getRegion().getCity())) {
				Predicate p3 = cb.like(regionJoin.get("city").as(String.class), "%" + model.getRegion().getCity() + "%");
				// p2 满足查询对象
				predicates.add(p3);
			}
			if (StringUtils.isNotBlank(model.getRegion().getDistrict())) {
				Predicate p4 = cb.like(regionJoin.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%");
				// p2 满足查询对象
				predicates.add(p4);
			}
		}
		// 定区 比较对象 比较OID
		if (model.getDecidedZone() != null && StringUtils.isNotBlank(model.getDecidedZone().getId())) {
			Predicate p5 = cb.equal(root.get("decidedZone").as(DecidedZone.class), model.getDecidedZone());
			// p3 满足查询对象
			predicates.add(p5);
		}

		Predicate ps[] = new Predicate[predicates.size()];
		return cb.and(predicates.toArray(ps));
	}

}
