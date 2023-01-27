package com.eon.ants;

import com.eon.ants.service.PheremoneManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PheremoneManagerTest {

	@Autowired
	@Qualifier("PheremoneManager")
	private PheremoneManager mgr;
	@Test
	void getPheremoneLevels() {
		//{circus, balloon, bumpers, carousel, swings, ferrisWheel};
		double[][] distanceMatrix={ { 0, 8, 7, 4, 6, 4 },
									{ 8, 0, 5, 7, 11, 5 },
									{ 7, 5, 0, 9, 6, 7 },
									{ 4, 7, 9, 0, 5, 6 },
									{ 6, 11, 6, 5, 0, 3 },
									{ 4, 5, 7, 6, 3, 0 }
						};

		mgr.setPheremoneTrails(distanceMatrix);
		assertEquals(11.0,mgr.getPheremoneLevel("swings","balloon"));

	}

}