package cn.itcast.bos.action.bc;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.utils.DownLoadUtils;
import cn.itcast.bos.utils.PinYin4jUtils;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class RegionAction extends BaseAction<Region> {
	// @Action(value = "staffAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/staff.jsp") })
	// public String save() {
	// // 添加 又是修改
	// try {
	// facadeService.getStaffService().save(model);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "save";
	// }
	// 优化查询
	@Action(value = "regionAction_pageQuery")
	public String pageQuery() {
		String string = facadeService.getRegionService().pageQueryByRedis(getPageRequest());
		try {
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().println(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;// 分页查询结果集名称
	}

	// @Action(value = "regionAction_pageQuery")
	// public String pageQuery() {
	// Page<Region> pageData = facadeService.getRegionService().pageQuery(getPageRequest());
	// setPageData(pageData);// {total rows}
	// return "pageQuery";// 分页查询结果集名称
	// }
	@Autowired
	private DataSource dataSource;// 注入连接池 数据源对象

	// 报表之Jasperreport导出
	@Action(value = "regionAction_export1")
	public String export1() {
		try {
			// 1: 加载设计文件 report2.jrxml
			String path = ServletActionContext.getServletContext().getRealPath("/jr/heima04_.jrxml");
			// 2: 报表 parameter 赋值 需要Map 集合
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("company", "上海传智播客");
			// 3: 编译该文件 JasperCompilerManager
			JasperReport report = JasperCompileManager.compileReport(path);
			// 4: JapserPrint = JasperFillManager.fillReport(report,map,connection)
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());
			// 5: 下载 准备一个流 两个头
			HttpServletResponse response = getResponse();
			ServletOutputStream outputStream = response.getOutputStream();
			String filename = "区域列表信息.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			// 6: JapdfExport 定义报表输出源
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// 7: 导出
			exporter.exportReport();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;

	}

	// 报表之Itext 实现pdf导出编码说明
	@Action(value = "regionAction_export")
	public String export() {
		// 1: 数据库最新的数据
		List<Region> regions = facadeService.getRegionService().findAll();
		// 2: 生成pdf --->Document 对象 具有一定格式数据 ... Table 对象 填充到Document对象中
		// Table 行和列
		// itext 报表 下载
		try {
			Document document = new Document(PageSize.A4);
			// response
			HttpServletResponse response = getResponse();
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			// pdf 可以设置密码
			writer.setEncryption("itcast".getBytes(), "heima04".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
			// 浏览器下载 ...两个头
			String filename = new Date(System.currentTimeMillis()).toLocaleString() + "_区域数据.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));// mime 类型
			response.setHeader("Content-Disposition", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			// 打开文档
			document.open();
			Table table = new Table(5, regions.size() + 1);// 5列 行号 0 开始
			// table.setBorderWidth(1f);
			// table.setAlignment(1);// // 其中1为居中对齐，2为右对齐，3为左对齐
			// table.setBorder(1); // 边框
			// table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
			// table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
			// 设置表格字体
			BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
			// 表头
			table.addCell(buildCell("省", font));
			table.addCell(buildCell("市", font));
			table.addCell(buildCell("区", font));
			table.addCell(buildCell("邮政编码", font));
			table.addCell(buildCell("简码", font));

			// 表格数据
			for (Region region : regions) {
				table.addCell(buildCell(region.getProvince(), font));
				table.addCell(buildCell(region.getCity(), font));
				table.addCell(buildCell(region.getDistrict(), font));
				table.addCell(buildCell(region.getPostcode(), font));
				table.addCell(buildCell(region.getShortcode(), font));
			}
			// 向文档添加表格
			document.add(table);
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return NONE;// 称
	}

	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		Cell cell = new Cell(phrase);
		// 设置垂直居中
		// cell.setVerticalAlignment(1);
		// 设置水平居中
		// cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	// struts2 接受上传文件
	private File upload;

	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Action(value = "regionAction_oneclickupload", results = { @Result(name = "oneclickupload", type = "json") })
	public String oneclickupload() {
		// 服务器 接受excel 1: struts2框架如何接受上传文件 源码 翻翻 2: poi如何解析 百度查查 3: List<Region> 4: spring data jpa 批量更新代码
		try {
			// 1: 加载excel文件 编程 工作簿对象
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
			// 2: sheet
			HSSFSheet sheet = workbook.getSheetAt(0);
			// 3: 第一行数据不需要封装List
			List<Region> regions = new ArrayList<Region>();
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				Region region = new Region();
				region.setId(row.getCell(0).getStringCellValue());
				String province = row.getCell(1).getStringCellValue();
				region.setProvince(province);// 省
				String city = row.getCell(2).getStringCellValue();
				region.setCity(city);// 市
				String district = row.getCell(3).getStringCellValue();
				region.setDistrict(district);// 区
				region.setPostcode(row.getCell(4).getStringCellValue());

				// 城市简码
				city = city.substring(0, city.length() - 1);// 去掉最后一个字 市 南京
				region.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));// 城市中文拼音组成
																			// pinyin4j
																			// 完成
																			// !
				// 简码制作 江苏省南京市鼓楼区 上海市 上海市 浦东新区
				// 江苏南京鼓楼 ----->JSNJGL
				province = province.substring(0, province.length() - 1);// 去掉最后一个中文字省
				district = district.substring(0, district.length() - 1);// 去掉最后一个字区
				String[] strings;// 数组存放所有的大写字母
				if (province.equalsIgnoreCase(city)) {
					// 直辖市
					strings = PinYin4jUtils.getHeadByString(province + district);
				} else {
					// 非直辖市
					strings = PinYin4jUtils.getHeadByString(province + city + district);
				}
				// 省市区关键字首字母 拼接字符串 遍历
				String shortcode = getHeadFromArray(strings);// 将数组里面所有的大写字母 拼接成一个字符串
				region.setShortcode(shortcode); // 省市区 每一个中文字首字母大写组成
				regions.add(region);

			}
			facadeService.getRegionService().save(regions);
			push(true);
		} catch (IOException e) {
			push(false);
			e.printStackTrace();
		}
		return "oneclickupload";
	}

	// 将数组里面存放的首字母拼接成字符串
	private String getHeadFromArray(String[] strings) {
		if (strings == null || strings.length == 0) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String s : strings) {
				sb.append(s);
			}
			return sb.toString();
		}
	}

	@Action(value = "regionAction_ajaxList", results = { @Result(name = "ajaxList", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String ajaxList() {
		String param = getParameter("q");
		List<Region> regions = facadeService.getRegionService().findAll(param);
		push(regions);// 需要 id name
		return "ajaxList";
	}
}
