import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Task implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(Task.class);
	private int id;
	public Task(int id) {
		this.id = id;
	}
	public void run() {
		logger.debug("线程:" + Thread.currentThread().getName() + " 在处理任务:" + id);
	}
}

public class ThreadPoolBasic {
	private static Logger logger = LoggerFactory.getLogger(Task.class);

	public static void main(String[] args) {
		// 1. 创建一个只允许3个 worker【工作线程】的线程池
		ExecutorService tPool = Executors.newFixedThreadPool(3);

		// 2. 把10个任务发给着3个worker去做
		for (int i = 0; i < 10; i++) {
			tPool.submit(new Task(i));
		}

		///////////////////////3. 关闭线程池///////////////////
		// 注意: 线程池的"安详"的关闭，要以下两个步骤
		tPool.shutdown();// 停止接受新任务，但是老任务不停止
		try {
			// 如果 任务执行完毕 或者 1分钟倒计时结束 或者 线程中断中有一个发生, 线程池关闭
			tPool.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
		}
		// //////////////////////////////////////////////////

		logger.debug("所有的任务结束");
	}
}