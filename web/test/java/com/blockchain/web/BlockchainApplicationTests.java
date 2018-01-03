package com.blockchain.web;

import com.blockchain.web.websockets.Emulator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockchainApplicationTests {

	@Autowired
	private Emulator emulator;

	@Test
	public void testTransaction() throws InterruptedException {
        emulator.dumpBalances();
        emulator.run();
        Thread.sleep(8000);
        emulator.dumpBalances();
	}

}
