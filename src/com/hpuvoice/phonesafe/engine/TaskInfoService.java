package com.hpuvoice.phonesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class TaskInfoService {

	public static List<TaskInfo> geTaskInfos(Context context) {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		ActivityManager amManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> rap = amManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : rap) {
			TaskInfo info = new TaskInfo();
			String packagename = runningAppProcessInfo.processName;
			info.setPackagename(packagename);

			int[] pids = new int[] { runningAppProcessInfo.pid };
			MemoryInfo[] processMemoryInfo = amManager
					.getProcessMemoryInfo(pids);
			long menmory = processMemoryInfo[0].getTotalPss() * 1024;
			info.setMemory(menmory);

			try {
				PackageManager pManager = context.getPackageManager();
				ApplicationInfo applicationInfo = pManager.getApplicationInfo(
						packagename, 0);
				String name = applicationInfo.loadLabel(pManager).toString();
				info.setAppname(name);
				Drawable icon = applicationInfo.loadIcon(pManager);
				info.setIcon(icon);
				boolean isSystem = taskFilter(applicationInfo);
				info.setSystemApp(isSystem);
			} catch (Exception e) {
				e.printStackTrace();
			}
			taskInfos.add(info);
		}

		return taskInfos;
	}

	public static boolean taskFilter(ApplicationInfo applicationInfo) {
		if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
			return true;
		} else {
			return false;
		}
	}

}
