package ecg.model;

import java.util.List;
//同步结果
public class SyncResult {
	List<GuardianData> guardian_datas;			//看护者数据模型，转换成列表格式
	List<PumpSpeedData> pump_speed_datas;		//泵速度数据模型
	List<PressureData> pressure_datas;			//压力数据模型

	public SyncResult(List<GuardianData> guardian_datas,
			List<PumpSpeedData> pump_speed_datas,
			List<PressureData> pressure_datas) {	//构造方法SyncResult
		this.guardian_datas=guardian_datas;
		this.pump_speed_datas=pump_speed_datas;
		this.pressure_datas=pressure_datas;
	}
}
