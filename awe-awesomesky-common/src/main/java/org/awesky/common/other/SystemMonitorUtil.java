package org.awesky.common.other;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import com.sun.management.OperatingSystemMXBean;

public class SystemMonitorUtil {

	private static final int CPUTIME = 5000;   
    private static final int PERCENT = 100;   
    private static final int FAULTLENGTH = 10;
    private static final int SIZE = 1024;
    
    public static SystemMonitorInfo getSystemInfo() {
        // 可使用内存   
        long totalMemory = Runtime.getRuntime().totalMemory() / SIZE;   
        // 剩余内存   
        long freeMemory = Runtime.getRuntime().freeMemory() / SIZE;   
        // 最大可使用内存   
        long maxMemory = Runtime.getRuntime().maxMemory() / SIZE; 
        
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();   
        // 操作系统   
        String osName = System.getProperty("os.name");   
        // 总的物理内存   
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / SIZE;   
        // 剩余的物理内存   
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / SIZE;   
        // 已使用的物理内存   
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb   
                .getFreePhysicalMemorySize())   
                / SIZE;   
        // 获得线程总数   
        ThreadGroup parentThread;   
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread   
                .getParent() != null; parentThread = parentThread.getParent()); 
        int totalThread = parentThread.activeCount();   
  
        int cpuRatio = 0;   
        if (osName.toLowerCase().startsWith("windows")) {   
            cpuRatio = (int)getCpuRatioForWindows();   
        }   
        int memoryRatio = (int) ((1 - usedMemory * 1.0 / totalMemorySize) * 100);
        // 构造返回对象   
        SystemMonitorInfo infoBean = new SystemMonitorInfo();   
        infoBean.setFreeMemory(freeMemory);   
        infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);   
        infoBean.setMaxMemory(maxMemory);   
        infoBean.setOsName(osName);   
        infoBean.setTotalMemory(totalMemory);   
        infoBean.setTotalMemorySize(totalMemorySize);   
        infoBean.setTotalThread(totalThread);   
        infoBean.setUsedMemory(usedMemory);   
        infoBean.setCpuRatio(cpuRatio);   
        infoBean.setMemoryRatio(memoryRatio);
        return infoBean;   
    }
    
    /**
	 * 获取CPU使用率
	 * @return
	 */
	private static double getCpuRatioForWindows() {
		try {   
            String procCmd = System.getenv("windir")   
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";   
            // 取进程信息   
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));   
            Thread.sleep(CPUTIME);   
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));   
            if (c0 != null && c1 != null) {   
                long idletime = c1[0] - c0[0];   
                long busytime = c1[1] - c0[1];   
                return Double.valueOf(   
                        PERCENT * (busytime) / (busytime + idletime))   
                        .doubleValue();   
            } else {   
                return 0.0;   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
            return 0.0;   
        }   
	}

	private static long[] readCpu(Process proc) {
		  long[] retn = new long[2];   
          try {   
              proc.getOutputStream().close();   
              InputStreamReader ir = new InputStreamReader(proc.getInputStream());   
              LineNumberReader input = new LineNumberReader(ir);   
              String line = input.readLine();  
              if (line == null || line.length() < FAULTLENGTH) {   
                  return null;   
              }   
              int capidx = line.indexOf("Caption");   
              int cmdidx = line.indexOf("CommandLine");   
              int rocidx = line.indexOf("ReadOperationCount");   
              int umtidx = line.indexOf("UserModeTime");   
              int kmtidx = line.indexOf("KernelModeTime");   
              int wocidx = line.indexOf("WriteOperationCount");   
              long idletime = 0;   
              long kneltime = 0;   
              long usertime = 0;   
              while ((line = input.readLine()) != null) {   
                  if (line.length() < wocidx) {   
                      continue;   
                  }  
                  // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,   
                  String caption = line.substring(capidx, cmdidx).trim();
                  String cmd = line.substring(cmdidx, kmtidx-1).trim();
                  if (cmd.indexOf("wmic.exe") >= 0) {   
                      continue;   
                  }   
                  if (caption.equals("System Idle Process")   
                          || caption.equals("System")) {   
                      idletime += Long.valueOf(line.substring(kmtidx, rocidx - 1).replaceAll(" ", "")).longValue();   
                      idletime += Long.valueOf(line.substring(umtidx, wocidx - 1).replaceAll(" ", "")).longValue();   
                      continue;   
                  }   
                  kneltime += Long.valueOf(   
                          line.substring(kmtidx, rocidx - 1).replaceAll(" ", ""))   
                          .longValue();   
                  usertime += Long.valueOf(   
                          line.substring(umtidx, wocidx - 1).replaceAll(" ", ""))   
                          .longValue();   
              }   
              retn[0] = idletime;   
              retn[1] = kneltime + usertime;   
              return retn;   
          } catch (Exception ex) {   
              ex.printStackTrace();   
          } finally {   
              try {   
                  proc.getInputStream().close();   
              } catch (Exception e) {   
                  e.printStackTrace();   
              }   
          }   
          return null;   
	}
	
	/**
	 * 获取系统各盘符大小
	 */
	public Map<String, Double> getSysSize() {
		File[] files = File.listRoots();
		Map<String, Double> map = null;
		for(int i = 0; i < files.length; i++) {
			 map = new HashMap<String, Double>();
			 long total = (long) files[i].getTotalSpace();  
             long free = (long) files[i].getFreeSpace();
             Double compare = (Double) (1 - free * 1.0 / total) * 100;
             map.put(files[i].getName(), compare);
		}
		return map;
	}
	
}
