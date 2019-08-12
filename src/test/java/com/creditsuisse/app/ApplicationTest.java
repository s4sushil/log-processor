package com.creditsuisse.app;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@PropertySource("classpath:application.properties")
public class ApplicationTest {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
    @Test(expected = InvalidParameterException.class)
    public void testRun_EmptyFilePath() throws IOException {
        String[] args = {""};
        Application.main(args);
    }

    @Test(expected = InvalidParameterException.class)
    public void testRun_InvalidFilePath() throws IOException {
        String[] args = {"invalid"};
        Application.main(args);
    }
	
	@Test
	public void runBatchInserter() throws Exception {
		String[] args = {"src/test/resources/test.txt"};        
		Application.main(args);
		
		long count = jdbcTemplate.queryForObject("SELECT count(*) FROM event", Long.class);
		assertThat(Files.lines(Paths.get(args[0])).count(), equalTo(count));
		jdbcTemplate.getDataSource().getConnection().close();
	}
	
	@Test
	public void runBatchToCountNumberOfAlerts() throws Exception {
		String[] args = {"src/test/resources/testAlert.txt"};        
		Application.main(args);
		
		long count = jdbcTemplate.queryForObject("SELECT count(*) FROM event", Long.class);
		assertThat(Files.lines(Paths.get(args[0])).count(), equalTo(count));

		int alertCount = jdbcTemplate.queryForObject("SELECT count(*) FROM event where alert=true", Integer.class);
		assertThat(1, equalTo(alertCount));
	}

}
