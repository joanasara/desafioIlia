package com.example.demo.repository;

import java.util.List;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entities.PointEntities;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PointRepositoryTest {
	
	@Autowired
	private PointRepository repository;
	
	@Test
	public void findByDateIntervalTest() {
		
		List<PointEntities> list = repository.findByDateInterval(Mockito.anyString(), Mockito.anyString());
		assertNotNull(list);
		
		
		
	}
}
