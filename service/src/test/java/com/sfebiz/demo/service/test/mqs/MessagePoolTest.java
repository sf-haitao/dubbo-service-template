package com.sfebiz.demo.service.test.mqs;

import com.aliyun.mqs.client.AsyncCallback;
import com.aliyun.mqs.client.CloudQueue;
import com.aliyun.mqs.client.DefaultMQSClient;
import com.aliyun.mqs.client.MQSClient;
import com.aliyun.mqs.common.ClientException;
import com.aliyun.mqs.common.ServiceException;
import com.aliyun.mqs.model.Message;
import com.aliyun.utils.ServiceSettings;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessagePoolTest {

	protected static final String MESSAGE_BODY = "message body.";
	private static String ENDPOINT1 = ServiceSettings.getMQSEndpoint();
	private MQSClient client;
	private static String QUEUENAME1 = "test1";
	private int MESSAGE_COUNT = 10;

	@Before
	public void init() {
		client = new DefaultMQSClient(ENDPOINT1,
				ServiceSettings.getMQSAccessKeyId(),
				ServiceSettings.getMQSAccessKeySecret());
		client.getQueueRef(QUEUENAME1);

//		QueueMeta meta = new QueueMeta();
//		meta.setQueueName(QUEUENAME1);
//		meta.setPollingWaitSeconds(30);
//
//		CloudQueue queue1 = client.createQueue(meta);
//		Assert.assertEquals(queue1.getQueueURL(), ENDPOINT1 + QUEUENAME1);
	}

	@After
	public void tearDown() throws ServiceException, ClientException {
		CloudQueue queue = client.getQueueRef(QUEUENAME1);
//		common.delete();
	}

	@Test
	public void sendMessage() throws ServiceException, ClientException,
			InterruptedException {

		initMessages(MESSAGE_COUNT);
	}

	private void initMessages(int messageCount) throws InterruptedException {
		Message message = new Message();
		message.setMessageBody(MESSAGE_BODY.getBytes());
		CloudQueue queue = client.getQueueRef(QUEUENAME1);

		// sync call
		final AtomicInteger count = new AtomicInteger(0);
		final ReentrantLock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		Date startTime = new Date();
		for (int i = 0; i < messageCount; i++) {
			queue.asyncPutMessage(message, new AsyncCallback<Message>() {
				public void onSuccess(Message result) {
					count.incrementAndGet();
					if (count.get() >= 10) {
						lock.lock();
						condition.signal();
						lock.unlock();
					}
				}

				public void onFail(Exception ex) {

				}
			});
		}

		lock.lock();
		condition.await();
		lock.unlock();
		System.out.println("Sent " + count.get() + " messages, elapse "
				+ (new Date().getTime() - startTime.getTime()) + " ms");
	}

	@Test
	public void receiveMessage() throws ServiceException, ClientException,
			InterruptedException {
		CloudQueue queue = client.getQueueRef(QUEUENAME1);

//		initMessages(MESSAGE_COUNT);

		final AtomicInteger count = new AtomicInteger(0);
		final ReentrantLock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		Date startTime = new Date();

		for (int i = 0; i < MESSAGE_COUNT; i++) {
			queue.asyncPopMessage(new AsyncCallback<Message>() {
				public void onSuccess(Message result) {
//					Assert.assertEquals(result.getMessageBodyAsString(),
//							MESSAGE_BODY);
					System.out.println(result.getMessageBodyAsString());
					count.incrementAndGet();
					if (count.get() >= MESSAGE_COUNT) {
						lock.lock();
						condition.signal();
						lock.unlock();
					}
				}

				public void onFail(Exception ex) {
					Assert.assertNull(ex);
					lock.lock();
					condition.signal();
					lock.unlock();
				}
			});
		}

		lock.lock();
		condition.await();
		lock.unlock();
		System.out.println("Received " + count.get() + " messages, elapse "
				+ (new Date().getTime() - startTime.getTime()) + " ms");
	}
	
	@Test
	public void peekMessage() throws ServiceException {
		CloudQueue queue = client.getQueueRef(QUEUENAME1);
	}

	@Test
	public void receiveAndDeleteMessage() throws ServiceException,
			ClientException, InterruptedException {
		final CloudQueue queue = client.getQueueRef(QUEUENAME1);

		initMessages(MESSAGE_COUNT);

		final AtomicInteger count = new AtomicInteger(0);
		final ReentrantLock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		Date startTime = new Date();

		for (int i = 0; i < MESSAGE_COUNT; i++) {
			queue.asyncPopMessage(new AsyncCallback<Message>() {
				public void onSuccess(Message result) {
					Assert.assertEquals(result.getMessageBodyAsString(),
							MESSAGE_BODY);
					queue.asyncDeleteMessage(result.getReceiptHandle(),
							new AsyncCallback<Void>() {
								public void onSuccess(Void result) {
									count.incrementAndGet();
									if (count.get() >= MESSAGE_COUNT) {
										lock.lock();
										condition.signal();
										lock.unlock();
									}

								}

								public void onFail(Exception ex) {
									Assert.assertNull(ex);
									lock.lock();
									condition.signal();
									lock.unlock();
								}
							});
				}

				public void onFail(Exception ex) {
					Assert.assertNull(ex);
					lock.lock();
					condition.signal();
					lock.unlock();
				}
			});
		}

		lock.lock();
		condition.await();
		lock.unlock();
		System.out.println("Received and deleted " + count.get()
				+ " messages, elapse "
				+ (new Date().getTime() - startTime.getTime()) + " ms");
	}
	
	@Test
	public void changeVisibility() throws ServiceException,
			ClientException, InterruptedException {
		final CloudQueue queue = client.getQueueRef(QUEUENAME1);

		initMessages(MESSAGE_COUNT);

		final AtomicInteger count = new AtomicInteger(0);
		final ReentrantLock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		Date startTime = new Date();

		for (int i = 0; i < MESSAGE_COUNT; i++) {
			queue.asyncPopMessage(new AsyncCallback<Message>() {
				public void onSuccess(Message result) {
					Assert.assertEquals(result.getMessageBodyAsString(),
							MESSAGE_BODY);
					queue.asyncChangeMessageVisibilityTimeout(result.getReceiptHandle(), 1, new AsyncCallback<String>() {
						public void onSuccess(String result) {
							count.incrementAndGet();
							if (count.get() >= MESSAGE_COUNT) {
								lock.lock();
								condition.signal();
								lock.unlock();
							}
							
						}
						
						public void onFail(Exception ex) {
							Assert.assertNull(ex);
							lock.lock();
							condition.signal();
							lock.unlock();
							
						}});
				}

				public void onFail(Exception ex) {
					Assert.assertNull(ex);
					lock.lock();
					condition.signal();
					lock.unlock();
				}
			});
		}

		lock.lock();
		condition.await();
		lock.unlock();
		System.out.println("Received and changed visibility  " + count.get()
				+ " messages, elapse "
				+ (new Date().getTime() - startTime.getTime()) + " ms");
	}
	
}
