package actor.config;

import config.ConfigCenter;

public class CtActorConfig {
	private String CT_ANALYSE_RESULT_SAVE_ROOT = ConfigCenter.getString("ct.analyse.result.save.root");
	private String CT_ANALYSE_RESULT_SAVE_FORMAT=ConfigCenter.getString("ct.analyse.result.save.format");
	private Integer CT_THREAD_POOL_SIZE = 3;

	public String getCT_ANALYSE_RESULT_SAVE_ROOT() {
		return CT_ANALYSE_RESULT_SAVE_ROOT;
	}

	public void setCT_ANALYSE_RESULT_SAVE_ROOT(String CT_ANALYSE_RESULT_SAVE_ROOT) {
		this.CT_ANALYSE_RESULT_SAVE_ROOT = CT_ANALYSE_RESULT_SAVE_ROOT;
	}

	public Integer getCT_THREAD_POOL_SIZE() {
		return CT_THREAD_POOL_SIZE;
	}

	public String getCT_ANALYSE_RESULT_SAVE_FORMAT() {
		return CT_ANALYSE_RESULT_SAVE_FORMAT;
	}
}
