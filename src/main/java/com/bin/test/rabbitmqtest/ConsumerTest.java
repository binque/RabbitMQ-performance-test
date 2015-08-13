package com.bin.test.rabbitmqtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ConsumerTest {
private static Logger logger = LoggerFactory.getLogger(SendTest.class); 
	
	int queueCount;
	int consumerThreadCount;
	int messageTotalCount;
	int connectorTotalCount;
	int prefetchCount;
	byte[] message;
	AtomicInteger totalCountIndex;
	
	private void consumeTask() throws IOException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("10.68.52.203");
		totalCountIndex = new AtomicInteger(messageTotalCount);
		for (int connectorCount = 0; connectorCount < connectorTotalCount; connectorCount++) {
			int connectorIndex = connectorCount;
			final Connection connection = factory.newConnection();
			
			for (int i = 0; i < queueCount; i++) {
				int queueIndex = i;
			
				List<Thread> threads = new ArrayList<Thread>();
				for (int j = 0; j < consumerThreadCount; j++) {
					
					final long[] times = new long[consumerThreadCount];
					
					final Channel channel = connection.createChannel();
					channel.queueDeclare(String.valueOf(i), true, false, false, null);
//					channel.basicQos(prefetchCount, true);
//					channel.txSelect();
					Thread sendThread = new Thread(() -> {
						long startTime = System.currentTimeMillis();
						String threadName = Thread.currentThread().getName();
						logger.debug("connector {} thread name {} queue {}", new Object[]{connectorIndex, threadName, String.valueOf(queueIndex)});
						
						try {
							channel.basicConsume(String.valueOf(queueIndex), true, new DefaultConsumer(channel) {
								@Override
                                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
									totalCountIndex.decrementAndGet();
									if (totalCountIndex.get() == 0) {
										long time = System.currentTimeMillis() - startTime;
										
										if (time == 0) {
											System.out.println("something error.");
											logger.debug("something error.");
											return;
										}
										
										times[Integer.parseInt(threadName)] = time;
										if (Integer.valueOf(threadName) == consumerThreadCount - 1) {
											try {
												Thread.currentThread().sleep(1000 * 3);
											}
											catch (InterruptedException e) {
												e.printStackTrace();
											}
											Arrays.sort(times);
											System.out.println("connection" + connectorIndex + " :" + times[consumerThreadCount - 1]);
											logger.info("connection" + connectorIndex + " :" + times[consumerThreadCount - 1]);
										}
									}
                                }
							});
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					});
					sendThread.setName(String.valueOf(j));
					threads.add(sendThread);
				}
				
				for (Thread sendThread : threads) {
					sendThread.start();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		ConsumerTest consumer = (ConsumerTest)factory.getBean("cusumerTest");
		try {
			consumer.consumeTask();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getQueueCount() {
		return queueCount;
	}

	public void setQueueCount(int queueCount) {
		this.queueCount = queueCount;
	}

	public int getConsumerThreadCount() {
		return consumerThreadCount;
	}

	public void setConsumerThreadCount(int consumerThreadCount) {
		this.consumerThreadCount = consumerThreadCount;
	}

	public int getMessageTotalCount() {
		return messageTotalCount;
	}

	public void setMessageTotalCount(int messageTotalCount) {
		this.messageTotalCount = messageTotalCount;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public int getConnectorTotalCount() {
		return connectorTotalCount;
	}

	public void setConnectorTotalCount(int connectorTotalCount) {
		this.connectorTotalCount = connectorTotalCount;
	}

	public int getPrefetchCount() {
		return prefetchCount;
	}

	public void setPrefetchCount(int prefetchCount) {
		this.prefetchCount = prefetchCount;
	}
}