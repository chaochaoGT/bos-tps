package cn.itcast.bos.action.bc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.bc.SubareaModel;
import cn.itcast.bos.service.base.SubareaCondtion;
import cn.itcast.bos.utils.DownLoadUtils;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class SubareaAction extends BaseAction<Subarea> {
	@Action(value = "subareaAction_noassociation", results = { @Result(name = "noassociation", type = "fastjson", params = { "includeProperties", "sid,addresskey,position" }) })
	public String noassociation() {
		// 添加 又是修改
		List<Subarea> subareas = facadeService.getSubareaService().noassociation();
		push(subareas);
		return "noassociation";
	}

	@Action(value = "subareaAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/subarea.jsp") })
	public String save() {
		// 添加 又是修改
		facadeService.getSubareaService().save(model);
		return "save";
	}

	@Action(value = "subareaAction_download")
	public String download() {
		try {
			// 添加 又是修改
			SubareaModel sub = new SubareaModel();
			BeanUtils.copyProperties(model, sub);
			SubareaCondtion<Subarea> spec = new SubareaCondtion<Subarea>(sub);
			List<Subarea> datas = facadeService.getSubareaService().findAllBySpecification(spec);
			// 2: 下载 编写工作簿对象
			// workbook sheet row cell
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("分区数据1");
			// excel标题
			HSSFRow first = sheet.createRow(0);
			first.createCell(0).setCellValue("分区编号");
			first.createCell(1).setCellValue("省");
			first.createCell(2).setCellValue("市");
			first.createCell(3).setCellValue("区");
			first.createCell(4).setCellValue("关键字");
			first.createCell(5).setCellValue("起始号");
			first.createCell(6).setCellValue("终止号");
			first.createCell(7).setCellValue("单双号");
			first.createCell(8).setCellValue("位置");
			// 数据体
			if (datas != null && datas.size() != 0) {
				for (Subarea s : datas) {
					// 循环一次创建一行
					int lastRowNum = sheet.getLastRowNum();// 获取当前excel最后一行行号
					HSSFRow row = sheet.createRow(lastRowNum + 1);
					row.createCell(0).setCellValue(s.getId());
					row.createCell(1).setCellValue(s.getRegion().getProvince());
					row.createCell(2).setCellValue(s.getRegion().getCity());
					row.createCell(3).setCellValue(s.getRegion().getDistrict());
					row.createCell(4).setCellValue(s.getAddresskey());
					row.createCell(5).setCellValue(s.getStartnum());
					row.createCell(6).setCellValue(s.getEndnum());
					row.createCell(7).setCellValue(s.getSingle() + "");
					row.createCell(8).setCellValue(s.getPosition());
				}
				// 第一个sheet数据完成
			}
			// 下载
			String filename = "分区数据.xls";
			HttpServletResponse response = getResponse();
			response.setHeader("Content-Disposition	", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename, getRequest().getHeader("user-agent")));
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			book.write(response.getOutputStream());
			return NONE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// @Action(value = "subareaAction_pageQuery")
	// public String pageQuery() {
	// // Specification<Subarea> spec = getSpecification();
	// // 2: 调用
	// SubareaModel sub = new SubareaModel();
	// BeanUtils.copyProperties(model, sub);
	// SubareaCondtion<Subarea> spec = new SubareaCondtion<Subarea>(sub);
	// Page<Subarea> pageData = facadeService.getSubareaService().pageQuery(getPageRequest(), spec);
	// setPageData(pageData);// 父类
	// return "pageQuery";
	// }

	@Action(value = "subareaAction_pageQuery")
	public String pageQuery() {
		SubareaModel sub = new SubareaModel();
		BeanUtils.copyProperties(model, sub);
		System.out.println(sub.getAddresskey() + "---------条件关键字查询---------------");
		SubareaCondtion<Subarea> spec = new SubareaCondtion<Subarea>(sub);
		Map<String, Object> conditions = getConditions();
		String string = facadeService.getSubareaService().pageQueryByRedisCacahe(getPageRequest(), spec, conditions);
		try {
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().println(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;// 分页查询结果集名称
	}

	private Map<String, Object> getConditions() {
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(model.getAddresskey())) {
			conditions.put("addresskey", model.getAddresskey());
		}
		// 主表Subarea n 连接从表 1 Region
		// 多方连接一方
		if (model.getRegion() != null) {

			if (StringUtils.isNotBlank(model.getRegion().getProvince())) {
				conditions.put("region.province", model.getRegion().getProvince());
			}
			if (StringUtils.isNotBlank(model.getRegion().getCity())) {
				conditions.put("region.city", model.getRegion().getCity());
			}
			if (StringUtils.isNotBlank(model.getRegion().getDistrict())) {
				conditions.put("region.district", model.getRegion().getDistrict());
			}
		}
		// 定区 比较对象 比较OID
		if (model.getDecidedZone() != null && StringUtils.isNotBlank(model.getDecidedZone().getId())) {
			conditions.put("decidedzone.id", model.getDecidedZone().getId());
		}
		return conditions;
	}

	// private SubareaCondtion<Subarea> getSpecification() {
	// SubareaCondtion<Subarea> condtion = new SubareaCondtion<Subarea>(model);
	// return condtion;
	// }

}
