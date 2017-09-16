package cn.itcast.bos.service.base;

import java.util.ResourceBundle;

public interface BaseInterface {
	public static final String CRM_BASE_URL = ResourceBundle.getBundle("crm").getString("crm_base_url");
}
